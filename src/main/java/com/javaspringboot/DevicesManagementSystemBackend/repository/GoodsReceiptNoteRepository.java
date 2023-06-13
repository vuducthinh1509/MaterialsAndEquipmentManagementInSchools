package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.model.GoodsReceiptNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsReceiptNoteRepository extends JpaRepository<GoodsReceiptNote,Long> {
    List<GoodsReceiptNote> findByUserId(Long id);
}