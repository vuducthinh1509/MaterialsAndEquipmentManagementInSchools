package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken,Long> {

    @Query(value = "select * from password_reset_token where token =?1",nativeQuery = true)
    PasswordResetToken findByToken(String token);

    @Query(value = "SELECT prt.user_id FROM password_reset_token prt WHERE prt.token = ?1",nativeQuery = true)
    int getUserIdByPasswordResetToken(String token);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM password_reset_token where token =?1",nativeQuery = true)
    void deletePasswordResetTokenByToken(String token);

    PasswordResetToken findByUserId(Long id);
}
