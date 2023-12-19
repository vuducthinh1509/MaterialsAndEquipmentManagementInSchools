package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.controller;


import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.HttpResponse;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.dto.PasswordDTO;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.exception.domain.UserNotFoundException;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.PasswordResetToken;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.User;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response.MessageResponse;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.PasswordTokenRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.UserRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.security.services.UserDetailsImpl;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.service.Impl.SendGridMailServiceImpl;
import com.sendgrid.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
public class ForgotPasswordController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private SendGridMailServiceImpl sendGridMailService;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String userEmail){
        User user = userRepository.findUserByEmail(userEmail);
        if (user == null) {
            return new ResponseEntity<>(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,"",String.format("No user found with email %s",userEmail)),HttpStatus.BAD_REQUEST);
        }
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByUserId(user.getId());
        if(passwordResetToken!=null){
            passwordTokenRepository.deleteById(passwordResetToken.getId());
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
        String subject = "DevicesManagementSystem - Reset Forgotten Password";
        Content content = new Content("text/html", String.format("<html>\n" +
                "<body>\n" +
                "<p>Hello %s,</p>\n" +
                "<p>Please click below to reset your password.</p>\n" +
                "<p><a href=\"http://localhost:3000/resetPassword?token=%s\">Click here</a></p>\n" +
                "<p>The above link is valid only for <strong>24 Hours</strong>.</p>\n" +
                "<p>If you did not ask to reset your password, please ignore this message.</p>" +
                "<p>Thank you.</p>\n" +
                "</body>\n" +
                "</html>",user.getUsername(),token));
        sendGridMailService.sendMail(subject,content,user.getEmail());
        return new ResponseEntity<>(new HttpResponse(OK.value(), OK,"","Email sent"), OK);
    }

    @GetMapping("/verifyToken")
    public ResponseEntity<?> showChangePasswordPage(@RequestParam("token") String token) {
        String result = sendGridMailService.validatePasswordResetToken(token);
        if(result != null) {
            return new ResponseEntity<>(new MessageResponse("Invalid token or Token is expired"),HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new MessageResponse("Valid token"),HttpStatus.OK);
        }
    }
    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(@Valid @RequestBody PasswordDTO passwordDto) {
        String result = sendGridMailService.validatePasswordResetToken(passwordDto.getToken());
        if(result != null) {
            return new ResponseEntity<>(new MessageResponse("Invalid token or Token is expired"),HttpStatus.BAD_REQUEST);
        }
        long userId = passwordTokenRepository.getUserIdByPasswordResetToken(passwordDto.getToken());
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            sendGridMailService.changeUserPassword(user.get(), passwordDto.getNewPassword());
            passwordTokenRepository.deletePasswordResetTokenByToken(passwordDto.getToken());
            return new ResponseEntity<>(new MessageResponse("Change password successfully"), OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Change password failed"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAnyAuthority('ROLE_USER') or hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Authentication authentication) throws UserNotFoundException {
        try {
            Authentication _authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authentication.getName(), oldPassword));
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            return new ResponseEntity<>(new MessageResponse("Change password successfully"), OK);

        } catch (Exception e){
            return new ResponseEntity<>(new MessageResponse("Old password does not match"),HttpStatus.BAD_REQUEST);
        }

    }
}
