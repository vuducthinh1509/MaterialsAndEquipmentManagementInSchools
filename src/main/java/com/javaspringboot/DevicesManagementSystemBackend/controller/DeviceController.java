package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.advice.HttpResponse;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.DeviceNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.model.Category;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.UpdateDeviceRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.CategoryRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.DeviceRepository;
import com.javaspringboot.DevicesManagementSystemBackend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RequestMapping("/api/device")
@RestController
public class DeviceController extends ExceptionHandling {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/list")
    public ResponseEntity<List<Device>> getAll(){
        List<Device> devices = deviceRepository.findAll();
        return new ResponseEntity(devices, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getByType(@RequestParam String type,@RequestParam String data) {
        if(type.isBlank()){
            return new ResponseEntity(new MessageResponse("Type is not blank"),HttpStatus.BAD_REQUEST);
        } else {
            if(type.equalsIgnoreCase("serial")){
                Optional<Device> device = deviceRepository.findDeviceBySerial(data);
                return new ResponseEntity(device.get(), HttpStatus.OK);
            } else if(type.equalsIgnoreCase("status")){
                Set<Device> devices = deviceService.findDeviceByStatus(data);
                return new ResponseEntity(devices, HttpStatus.OK);
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
}
