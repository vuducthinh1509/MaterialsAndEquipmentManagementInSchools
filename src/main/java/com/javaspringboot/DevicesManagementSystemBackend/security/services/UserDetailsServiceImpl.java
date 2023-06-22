package com.javaspringboot.DevicesManagementSystemBackend.security.services;

import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.UserNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  public static final String NO_USER_FOUND_BY_USERNAME = "No user found by username: ";
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByUsername(username);
    if(user==null){
      throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
    } else {
      userRepository.save(user);
      UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);
      return userDetailsImpl;
    }
  }

}
