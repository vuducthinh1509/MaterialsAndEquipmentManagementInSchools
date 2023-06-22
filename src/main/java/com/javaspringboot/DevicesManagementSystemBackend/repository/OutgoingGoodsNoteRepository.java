package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.model.GoodsReceiptNote;
import com.javaspringboot.DevicesManagementSystemBackend.model.OutgoingGoodsNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutgoingGoodsNoteRepository extends JpaRepository<OutgoingGoodsNote,Long> {
    @Query(value = "SELECT px.* FROM users u JOIN outgoing_goods_note px ON u.id = px.id_receiver WHERE u.username = ?1",nativeQuery = true)
    List<OutgoingGoodsNote> findByUsername(String username);
}
