package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.advice.CustomMapper;
import com.javaspringboot.DevicesManagementSystemBackend.model.HttpResponse;
import com.javaspringboot.DevicesManagementSystemBackend.dto.OutgoingGoodsNoteDTO;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusDevice;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.DeviceNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.GoodsReceiptNoteNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.OutgoingGoodsNoteNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.UserNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.model.GoodsReceiptNote;
import com.javaspringboot.DevicesManagementSystemBackend.model.OutgoingGoodsNote;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.OutgoingGoodsNoteResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.DeviceRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.OutgoingGoodsNoteRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.service.CustomMapperService;
import com.javaspringboot.DevicesManagementSystemBackend.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/phieuxuat")
@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
public class OutgoingGoodsNoteController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private CustomMapperService customMapperService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private OutgoingGoodsNoteRepository outgoingGoodsNoteRepository;

    public OutgoingGoodsNoteController(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @PostMapping("/add")
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody OutgoingGoodsNoteDTO outgoingGoodsNoteDTO, Authentication authentication) throws UserNotFoundException, DeviceNotFoundException {
        User exporter = userRepository.findUserByUsername(authentication.getName());
        if(exporter==null){
            return new ResponseEntity<>(new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,"","Action failed! Try again"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User receiver = userRepository.findUserByUsername(outgoingGoodsNoteDTO.getReceiver());
        if(receiver==null){
            throw new UserNotFoundException(outgoingGoodsNoteDTO.getReceiver());
        }
        Set<String> strSerial = outgoingGoodsNoteDTO.getDevices();
        Set<Device> devices = new HashSet<>();
        if(strSerial.isEmpty()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"","devices can not be empty"));
        }
        OutgoingGoodsNote outgoingGoodsNote = new OutgoingGoodsNote(exporter,receiver);
        for(String serial : strSerial){
            Optional<Device> device = deviceRepository.findDeviceBySerial(serial);
            if(!device.isPresent()){
                throw new DeviceNotFoundException(serial);
            } else {
                Device _temp = device.get();
                if(_temp.getOutgoingGoodsNote()==null){
                    _temp.setStatus(EStatusDevice.DA_XUAT);
                    _temp.setOutgoingGoodsNote(outgoingGoodsNote);
                    devices.add(_temp);
                } else {
                    return new ResponseEntity(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"",String.format("Device %s has been exported",serial)),HttpStatus.BAD_REQUEST);
                }
            }
        }
        outgoingGoodsNote.setDevices(devices);
        outgoingGoodsNoteRepository.save(outgoingGoodsNote);
        return new ResponseEntity(new MessageResponse("Add succesfully"),HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@RequestParam Long id) throws OutgoingGoodsNoteNotFoundException {
        Optional<OutgoingGoodsNote> outgoingGoodsNote = outgoingGoodsNoteRepository.findById(id);
        if(!outgoingGoodsNote.isPresent()){
            throw new OutgoingGoodsNoteNotFoundException(id.toString());
        }
        OutgoingGoodsNote note = outgoingGoodsNote.get();
        Set<Device> devices = note.getDevices();
        List<Device> deviceList = devices.stream().map(Device::removeOutgoingGoodsNote).collect(Collectors.toList());
        deviceRepository.saveAll(deviceList);
        note.setDevices(null);
        outgoingGoodsNoteRepository.delete(note);
        return new ResponseEntity(new MessageResponse("Delete succesfully1"),HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<OutgoingGoodsNote>> findAll(){
        List<OutgoingGoodsNote> outgoingGoodsNotes = outgoingGoodsNoteRepository.findAll();
        return new ResponseEntity(modelMapperService.mapList(outgoingGoodsNotes,customMapper),HttpStatus.OK);
    }
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> findById(@RequestParam Long id){
        Optional<OutgoingGoodsNote> outgoingGoodsNote = outgoingGoodsNoteRepository.findById(id);
        return new ResponseEntity(modelMapperService.mapObject(outgoingGoodsNote.get(),customMapper),HttpStatus.OK);

    }

    @GetMapping("/get-by-serial-device")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<GoodsReceiptNote> findBySerialDevice(@RequestParam String serial) throws DeviceNotFoundException, GoodsReceiptNoteNotFoundException {
        Optional<Device> device = deviceRepository.findDeviceBySerial(serial);
        if(!device.isPresent()){
            throw new DeviceNotFoundException(serial);
        }
        Long id = device.get().getOutgoingGoodsNote().getId();
        Optional<OutgoingGoodsNote> outgoingGoodsNote = outgoingGoodsNoteRepository.findById(id);
        if(!outgoingGoodsNote.isPresent()){
            throw new GoodsReceiptNoteNotFoundException(id.toString());
        }
        return new ResponseEntity(modelMapperService.mapObject(outgoingGoodsNote.get(),customMapper),HttpStatus.OK);

    }

    @GetMapping("/list-by-current-user")
    public ResponseEntity<List<OutgoingGoodsNote>> listByCurrentUser(Authentication authentication) {
        List<OutgoingGoodsNote> list = outgoingGoodsNoteRepository.findByUsername(authentication.getName());
        return new ResponseEntity(modelMapperService.mapList(list,customMapper),HttpStatus.OK);
    }

    public CustomMapper<OutgoingGoodsNote, OutgoingGoodsNoteResponse> customMapper = outgoingGoodsNote -> {
        OutgoingGoodsNoteResponse outgoingGoodsNoteResponse = mapper.map(outgoingGoodsNote,OutgoingGoodsNoteResponse.class);
        outgoingGoodsNoteResponse.setExporter(outgoingGoodsNote.getExporter().getUsername());
        outgoingGoodsNoteResponse.setReceiver(outgoingGoodsNote.getReceiver().getUsername());
        outgoingGoodsNoteResponse.setDevices(customMapperService.mapSetDevice(outgoingGoodsNote.getDevices()));
        outgoingGoodsNoteResponse.setExportDate(outgoingGoodsNote.getCreatedAt());
        return outgoingGoodsNoteResponse;
    };

//    public Device setNoOutgoingGoodsNote(Device device){
//        device.setOutgoingGoodsNote(null);
//        device.setMaintenanceStatus(null);
//        device.setWarrantyStatus(null);
//        device.setStatus(EStatusDevice.TRONG_KHO);
//        return device;
//    }
}
