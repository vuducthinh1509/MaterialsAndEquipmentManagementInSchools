package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.advice.CustomMapper;
import com.javaspringboot.DevicesManagementSystemBackend.advice.HttpResponse;
import com.javaspringboot.DevicesManagementSystemBackend.service.ModelMapperService;
import com.javaspringboot.DevicesManagementSystemBackend.dto.OutgoingGoodsNoteDTO;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.DeviceNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.UserNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.model.OutgoingGoodsNote;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.OutgoingGoodsNoteResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.DeviceRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.OutgoingGoodsNoteRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/phieuxuat")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OutgoingGoodsNoteController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ModelMapperService modelMapperService;


    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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
    public ResponseEntity<?> createPhieuXuat(@Valid @RequestBody OutgoingGoodsNoteDTO outgoingGoodsNoteDTO, Authentication authentication) throws UserNotFoundException, DeviceNotFoundException {
        User exporter = userRepository.findUserByUsername(authentication.getName());
        if(exporter==null){
            return new ResponseEntity<>(new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,"","Action failed! Try again"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> receiver = userRepository.findById(outgoingGoodsNoteDTO.getReceiverId());
        if(!receiver.isPresent()){
            throw new UserNotFoundException("No receiver found with id " + outgoingGoodsNoteDTO.getReceiverId());
        }
        Set<String> strSerial = outgoingGoodsNoteDTO.getDevices();
        Set<Device> devices = new HashSet<>();
        if(strSerial.isEmpty()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"","devices can not be empty"));
        }
        OutgoingGoodsNote outgoingGoodsNote = new OutgoingGoodsNote(exporter,receiver.get());
        for(String serial : strSerial){
            Optional<Device> device = deviceRepository.findDeviceBySerial(serial);
            if(!device.isPresent()){
                throw new DeviceNotFoundException(serial);
            } else {
                Device _temp = device.get();
                if(_temp.getOutgoingGoodsNote()==null){
                    _temp.setStatus("Đã xuất");
                    _temp.setOutgoingGoodsNote(outgoingGoodsNote);
                    devices.add(_temp);
                } else {
                    return new ResponseEntity(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"",String.format("Device %s has been exported",serial)),HttpStatus.BAD_REQUEST);
                }

            }
        }
        outgoingGoodsNoteRepository.save(outgoingGoodsNote);
        return new ResponseEntity(new MessageResponse("Add succesfully"), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteById(@RequestParam Long id){
        outgoingGoodsNoteRepository.deleteById(id);
        return new ResponseEntity<>(new MessageResponse("Delete succesfully"),HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<OutgoingGoodsNote>> findAll(){
        List<OutgoingGoodsNote> outgoingGoodsNotes = outgoingGoodsNoteRepository.findAll();
        return new ResponseEntity(modelMapperService.mapList(outgoingGoodsNotes,customMapper),HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> findById(@RequestParam Long id){
        Optional<OutgoingGoodsNote> outgoingGoodsNote = outgoingGoodsNoteRepository.findById(id);
        return new ResponseEntity(modelMapperService.mapObject(outgoingGoodsNote.get(),customMapper),HttpStatus.OK);
    }

    public CustomMapper<OutgoingGoodsNote, OutgoingGoodsNoteResponse> customMapper = outgoingGoodsNote -> {
        OutgoingGoodsNoteResponse outgoingGoodsNoteResponse = mapper.map(outgoingGoodsNote,OutgoingGoodsNoteResponse.class);
        outgoingGoodsNoteResponse.setExporter(outgoingGoodsNote.getExporter().getUsername());
        outgoingGoodsNoteResponse.setReceiver(outgoingGoodsNote.getReceiver().getUsername());
        return outgoingGoodsNoteResponse;
    };
}
