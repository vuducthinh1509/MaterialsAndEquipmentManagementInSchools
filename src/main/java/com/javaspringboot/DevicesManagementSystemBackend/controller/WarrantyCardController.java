package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.advice.CustomMapper;
import com.javaspringboot.DevicesManagementSystemBackend.advice.HttpResponse;
import com.javaspringboot.DevicesManagementSystemBackend.dto.WarrantyCardDTO;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EConfirmStatus;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusWarranty;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.DeviceNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.UserNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.WarrantyCardNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.model.WarrantyCard;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.WarrantyCardRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.DeviceRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.WarrantyCardRepository;
import com.javaspringboot.DevicesManagementSystemBackend.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/warrantycard")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class WarrantyCardController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private WarrantyCardRepository warrantyCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping("/require")
    public ResponseEntity<?> claimWarrantyCard(@Valid @RequestBody WarrantyCardRequest warrantyCardRequest,Authentication authentication) throws UserNotFoundException,DeviceNotFoundException {
        User receiver = userRepository.findUserByUsername(authentication.getName());
        if(receiver==null){
            throw new UserNotFoundException(authentication.getName());
        }
        Optional<Device> deviceOpt = deviceRepository.findDeviceBySerial(warrantyCardRequest.getSerial());
        if(!deviceOpt.isPresent()){
            throw new DeviceNotFoundException(warrantyCardRequest.getSerial());
        }
        Device device = deviceOpt.get();
        if(device.getOutgoingGoodsNote()==null){
            return new ResponseEntity<>(new HttpResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,"BAD_REQUEST","Device has not been exported"),HttpStatus.BAD_REQUEST);
        }
        WarrantyCard warrantyCard = new WarrantyCard(warrantyCardRequest.getNote(),receiver,device);
        warrantyCardRepository.save(warrantyCard);
        return new ResponseEntity(new MessageResponse("Warranty claim successful"), HttpStatus.OK);
    }


    @GetMapping("/confirm")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> confirmWarrantyCard(@Valid @RequestParam Long id, Authentication authentication) throws WarrantyCardNotFoundException {
        Optional<WarrantyCard> warrantyCard = warrantyCardRepository.findById(id);
        if(!warrantyCard.isPresent()){
            throw new WarrantyCardNotFoundException(id.toString());
        }
        WarrantyCard card = warrantyCard.get();
        User user = userRepository.findUserByUsername(authentication.getName());
        Device device = card.getDevice();
        if(device.getWarrantyStatus()==EStatusWarranty.DANG_BAO_HANH){
            return new ResponseEntity<>(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,"Device","The device already exists in a warranty card"),HttpStatus.BAD_REQUEST);
        }
        device.setWarrantyStatus(EStatusWarranty.DANG_BAO_HANH);
        card.setConfirmer(user);
        card.setStatus(EStatusWarranty.DANG_BAO_HANH);
        card.setConfirmStatus(EConfirmStatus.DA_XAC_NHAN);
        warrantyCardRepository.save(card);
        return new ResponseEntity(new MessageResponse("Confirm successfully"), HttpStatus.OK);
    }

    @GetMapping("/deny")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deniedWarrantyCard(@Valid @RequestParam Long id, Authentication authentication) throws WarrantyCardNotFoundException {

        Optional<WarrantyCard> warrantyCard = warrantyCardRepository.findById(id);
        if(!warrantyCard.isPresent()){
            throw new WarrantyCardNotFoundException(id.toString());
        }
        WarrantyCard card = warrantyCard.get();
        User user = userRepository.findUserByUsername(authentication.getName());
        card.setConfirmer(user);
        card.setConfirmStatus(EConfirmStatus.TU_CHOI);
        warrantyCardRepository.save(card);
        return new ResponseEntity(new MessageResponse("Denied successfully"), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<WarrantyCard>> getAllWarrantyCard(){
        List<WarrantyCard> warrantyCardList = warrantyCardRepository.findAll();
        return new ResponseEntity(modelMapperService.mapList(warrantyCardList,customMapper),HttpStatus.OK);
    }

    // Tìm theo id
    @GetMapping("")
    public ResponseEntity<?> getWarrantyCardById(@Valid @RequestParam Long id){
        Optional<WarrantyCard> warrantyCard = warrantyCardRepository.findById(id);
        return new ResponseEntity<>(modelMapperService.mapObject(warrantyCard.get(),customMapper),HttpStatus.OK);
    }

    // Xóa theo id
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteWarrantyCard(@Valid @RequestParam Long id) throws WarrantyCardNotFoundException{
        warrantyCardRepository.deleteById(id);
        return new ResponseEntity(new MessageResponse("Delete succesfully"),HttpStatus.OK);
    }

    @GetMapping("/list_by_serial")
    public ResponseEntity<?> getWarrantyCardBySerialDevice(@Valid @RequestParam String serial) throws DeviceNotFoundException {
        Optional<Device> device = deviceRepository.findDeviceBySerial(serial);
        if(!device.isPresent()){
            throw new DeviceNotFoundException(serial);
        }
        List<WarrantyCard> warrantyCardList = warrantyCardRepository.findWarrantyCardByDeviceId(device.get().getId());
        return new ResponseEntity<>(modelMapperService.mapList(warrantyCardList,customMapper),HttpStatus.OK);
    }

    @GetMapping("/list_by_receiver")
    public ResponseEntity<?> getWarrantyCardByReceiver(@Valid @RequestParam String username) throws UserNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UserNotFoundException(username);
        }
        List<WarrantyCard> warrantyCardList = warrantyCardRepository.findWarrantyCardByReceiverId(user.getId());
        return new ResponseEntity<>(modelMapperService.mapList(warrantyCardList,customMapper),HttpStatus.OK);
    }
    public CustomMapper<WarrantyCard, WarrantyCardDTO> customMapper = warrantyCard -> {
        WarrantyCardDTO warrantyCardDTO = mapper.map(warrantyCard,WarrantyCardDTO.class);
        User user1 = warrantyCard.getConfirmer();
        User user2 = warrantyCard.getExporter();
        User user3 = warrantyCard.getReceiver();
        warrantyCardDTO.setConfirmer(user1==null?null:user1.getUsername());
        warrantyCardDTO.setExporter(user2==null?null:user2.getUsername());
        warrantyCardDTO.setReceiver(user3==null?null:user3.getUsername());
        return warrantyCardDTO;
    };

    @PostMapping("/transfer")
    public ResponseEntity<?> transferWarrantyCard(@Valid @RequestParam String price,@RequestParam Long id,Authentication authentication) throws UserNotFoundException {
        User user = userRepository.findUserByUsername(authentication.getName());
        if(user==null){
            throw new UserNotFoundException(authentication.getName());
        }
        Optional<WarrantyCard> warrantyCardOpt = warrantyCardRepository.findById(id);
        WarrantyCard warrantyCard = warrantyCardOpt.get();
        warrantyCard.setExporter(user);
        warrantyCard.setPrice(price);
        warrantyCard.setHandoverDate(new Date());
        warrantyCard.setStatus(EStatusWarranty.DA_BAN_GIAO);
        Device device = warrantyCard.getDevice();
        device.setWarrantyStatus(EStatusWarranty.DA_BAN_GIAO);
        warrantyCardRepository.save(warrantyCard);
        return new ResponseEntity<>(new MessageResponse("Transfer succesfully"),HttpStatus.OK);
    }
}
