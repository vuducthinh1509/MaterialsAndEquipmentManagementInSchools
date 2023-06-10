package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

public interface DeviceRepository extends JpaRepository<Device,Long> {

    Optional<Device> findDeviceBySerial(String serial);

    Set<Device> findByGoodsReceiptNoteId(Long id);

    @Query(value = "select d.* from devices d where d.status =?1",nativeQuery = true)
    Set<Device> findDeviceWithStatus(String status);
}
