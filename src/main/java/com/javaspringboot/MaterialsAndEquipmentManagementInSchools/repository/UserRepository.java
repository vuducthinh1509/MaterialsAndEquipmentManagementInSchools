package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findUserByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  @Query(value = "SELECT u.* FROM users u WHERE u.fullname like %:name%",nativeQuery = true)
  List<User> findUserLikeName(String name);

  User findUserByEmail(String email);

  User findUserByRefreshToken(String refreshToken);
}
