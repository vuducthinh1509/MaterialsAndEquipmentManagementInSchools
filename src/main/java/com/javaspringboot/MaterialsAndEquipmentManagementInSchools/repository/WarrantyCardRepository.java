package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.WarrantyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarrantyCardRepository extends JpaRepository<WarrantyCard,Long> {

    @Query(value = "select * from warranty_card wc  where wc.device_id = ?1",nativeQuery = true)
    List<WarrantyCard> findWarrantyCardByDeviceId(Long id);

    @Query(value = "select * from warranty_card wc  where wc.id_receiver = ?1",nativeQuery = true)
    List<WarrantyCard> findWarrantyCardByReceiverId(Long id);
    @Query(value = "select * from warranty_card wc  where wc.confirm_status = 'CHUA_XAC_NHAN'",nativeQuery = true)
    List<WarrantyCard> listByStatusUnconfirm();
}
