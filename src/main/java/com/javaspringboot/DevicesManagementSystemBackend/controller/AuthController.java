package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.advice.CustomMapper;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.ERole;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.EmailExistException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.UsernameExistException;
import com.javaspringboot.DevicesManagementSystemBackend.model.Role;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.LoginRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.SignupRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.UserResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.RoleRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.security.jwt.JwtUtils;
import com.javaspringboot.DevicesManagementSystemBackend.security.services.UserDetailsImpl;
import com.javaspringboot.DevicesManagementSystemBackend.service.Impl.UserServiceImpl;
import com.javaspringboot.DevicesManagementSystemBackend.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController extends ExceptionHandling {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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
  UserServiceImpl userService;

  @Autowired
  private ModelMapper mapper;

  @Autowired
  private ModelMapperService mapperService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws DisabledException {

    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    logger.info(String.format("Login attemps: %s",userDetails.getUsername()));

    String token = userService.createRefreshToken(userDetails.getId());

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails.getUser());

    ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(token);
    return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
            .body(mapperService.mapObject(userDetails.getUser(),customMapper));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws UsernameExistException, EmailExistException,HttpMessageNotReadableException {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw new UsernameExistException("Username is already taken!");
    }
    if(userRepository.existsByEmail(signUpRequest.getEmail())){
      throw new EmailExistException("");
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
      userService.clearRefreshToken(((UserDetailsImpl) principle).getId());
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
    Instant currentTime = Instant.now();
    if(refreshToken == null || refreshToken.length()!=36){
      return ResponseEntity.badRequest().body(new MessageResponse("Invalid Refresh Token format"));
    }
    User user = userService.findByRefreshToken(refreshToken);
    long secondsDiff = Duration.between(currentTime, user.getExpiryRefreshToken()).getSeconds();
    if(user==null){
      return ResponseEntity.badRequest().body(new MessageResponse("Refresh token is not in database!"));
    }
    if(userService.verifyExpiration(user)){
      return ResponseEntity.badRequest().body(new MessageResponse("Refresh token was expired. Please make a new signin request"));
    } else {
      String token = userService.createNewRefreshToken(user.getId());
      ResponseCookie jwtRefreshCookie = jwtUtils.generateNewRefreshJwtCookie(token,secondsDiff);
      ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
      return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
              .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
              .body(new MessageResponse("Token is refreshed successfully!"));
    }
  }

  public CustomMapper<User, UserResponse> customMapper = user -> {
    UserResponse userResponse = mapper.map(user,UserResponse.class);
    Set<Role> setRoles = user.getRoles();
    List<ERole> listRoles =  setRoles.stream().map(role -> role.getName()).collect(Collectors.toList());
    userResponse.setRoles(listRoles);
    return userResponse;
  };
}
