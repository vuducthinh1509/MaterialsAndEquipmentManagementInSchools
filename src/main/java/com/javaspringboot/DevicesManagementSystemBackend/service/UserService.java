package com.javaspringboot.DevicesManagementSystemBackend.service;

import com.javaspringboot.DevicesManagementSystemBackend.model.User;

public interface UserService {
    void clearRefreshToken(Long id);

    String createRefreshToken(Long id);

    User findByRefreshToken(String refreshToken);

    boolean verifyExpiration(User user);

    String createNewRefreshToken(Long id);
}
