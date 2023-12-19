package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.service;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.User;

public interface UserService {
    void clearRefreshToken(Long id);

    String createRefreshToken(Long id);

    User findByRefreshToken(String refreshToken);

    boolean verifyExpiration(User user);

    String createNewRefreshToken(Long id);
}
