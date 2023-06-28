package com.javaspringboot.DevicesManagementSystemBackend.service.Impl;

import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void clearRefreshToken(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setRefreshToken(null);
            user.setExpiryRefreshToken(null);
            userRepository.save(user);
        }
    }

    @Override
    public String createRefreshToken(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            String token = UUID.randomUUID().toString();
            user.setRefreshToken(token);
            user.setExpiryRefreshToken(Instant.now().plusMillis(refreshTokenDurationMs));
            userRepository.save(user);
            return token;
        }
        return null;
    }


    @Override
    public User findByRefreshToken(String refreshToken) {
        return userRepository.findUserByRefreshToken(refreshToken);
    }

    @Override
    public boolean verifyExpiration(User user) {
        return user.getExpiryRefreshToken().compareTo(Instant.now()) < 0;
    }

    @Override
    public String refreshToken(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            String token = UUID.randomUUID().toString();
            user.setRefreshToken(token);
            userRepository.save(user);
            return token;
        }
        return null;
    }


}
