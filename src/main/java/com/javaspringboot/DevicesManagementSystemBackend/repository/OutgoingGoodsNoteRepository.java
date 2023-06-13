package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.model.OutgoingGoodsNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutgoingGoodsNoteRepository extends JpaRepository<OutgoingGoodsNote,Long> {
}
