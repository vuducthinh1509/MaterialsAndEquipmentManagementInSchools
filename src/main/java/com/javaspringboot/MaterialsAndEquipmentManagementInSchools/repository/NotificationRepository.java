package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "SELECT * FROM notification WHERE id <= (SELECT MAX(id) FROM notification  WHERE is_read = false ORDER BY created_at DESC) and type = 'ADMIN_TO_SPECIFIC' and user_id = ?1",nativeQuery = true)
    List<Notification> getAllNotificationFromUser(Long id);

    @Query(value = "SELECT * from notification where user_id = ?1 and type = 'ADMIN_TO_SPECIFIC' ORDER BY created_at DESC ",nativeQuery = true)
    List<Notification> getAllByUserId(Long id);

    @Query(value = "SELECT * from notification where type = 'USER_TO_ADMIN' ORDER BY created_at DESC",nativeQuery = true)
    List<Notification> getAllNotificationByAdmin();
}
