package com.javaspringboot.DevicesManagementSystemBackend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaspringboot.DevicesManagementSystemBackend.advice.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";

  public static final String DISABLED_MESSAGE = "Account is disabled";

  public static final HttpStatus status = FORBIDDEN;


  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    String message = FORBIDDEN_MESSAGE;
    if(authException instanceof DisabledException){
        message = DISABLED_MESSAGE;
    }
    HttpResponse httpResponse = new HttpResponse(status.value(), status, status.getReasonPhrase().toUpperCase(), message);
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(status.value());
    OutputStream outputStream = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(outputStream, httpResponse);
    outputStream.flush();
  }

}
