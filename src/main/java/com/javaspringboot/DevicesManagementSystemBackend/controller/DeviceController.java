package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public ResponseEntity<List<Device>> getAllDevice(){
        try {
            List<Device> devices = deviceRepository.findAll();
            return new ResponseEntity(devices, HttpStatus.OK);
        } catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getDevicesBySerial(@RequestParam(value = "serial",required = false) String serial,@RequestParam(value = "status",required = false) String status){
        if(serial==null&&!status.isBlank()){
            Set<Device> devices = deviceService.findDeviceByStatus(status);
            return new ResponseEntity(devices, HttpStatus.OK);
        } else if(!serial.isBlank() && status==null){
            Optional<Device> device = deviceRepository.findDeviceBySerial(serial);
            return new ResponseEntity(device.get(), HttpStatus.OK);
        }
        return new ResponseEntity(new MessageResponse("Action failed"),HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDeviceInfo(@RequestParam Long id,@Valid @RequestBody UpdateDeviceRequest updateDeviceRequest) throws SQLIntegrityConstraintViolationException {
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
