package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.controller;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.advice.CustomMapper;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.ETypeNotification;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.exception.domain.NotificationNotFoundException;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.exception.domain.UserNotFoundException;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.Notification;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.User;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response.MessageResponse;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response.NotificationResponse;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.NotificationRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.UserRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapperService modelMapperService;
    //user get notification
    @GetMapping("/get-notification")
    public ResponseEntity<?> getAllNotificationFromUser(Authentication authentication) throws UserNotFoundException{
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UserNotFoundException(username);
        }
        List<Notification> notifications = notificationRepository.getAllNotificationFromUser(user.getId());
        return new ResponseEntity(modelMapperService.mapList(notifications,customMapper), OK);
    }

    @GetMapping("/get-all-notification")
    public ResponseEntity<?> getAllNotification(Authentication authentication) throws UserNotFoundException{
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UserNotFoundException(username);
        }
        List<Notification> notifications = notificationRepository.getAllByUserId(user.getId());
        return new ResponseEntity(modelMapperService.mapList(notifications,customMapper), OK);
    }

    // user read notification
    @GetMapping("/read")
    public ResponseEntity<?> readNotification(@RequestParam Long id,Authentication authentication) throws UserNotFoundException, NotificationNotFoundException {
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UserNotFoundException(username);
        }
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if(!notificationOpt.isPresent()){
            throw new NotificationNotFoundException(id.toString());
        }
        Notification notification = notificationOpt.get();
        Long userId = notification.getUser().getId();
        if(userId == user.getId() && notification.getType().equals(ETypeNotification.ADMIN_TO_SPECIFIC)){
            notification.setRead(true);
            notificationRepository.save(notification);
            return ResponseEntity.ok(new MessageResponse("Action succesfully"));
        }
        return ResponseEntity.ok(new MessageResponse("Action failed"));
    }



    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/get-all-notification")
    public ResponseEntity<?> getAllNotificationFromAdmin(){
        List<Notification> notifications = notificationRepository.getAllNotificationByAdmin();
        return new ResponseEntity(modelMapperService.mapList(notifications,customMapper), OK);
    }

    @GetMapping("/admin/read")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> readNotificationFromAdmin(@RequestParam Long id) throws NotificationNotFoundException {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if(!notificationOpt.isPresent()){
            throw new NotificationNotFoundException(id.toString());
        }
        Notification notification = notificationOpt.get();
        if(notification.getType() == ETypeNotification.USER_TO_ADMIN){
            notification.setRead(true);
            notificationRepository.save(notification);
            return ResponseEntity.ok(new MessageResponse("Action succesfully"));
        }
        return ResponseEntity.ok(new MessageResponse("Action failed"));

    }

    public CustomMapper<Notification, NotificationResponse> customMapper = notification -> {
        NotificationResponse notificationResponse = mapper.map(notification,NotificationResponse.class);
        notificationResponse.setUser(notification.getUser().getUsername());
        return notificationResponse;
    };
}
