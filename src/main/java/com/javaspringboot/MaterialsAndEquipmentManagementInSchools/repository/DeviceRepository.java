package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DeviceRepository extends JpaRepository<Device,Long> {

    Optional<Device> findDeviceBySerial(String serial);

    Set<Device> findByGoodsReceiptNoteId(Long id);

    @Query(value = "select d.* from devices d where d.status = :status",nativeQuery = true)
    List<Device> findDeviceWithStatus(String status);


    @Query(value = "select * from devices d where d.category_id  = ?1",nativeQuery = true)
    List<Device> findDeviceByCategoryId(Long id);
}
