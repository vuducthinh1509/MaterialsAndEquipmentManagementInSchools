package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.models.RefreshToken;
import com.javaspringboot.DevicesManagementSystemBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByUser(User user);
}
