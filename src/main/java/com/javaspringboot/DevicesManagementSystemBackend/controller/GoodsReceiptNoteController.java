package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.advice.CustomMapper;
import com.javaspringboot.DevicesManagementSystemBackend.advice.HttpResponse;
import com.javaspringboot.DevicesManagementSystemBackend.dto.DeviceDTO;
import com.javaspringboot.DevicesManagementSystemBackend.dto.GoodsReceiptNoteDTO;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.*;
import com.javaspringboot.DevicesManagementSystemBackend.model.Category;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.model.GoodsReceiptNote;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.UpdateGoodsReceiptNoteRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.GoodsReceiptNoteResponse;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.CategoryRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.DeviceRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.GoodsReceiptNoteRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.service.CustomMapperService;
import com.javaspringboot.DevicesManagementSystemBackend.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/phieunhap")
@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
public class GoodsReceiptNoteController extends ExceptionHandling {

    private static final Logger logger = LoggerFactory.getLogger(GoodsReceiptNoteController.class);

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ModelMapperService modelMapperService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GoodsReceiptNoteRepository goodsReceiptNoteRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CustomMapperService customMapperService;

    @PostMapping("/add")
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody GoodsReceiptNoteDTO goodsReceiptNoteDTO,Authentication authentication) throws UserNotFoundException, DeviceExistException,CategoryNotFoundException{
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UserNotFoundException(username);
        }
        GoodsReceiptNote goodsReceiptNote = new GoodsReceiptNote(goodsReceiptNoteDTO.getFullname(), goodsReceiptNoteDTO.getPhone(), goodsReceiptNoteDTO.getCompanyName());
        goodsReceiptNote.setUser(user);
        List<Device> devices = new ArrayList<>();
        Set<DeviceDTO> devicesDTO = goodsReceiptNoteDTO.getDevices();
        if(devicesDTO.isEmpty()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"","Devices can not be empty"));
        }
        for(DeviceDTO deviceDTO : devicesDTO){
            String serial = deviceDTO.getSerial();
            if(deviceRepository.findDeviceBySerial(serial).isPresent()){
                throw new DeviceExistException(serial);
            } else {
                Device _temp = new Device(deviceDTO.getName(),deviceDTO.getSerial(),deviceDTO.getPrice(),deviceDTO.getWarrantyTime(),deviceDTO.getMaintenanceTime());
                Category category = categoryRepository.findCategoryByNameAndDescription(deviceDTO.getCategoryName(), deviceDTO.getCategoryDescription());
                if(category!=null){
                    _temp.setCategory(category);
                } else {
                    Category newCategory = new Category(deviceDTO.getCategoryName(), deviceDTO.getCategoryDescription());
                    categoryRepository.save(newCategory);
                    _temp.setCategory(newCategory);
                }
                _temp.setGoodsReceiptNote(goodsReceiptNote);
                devices.add(_temp);
            }
        }
        goodsReceiptNote.setDevices(devices);
        try {
            goodsReceiptNoteRepository.save(goodsReceiptNote);
            return new ResponseEntity(new MessageResponse("Create succesfully"), HttpStatus.CREATED);
        } catch (ConstraintViolationException e){
                List<HttpResponse> errors = new ArrayList<>();
                Stream<ConstraintViolation<?>> violationStream = e.getConstraintViolations().stream();
                violationStream.forEach(violation -> {
                    errors.add(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, violation.getPropertyPath().toString(), violation.getMessage()));
                });
                return new ResponseEntity(errors.get(0), HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@RequestParam Long id){
        goodsReceiptNoteRepository.deleteById(id);
        return new ResponseEntity(new MessageResponse("Delete succesfully"),HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(@RequestParam(value = "id") Long id, @Valid @RequestBody UpdateGoodsReceiptNoteRequest updateGoodsReceiptNoteRequest) throws HttpMessageNotReadableException, GoodsReceiptNoteNotFoundException {
        Optional<GoodsReceiptNote> _goodsReceiptNote = goodsReceiptNoteRepository.findById(id);
        if(!_goodsReceiptNote.isPresent()){
            throw new GoodsReceiptNoteNotFoundException(id.toString());
        } else {
            GoodsReceiptNote goodsReceiptNote = _goodsReceiptNote.get();
            goodsReceiptNote.setFullname(updateGoodsReceiptNoteRequest.getFullname());
            goodsReceiptNote.setPhone(updateGoodsReceiptNoteRequest.getPhone());
            goodsReceiptNote.setCompanyName(updateGoodsReceiptNoteRequest.getCompanyName());
            goodsReceiptNoteRepository.save(goodsReceiptNote);
            return ResponseEntity.ok(new MessageResponse("Update succesfully"));
        }
    }
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getById(@RequestParam("id") Long id) throws GoodsReceiptNoteNotFoundException {
        Optional<GoodsReceiptNote> _goodsReceiptNote = goodsReceiptNoteRepository.findById(id);
        if(!_goodsReceiptNote.isPresent()){
            throw new GoodsReceiptNoteNotFoundException(id.toString());
        } else {
            return new ResponseEntity(modelMapperService.mapObject(_goodsReceiptNote.get(),customMapper),HttpStatus.OK);

        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<GoodsReceiptNote>> listAll(){
        try {
            List<GoodsReceiptNote> list = goodsReceiptNoteRepository.findAll();
            return new ResponseEntity(modelMapperService.mapList(list,customMapper),HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/list-by-username")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<GoodsReceiptNote>> listByUsername(@RequestParam(value = "username") String username) throws UserNotFoundException {
        try {
            User user = userRepository.findUserByUsername(username);
            if(user==null){
                throw new UserNotFoundException(username);
            }
            List<GoodsReceiptNote> list = goodsReceiptNoteRepository.findByUserId(user.getId());
            return new ResponseEntity(modelMapperService.mapList(list,customMapper),HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/list-by-current-user")
    public ResponseEntity<List<GoodsReceiptNoteResponse>> listByCurrentUser(Authentication authentication) throws UserNotFoundException {
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UserNotFoundException(username);
        } else {
            List<GoodsReceiptNote> list = goodsReceiptNoteRepository.findByUsername(username);
            return new ResponseEntity(modelMapperService.mapList(list,customMapper),HttpStatus.OK);
        }
    }

    @GetMapping("/get-by-serial-device")
    public ResponseEntity<GoodsReceiptNote> findBySerialDevice(@RequestParam String serial) throws DeviceNotFoundException, GoodsReceiptNoteNotFoundException {
        Optional<Device> device = deviceRepository.findDeviceBySerial(serial);
        if(!device.isPresent()){
            throw new DeviceNotFoundException(serial);
        }
        Long id = device.get().getGoodsReceiptNote().getId();
        Optional<GoodsReceiptNote> goodsReceiptNote = goodsReceiptNoteRepository.findById(id);
        if(!goodsReceiptNote.isPresent()){
            throw new GoodsReceiptNoteNotFoundException(id.toString());
        }
        return new ResponseEntity(modelMapperService.mapObject(goodsReceiptNote.get(),customMapper),HttpStatus.OK);

    }
    public CustomMapper<GoodsReceiptNote, GoodsReceiptNoteResponse> customMapper = goodsReceiptNote -> {
        GoodsReceiptNoteResponse goodsReceiptNoteResponse = mapper.map(goodsReceiptNote,GoodsReceiptNoteResponse.class);
        goodsReceiptNoteResponse.setExporter(goodsReceiptNote.getUser().getUsername());
        goodsReceiptNoteResponse.setExport_date(goodsReceiptNote.getDate());
        goodsReceiptNoteResponse.setDevices(customMapperService.mapListDevice(goodsReceiptNote.getDevices()));
        return goodsReceiptNoteResponse;
    };
}
