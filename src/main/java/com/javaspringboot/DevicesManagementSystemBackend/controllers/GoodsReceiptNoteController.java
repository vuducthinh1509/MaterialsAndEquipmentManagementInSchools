package com.javaspringboot.DevicesManagementSystemBackend.controllers;

import com.javaspringboot.DevicesManagementSystemBackend.advice.CustomMapper;
import com.javaspringboot.DevicesManagementSystemBackend.advice.HttpResponse;
import com.javaspringboot.DevicesManagementSystemBackend.dto.DeviceDTO;
import com.javaspringboot.DevicesManagementSystemBackend.dto.GoodsReceiptNoteDTO;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.models.*;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.UpdateGoodsReceiptNoteRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.GoodsReceiptNoteResponse;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.CategoryRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.DeviceRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.GoodsReceiptNoteRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/phieunhap")
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

    @PostMapping("/add")
    public ResponseEntity<?> createGoodsReceiptNote(@Valid @RequestBody GoodsReceiptNoteDTO goodsReceiptNoteDTO){
        if(!userRepository.existsByUsername(goodsReceiptNoteDTO.getUsername())){
                throw new EmptyResultDataAccessException("User does not exist", 1 );
        }
        GoodsReceiptNote goodsReceiptNote = new GoodsReceiptNote(goodsReceiptNoteDTO.getFullname(), goodsReceiptNoteDTO.getPhone(), goodsReceiptNoteDTO.getCompanyName());
        goodsReceiptNote.setUser(userRepository.findUserByUsername(goodsReceiptNoteDTO.getUsername()));
        Set<Device> devices = new HashSet<>();
        Set<DeviceDTO> devicesDTO = goodsReceiptNoteDTO.getDevices();
        if(devicesDTO.isEmpty()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"","devices can not be empty"));
        }
        for(DeviceDTO deviceDTO : devicesDTO){
            if(deviceRepository.findDeviceBySerial(deviceDTO.getSerial()).isPresent()){
                String message = String.format("Device '%s' already exists",deviceDTO.getSerial());
                return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"",message));
            } else {
                Device _temp = new Device(deviceDTO.getName(),deviceDTO.getSerial(),deviceDTO.getPrice(),deviceDTO.getWarrantyTime(),deviceDTO.getMaintenanceTime());
                Optional<Category> cate =  categoryRepository.findById(deviceDTO.getCategoryId());
                if(cate.isPresent()){
                    _temp.setCategory(cate.get());
                } else {
                    return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"","No category found with id: "+ deviceDTO.getCategoryId()));
                }
                _temp.setGoodsReceiptNote(goodsReceiptNote);
                devices.add(_temp);
            }
        }
        goodsReceiptNote.setDevices(devices);
        try {
            goodsReceiptNoteRepository.save(goodsReceiptNote);
            return ResponseEntity.ok(new MessageResponse("Create succesfully"));
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
    public ResponseEntity<?> deletePhieuNhap(@RequestParam Long id){
        goodsReceiptNoteRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Delete successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePhieuNhap(@PathVariable("id") Long id, @Valid @RequestBody UpdateGoodsReceiptNoteRequest updateGoodsReceiptNoteRequest) throws HttpMessageNotReadableException {
        Optional<GoodsReceiptNote> _goodsReceiptNote = goodsReceiptNoteRepository.findById(id);
        if(!_goodsReceiptNote.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"","No goods receipt note found with id: "+ id));
        } else {
            GoodsReceiptNote goodsReceiptNote = _goodsReceiptNote.get();
            goodsReceiptNote.setFullname(updateGoodsReceiptNoteRequest.getFullname());
            goodsReceiptNote.setPhone(updateGoodsReceiptNoteRequest.getPhone());
            goodsReceiptNote.setCompanyName(updateGoodsReceiptNoteRequest.getCompanyName());
            goodsReceiptNoteRepository.save(goodsReceiptNote);
            return ResponseEntity.ok(new MessageResponse("Update succesfully"));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPhieuNhapById(@PathVariable("id") Long id){
        Optional<GoodsReceiptNote> _goodsReceiptNote = goodsReceiptNoteRepository.findById(id);
        if(!_goodsReceiptNote.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"","No goods receipt note found with id: "+ id));
        } else {
            return new ResponseEntity(modelMapperService.mapObject(_goodsReceiptNote.get(),customMapper),HttpStatus.OK);

        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<GoodsReceiptNote>> getAllPhieuNhap(){
        try {
            List<GoodsReceiptNote> list = goodsReceiptNoteRepository.findAll();
            return new ResponseEntity(modelMapperService.mapList(list,customMapper),HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<GoodsReceiptNote>> getByUserId(@PathVariable Long id){
        try {
            List<GoodsReceiptNote> list = goodsReceiptNoteRepository.findByUserId(id);
            return new ResponseEntity(modelMapperService.mapList(list,customMapper),HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public CustomMapper<GoodsReceiptNote, GoodsReceiptNoteResponse> customMapper = goodsReceiptNote -> {
        GoodsReceiptNoteResponse goodsReceiptNoteResponse = mapper.map(goodsReceiptNote,GoodsReceiptNoteResponse.class);
        goodsReceiptNoteResponse.setUsername(goodsReceiptNote.getUser().getUsername());
        return goodsReceiptNoteResponse;
    };
}
