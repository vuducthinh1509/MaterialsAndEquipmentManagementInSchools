package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.service;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.advice.CustomMapper;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.dto.CategoryDTO;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.Device;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response.DeviceResponse;
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

    public List<DeviceResponse> mapSetDevice(Set<Device> list){
        return modelMapperService.mapSet(list,customMapperDevice);
    }

    public DeviceResponse mapDevice(Device device){
        return modelMapperService.mapObject(device,customMapperDevice);
    }
}
