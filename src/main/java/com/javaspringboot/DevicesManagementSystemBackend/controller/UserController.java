package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.advice.CustomMapper;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.ERole;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.EmailExistException;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.UserNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.model.Role;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.payload.request.UpdateInforUserRequest;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.UserResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class UserController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapperService mapperService;

    @PutMapping("/update")
    public ResponseEntity<?> updateByUsername(@RequestParam("username") String username,@Valid @RequestBody UpdateInforUserRequest infoUpdate) throws UserNotFoundException,EmailExistException {
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            throw new UserNotFoundException(username);
        } else {
            if(!user.getEmail().equals(infoUpdate.getEmail())){
                user.setEmail(infoUpdate.getEmail());
                if(userRepository.existsByEmail(infoUpdate.getEmail())){
                    throw new EmailExistException("");
                }
            }
            user.setBirthDate(infoUpdate.getBirthDate());
            user.setFullname(infoUpdate.getFullname());
            user.setPhone(infoUpdate.getPhone());
            user.setTenVien(infoUpdate.getTenVien());
            user.setTenPhong(infoUpdate.getTenPhong());
            user.setTenBan(infoUpdate.getTenBan());
            userRepository.save(user);
            return new ResponseEntity(new MessageResponse("Update succesfully"), OK);
        }
    }

    //delete user
    @GetMapping("/disable")
    public ResponseEntity<?> disableByUsername(@RequestParam String username) throws UserNotFoundException{
            User user = userRepository.findUserByUsername(username);
            if(user==null){
                throw new UserNotFoundException(username);
            }
            user.setDisable();
            userRepository.save(user);
            if(user.isEnabled()){
                return new  ResponseEntity(new MessageResponse("Undisable succesfully"), OK);
            } else {
                return new  ResponseEntity(new MessageResponse("Disable succesfully"), OK);
            }



    }

    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> getAll() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(mapperService.mapList(users,customMapper), OK);
    }

    @GetMapping("")
    public ResponseEntity<?> findByUsername(@RequestParam String username) throws UserNotFoundException{
        User _user = userRepository.findUserByUsername(username);
        if(_user!=null){
            return ResponseEntity.ok(mapperService.mapObject(_user,customMapper));
        } else {
            throw new UserNotFoundException(username);
        }
    }

    @GetMapping("/find")
    public ResponseEntity<List<UserResponse>> findByName(@RequestParam String name){
        List<User> users = userRepository.findUserLikeName(name);
        return new ResponseEntity<>(mapperService.mapList(users,customMapper), OK);
    }

    public CustomMapper<User, UserResponse> customMapper = user -> {
        UserResponse userResponse = mapper.map(user,UserResponse.class);
        Set<Role> setRoles = user.getRoles();
        List<ERole> listRoles =  setRoles.stream().map(role -> role.getName()).collect(Collectors.toList());
        userResponse.setRoles(listRoles);
        return userResponse;
    };


}
