package com.javaspringboot.DevicesManagementSystemBackend.service;

import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import org.springframework.stereotype.Service;

public interface UserService {
    void clearRefreshToken(Long id);

    String createRefreshToken(Long id);

    User findByRefreshToken(String refreshToken);

    boolean verifyExpiration(User user);
}
