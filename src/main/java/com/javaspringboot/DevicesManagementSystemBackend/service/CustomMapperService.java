package com.javaspringboot.DevicesManagementSystemBackend.service;

import com.javaspringboot.DevicesManagementSystemBackend.advice.CustomMapper;
import com.javaspringboot.DevicesManagementSystemBackend.dto.CategoryDTO;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.DeviceResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CustomMapperService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ModelMapperService modelMapperService;

    public CustomMapper<Device, DeviceResponse> customMapperDevice = device -> {
        DeviceResponse deviceResponse = mapper.map(device,DeviceResponse.class);
        deviceResponse.setCategory(new CategoryDTO(device.getCategory().getName(),device.getCategory().getDescription()));
        return deviceResponse;
    };

    public List<DeviceResponse> mapListDevice(List<Device> list){
        return modelMapperService.mapList(list,customMapperDevice);
    }
}
