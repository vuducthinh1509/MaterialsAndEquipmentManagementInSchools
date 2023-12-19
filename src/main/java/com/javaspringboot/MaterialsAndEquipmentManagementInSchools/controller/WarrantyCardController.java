package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.controller;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.advice.CustomMapper;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.*;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.dto.WarrantyCardDTO;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.EConfirmStatus;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.EStatusWarranty;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.exception.ExceptionHandling;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.exception.domain.DeviceNotFoundException;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.exception.domain.UserNotFoundException;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.exception.domain.WarrantyCardNotFoundException;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.request.WarrantyCardRequest;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response.MessageResponse;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.DeviceRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.NotificationRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.UserRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.WarrantyCardRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.service.CustomMapperService;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.ETypeNotification.ADMIN_TO_SPECIFIC;
import static com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.ETypeNotification.USER_TO_ADMIN;

@RequestMapping("/api/warrantycard")
@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RestController
public class WarrantyCardController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomMapperService customMapperService;

    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private WarrantyCardRepository warrantyCardRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping("/require")
    public ResponseEntity<?> require(@Valid @RequestBody WarrantyCardRequest warrantyCardRequest,Authentication authentication) throws UserNotFoundException,DeviceNotFoundException {
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
        String message = String.format("Yêu cầu bảo hành thiết bị %s từ người dùng %s",device.getSerial(),receiver.getUsername());
        Notification notification = new Notification(message,USER_TO_ADMIN,receiver);
        notificationRepository.save(notification);
        warrantyCardRepository.save(warrantyCard);
        return new ResponseEntity(new MessageResponse("Claim successful"), HttpStatus.CREATED);
    }


    @GetMapping("/confirm")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> confirm(@Valid @RequestParam Long id, Authentication authentication) throws WarrantyCardNotFoundException {
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
        String message = String.format("Phiếu bảo hành số %d của thiết bị %s được xác nhận",card.getId(),card.getDevice().getSerial());
        Notification notification = new Notification(message,ADMIN_TO_SPECIFIC,card.getReceiver());
        notificationRepository.save(notification);
        warrantyCardRepository.save(card);
        return new ResponseEntity(new MessageResponse("Confirm successfully"), HttpStatus.OK);
    }

    @GetMapping("/deny")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deny(@Valid @RequestParam Long id, Authentication authentication) throws WarrantyCardNotFoundException {

        Optional<WarrantyCard> warrantyCard = warrantyCardRepository.findById(id);
        if(!warrantyCard.isPresent()){
            throw new WarrantyCardNotFoundException(id.toString());
        }
        WarrantyCard card = warrantyCard.get();
        User user = userRepository.findUserByUsername(authentication.getName());
        card.setConfirmer(user);
        card.setConfirmStatus(EConfirmStatus.TU_CHOI);
        warrantyCardRepository.save(card);
        String message = String.format("Phiếu bảo hành số %d của thiết bị %s bị từ chối",card.getId(),card.getDevice().getSerial());
        Notification notification = new Notification(message,ADMIN_TO_SPECIFIC,card.getReceiver());
        notificationRepository.save(notification);
        return new ResponseEntity(new MessageResponse("Denied successfully"), HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<WarrantyCard>> getAll(){
        List<WarrantyCard> warrantyCardList = warrantyCardRepository.findAll();
        return new ResponseEntity(modelMapperService.mapList(warrantyCardList,customMapper),HttpStatus.OK);
    }

    @GetMapping("/list-unconfirm")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<WarrantyCard>> listUnconfirm(){
        List<WarrantyCard> warrantyCardList = warrantyCardRepository.listByStatusUnconfirm();
        return new ResponseEntity(modelMapperService.mapList(warrantyCardList,customMapper),HttpStatus.OK);
    }

    // Tìm theo id
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getById(@Valid @RequestParam Long id){
        Optional<WarrantyCard> warrantyCard = warrantyCardRepository.findById(id);
        return new ResponseEntity<>(modelMapperService.mapObject(warrantyCard.get(),customMapper),HttpStatus.OK);
    }

    // Xóa theo id
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@Valid @RequestParam Long id){
        Optional<WarrantyCard> warrantyCard = warrantyCardRepository.findById(id);
        WarrantyCard warrantyCard1 = warrantyCard.get();
        if(warrantyCard1.getConfirmStatus().equals(EConfirmStatus.CHUA_XAC_NHAN)){
            warrantyCardRepository.delete(warrantyCard1);
            return new ResponseEntity(new MessageResponse("Delete succesfully"),HttpStatus.OK);
        } else {
            return new ResponseEntity(new MessageResponse("Delete failed"),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list-by-serial-device")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getBySerialDevice(@Valid @RequestParam String serial) throws DeviceNotFoundException {
        Optional<Device> device = deviceRepository.findDeviceBySerial(serial);
        if(!device.isPresent()){
            throw new DeviceNotFoundException(serial);
        }
        List<WarrantyCard> warrantyCardList = warrantyCardRepository.findWarrantyCardByDeviceId(device.get().getId());
        return new ResponseEntity<>(modelMapperService.mapList(warrantyCardList,customMapper),HttpStatus.OK);
    }

    @GetMapping("/list-by-receiver")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getByReceiver(@Valid @RequestParam String username) throws UserNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UserNotFoundException(username);
        }
        List<WarrantyCard> warrantyCardList = warrantyCardRepository.findWarrantyCardByReceiverId(user.getId());
        return new ResponseEntity<>(modelMapperService.mapList(warrantyCardList,customMapper),HttpStatus.OK);
    }

    @GetMapping("/list-by-current-user")
    public ResponseEntity<?> getByReceiver(Authentication authentication) throws UserNotFoundException {
        User user = userRepository.findUserByUsername(authentication.getName());
        if(user==null){
            throw new UserNotFoundException(authentication.getName());
        }
        List<WarrantyCard> warrantyCardList = warrantyCardRepository.findWarrantyCardByReceiverId(user.getId());
        return new ResponseEntity<>(modelMapperService.mapList(warrantyCardList,customMapper),HttpStatus.OK);
    }

    @GetMapping("/transfer")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
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

    public CustomMapper<WarrantyCard, WarrantyCardDTO> customMapper = warrantyCard -> {
        WarrantyCardDTO warrantyCardDTO = mapper.map(warrantyCard,WarrantyCardDTO.class);
        User user1 = warrantyCard.getConfirmer();
        User user2 = warrantyCard.getExporter();
        User user3 = warrantyCard.getReceiver();
        warrantyCardDTO.setConfirmer(user1==null?null:user1.getUsername());
        warrantyCardDTO.setExporter(user2==null?null:user2.getUsername());
        warrantyCardDTO.setReceiver(user3==null?null:user3.getUsername());
        warrantyCardDTO.setDevice(customMapperService.mapDevice(warrantyCard.getDevice()));
        return warrantyCardDTO;
    };

}
