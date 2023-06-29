package com.javaspringboot.DevicesManagementSystemBackend.security.jwt;


import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Value("${app.jwtCookieName}")
  private String jwtCookie;

  @Value("${app.jwtRefreshCookieName}")
  private String jwtRefreshCookie;

  private final String API_REFRESHTOKEN = "/api/auth/refreshtoken";

  public ResponseCookie generateJwtCookie(User user) {
    String jwt = generateTokenFromUsername(user.getUsername());
    return generateCookieForJwtToken(jwtCookie, jwt, "/api");
  }

  public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
    return generateCookieForRefreshToken(jwtRefreshCookie, refreshToken, API_REFRESHTOKEN);
  }

  public ResponseCookie generateNewRefreshJwtCookie(String refreshToken, Long maxAge) {
    return generateCookieForNewRefreshToken(jwtRefreshCookie, refreshToken, API_REFRESHTOKEN, maxAge);
  }

  public String getJwtFromCookies(HttpServletRequest request) {
    return getCookieValueByName(request, jwtCookie);
  }

  public String getJwtRefreshFromCookies(HttpServletRequest request) {
    return getCookieValueByName(request, jwtRefreshCookie);
  }

  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    return cookie;
  }

  public ResponseCookie getCleanJwtRefreshCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtRefreshCookie, null).path(API_REFRESHTOKEN).build();
    return cookie;
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  private ResponseCookie generateCookieForRefreshToken(String name, String value, String path) {
    ResponseCookie cookie = ResponseCookie.from(name, value).path(path).maxAge(refreshTokenDurationMs/1000).httpOnly(true).build();
    return cookie;
  }

  private ResponseCookie generateCookieForJwtToken(String name, String value, String path) {
    ResponseCookie cookie = ResponseCookie.from(name, value).path(path).maxAge(jwtExpirationMs/1000).httpOnly(true).build();
    return cookie;
  }

  private ResponseCookie generateCookieForNewRefreshToken(String name, String value,String path,Long maxAge){
    ResponseCookie cookie = ResponseCookie.from(name, value).path(path).maxAge(maxAge).httpOnly(true).build();
    return cookie;
  }

  private String getCookieValueByName(HttpServletRequest request, String name) {
    Cookie cookie = WebUtils.getCookie(request, name);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }
}
