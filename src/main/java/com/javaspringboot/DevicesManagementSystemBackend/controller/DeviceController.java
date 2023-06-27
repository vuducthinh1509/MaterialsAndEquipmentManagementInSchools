package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.advice.CustomMapper;
import com.javaspringboot.DevicesManagementSystemBackend.advice.HttpResponse;
import com.javaspringboot.DevicesManagementSystemBackend.dto.CategoryDTO;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.ERole;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusDevice;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.DeviceNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.UserNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.model.*;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.UpdateDeviceRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.DeviceResponse;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.UserResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.CategoryRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.DeviceRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.OutgoingGoodsNoteRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.service.CustomMapperService;
import com.javaspringboot.DevicesManagementSystemBackend.service.DeviceService;
import com.javaspringboot.DevicesManagementSystemBackend.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RequestMapping("/api/device")
@RestController
public class DeviceController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ModelMapperService mapperService;
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CustomMapperService customMapperService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OutgoingGoodsNoteRepository outgoingGoodsNoteRepository;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Device>> getAll(){
        List<Device> devices = deviceRepository.findAll();
        return new ResponseEntity(mapperService.mapList(devices,customMapper), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getByType(@RequestParam String type,@RequestParam String data) throws DeviceNotFoundException {
        if(type.isBlank()){
            return new ResponseEntity(new MessageResponse("Type is not blank"),HttpStatus.BAD_REQUEST);
        } else {
            if(type.equalsIgnoreCase("serial")){
                Optional<Device> device = deviceRepository.findDeviceBySerial(data);
                if(!device.isPresent()){
                    throw new DeviceNotFoundException(data);
                }
                return new ResponseEntity(mapperService.mapObject(device.get(),customMapper), HttpStatus.OK);
            } else if(type.equalsIgnoreCase("status")){
                List<Device> devices = new ArrayList<>();
                if(data.equalsIgnoreCase("trong_kho")){
                    devices = deviceService.findDeviceByStatus(0);
                } else if(data.equalsIgnoreCase("da_xuat")){
                    devices = deviceService.findDeviceByStatus(1);
                }
                return new ResponseEntity(mapperService.mapList(devices,customMapper), HttpStatus.OK);
            } else {
                return new ResponseEntity(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,"","Type is not exist"),HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(@RequestParam Long id,@Valid @RequestBody UpdateDeviceRequest updateDeviceRequest) throws SQLIntegrityConstraintViolationException {
        Optional<Category> category = categoryRepository.findById(updateDeviceRequest.getCategoryId());
        Device device = deviceRepository.findById(id).get();
        device.setName(updateDeviceRequest.getName());
        device.setSerial(updateDeviceRequest.getSerial());
        device.setPrice(updateDeviceRequest.getPrice());
        device.setMaintenanceTime(updateDeviceRequest.getMaintenanceTime());
        device.setWarrantyTime(updateDeviceRequest.getWarrantyTime());
        device.setCategory(category.get());
        try {
            deviceRepository.save(device);
            return new ResponseEntity(new MessageResponse("Update succesfully"), HttpStatus.OK);
        } catch (Exception exception) {
            throw new SQLIntegrityConstraintViolationException("Serial already exist");
        }
    }

    @GetMapping("/get-all-devices-by-user")
    public ResponseEntity<Set<DeviceResponse>> listByUser(@RequestParam String username) {
        List<OutgoingGoodsNote> list = outgoingGoodsNoteRepository.findByUsername(username);
        List<Device> devices = new ArrayList<>();
        for(OutgoingGoodsNote outgoingGoodsNote : list){
            devices.addAll(outgoingGoodsNote.getDevices());
        }
        return new ResponseEntity(customMapperService.mapListDevice(devices), HttpStatus.OK);
    }

    @GetMapping("/list-devices-by-category-name")
    public ResponseEntity<Set<DeviceResponse>> listByCategoryName(@RequestParam(required = true) String categoryName){
        List<Category> categories = categoryRepository.findCategoriesByName(categoryName);
        List<Device> devices = new ArrayList<>();
        for(Category category : categories){
            List<Device> deviceList = deviceRepository.findDeviceByCategoryId(category.getId());
            devices.addAll(deviceList);
        }
        return new ResponseEntity(customMapperService.mapListDevice(devices), HttpStatus.OK);
    }

    public CustomMapper<Device, DeviceResponse> customMapper = device -> {
        DeviceResponse deviceResponse = mapper.map(device,DeviceResponse.class);
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(device.getCategory().getName());
        categoryDTO.setDescription(device.getCategory().getDescription());
        deviceResponse.setCategory(categoryDTO);
        return deviceResponse;
    };
}
