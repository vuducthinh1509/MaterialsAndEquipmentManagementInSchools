package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.model.WarrantyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarrantyCardRepository extends JpaRepository<WarrantyCard,Long> {

    @Query(value = "select * from warranty_card wc  where wc.device_id = ?1",nativeQuery = true)
    List<WarrantyCard> findWarrantyCardByDeviceId(Long id);

    @Query(value = "select * from warranty_card wc  where wc.id_receiver = ?1",nativeQuery = true)
    List<WarrantyCard> findWarrantyCardByReceiverId(Long id);
}
