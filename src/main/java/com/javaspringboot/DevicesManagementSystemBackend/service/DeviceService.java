package com.javaspringboot.DevicesManagementSystemBackend.service;

import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusDevice;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;


    public List<Device> findDeviceByStatus(int status) {
        return deviceRepository.findDeviceWithStatus(status);
    }
}
