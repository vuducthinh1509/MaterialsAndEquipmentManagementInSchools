package com.javaspringboot.DevicesManagementSystemBackend.security.services;

import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

  private User user;
  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(User user) {
    this.user = user;
    this.authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
            .collect(Collectors.toList());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId(){
    return this.user.getId();
  }

  public User getUser(){
    return this.user;
  }
  @Override
  public String getPassword() {
    return this.user.getPassword();
  }

  @Override
  public String getUsername() {
    return this.user.getUsername();
  }
//  public static UserDetailsImpl build(User user) {
//    String[] authorities = user.getRoles().stream()
//            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//            .collect(Collectors.toList());
//
//    user.setAuthorities(authorities);
//
//    return new UserDetailsImpl(user);
//  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

//  @Override
//  public boolean equals(Object o) {
//    if (this == o)
//      return true;
//    if (o == null || getClass() != o.getClass())
//      return false;
//    UserDetailsImpl user = (UserDetailsImpl) o;
//    return Objects.equals(id, user.id);
//  }


  public void setUser(User user) {
    this.user = user;
  }
}
