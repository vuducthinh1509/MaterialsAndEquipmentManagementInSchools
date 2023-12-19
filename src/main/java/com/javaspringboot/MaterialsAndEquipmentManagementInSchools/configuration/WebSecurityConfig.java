package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.configuration;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.security.jwt.JwtAccessDeniedHandler;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.security.jwt.JwtAuthenticationEntryPoint;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.security.jwt.JwtAuthorizationFilter;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  String[] forgotPasswordList = new String[]{"/api/resetPassword","/api/verifyToken","/api/savePassword"};

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private JwtAuthenticationEntryPoint unauthorizedHandler;

  @Bean
  public JwtAuthorizationFilter authenticationJwtTokenFilter() {
    return new JwtAuthorizationFilter();
  }


  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
      return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/","/ws/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
            .antMatchers(forgotPasswordList).permitAll()
            .antMatchers("/api-docs/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
        .anyRequest().authenticated();
    
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }


  @Bean
  public AccessDeniedHandler accessDeniedHandler(){
    return new JwtAccessDeniedHandler();
  }

}
