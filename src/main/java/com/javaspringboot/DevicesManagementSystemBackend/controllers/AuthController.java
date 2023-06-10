package com.javaspringboot.DevicesManagementSystemBackend.controllers;

import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.EmailExistException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.UsernameExistException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.token.TokenRefreshException;
import com.javaspringboot.DevicesManagementSystemBackend.models.ERole;
import com.javaspringboot.DevicesManagementSystemBackend.models.RefreshToken;
import com.javaspringboot.DevicesManagementSystemBackend.models.Role;
import com.javaspringboot.DevicesManagementSystemBackend.models.User;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.auth.LoginRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.auth.SignupRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.RoleRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.security.jwt.JwtUtils;
import com.javaspringboot.DevicesManagementSystemBackend.security.services.RefreshTokenService;
import com.javaspringboot.DevicesManagementSystemBackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController extends ExceptionHandling {
  @Autowired
  AuthenticationManager authenticationManager;


  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
    
    ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

    return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
              .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
              .body(userDetails.getUser());
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws UsernameExistException, EmailExistException {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw new UsernameExistException("Username is already taken!");
    }
    if(userRepository.existsByEmail(signUpRequest.getEmail())){
      throw new EmailExistException("Email is already taken!");
    }

    User user = new User(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()), signUpRequest.getFullname(), signUpRequest.getBirthDate(),signUpRequest.getPhone(),signUpRequest.getTenVien(),signUpRequest.getTenPhong(),signUpRequest.getTenBan());
    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();
    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);
          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }
    user.setRoles(roles);
    userRepository.save(user);
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principle.toString() != "anonymousUser") {
      refreshTokenService.deleteByUserId(((UserDetailsImpl) principle).getId());
    }
    
    ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
    ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
    String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
    
    if ((refreshToken != null) && (refreshToken.length() > 0)) {
      return refreshTokenService.findByToken(refreshToken)
          .map(refreshTokenService::verifyExpiration)
          .map(RefreshToken::getUser)
          .map(user -> {
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new MessageResponse("Token is refreshed successfully!"));
          })
          .orElseThrow(() -> new TokenRefreshException(refreshToken,
              "Refresh token is not in database!"));
    }
    
    return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
  }
}
