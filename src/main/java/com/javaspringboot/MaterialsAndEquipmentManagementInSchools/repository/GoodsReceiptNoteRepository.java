package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.GoodsReceiptNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsReceiptNoteRepository extends JpaRepository<GoodsReceiptNote,Long> {
    List<GoodsReceiptNote> findByUserId(Long id);

    @Query(value = "SELECT pn.* FROM users u JOIN goods_receipt_note pn ON u.id = pn.user_id WHERE u.username = ?1",nativeQuery = true)
    List<GoodsReceiptNote> findByUsername(String username);
}
