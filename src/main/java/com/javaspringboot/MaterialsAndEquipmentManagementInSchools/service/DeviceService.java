package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.service;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.Device;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;


    public List<Device> findDeviceByStatus(String status) {
        return deviceRepository.findDeviceWithStatus(status);
    }
}
