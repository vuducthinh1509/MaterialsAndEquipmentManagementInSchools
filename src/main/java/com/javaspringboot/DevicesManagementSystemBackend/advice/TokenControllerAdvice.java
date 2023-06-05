package com.javaspringboot.DevicesManagementSystemBackend.advice;

import com.javaspringboot.DevicesManagementSystemBackend.exception.token.TokenRefreshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TokenControllerAdvice {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(value = TokenRefreshException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public HttpResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
       return new HttpResponse(HttpStatus.FORBIDDEN.value(),HttpStatus.FORBIDDEN,null,ex.getMessage());
  }
}