package com.javaspringboot.DevicesManagementSystemBackend.security.services;

import com.javaspringboot.DevicesManagementSystemBackend.models.User;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.javaspringboot.DevicesManagementSystemBackend.constant.UserImplConstant.FOUND_USER_BY_USERNAME;
import static com.javaspringboot.DevicesManagementSystemBackend.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private Logger LOGGER = LoggerFactory.getLogger(getClass());
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByUsername(username);
    if (user == null) {
      LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
      throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
    } else {
      userRepository.save(user);
      UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);
      LOGGER.info(FOUND_USER_BY_USERNAME + username);
      return userDetailsImpl;
    }
  }

}
