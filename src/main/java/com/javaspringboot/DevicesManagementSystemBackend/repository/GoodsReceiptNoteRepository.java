package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.models.GoodsReceiptNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoodsReceiptNoteRepository extends JpaRepository<GoodsReceiptNote,Long> {
    List<GoodsReceiptNote> findByUserId(Long id);
}
