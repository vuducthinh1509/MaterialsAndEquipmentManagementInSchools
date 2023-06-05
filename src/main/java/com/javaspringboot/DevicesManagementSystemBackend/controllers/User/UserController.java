package com.javaspringboot.DevicesManagementSystemBackend.controllers.User;

import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.UserNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.models.User;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController extends ExceptionHandling {

    @Autowired
    UserRepository userRepository;

    //update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id,
                                        @RequestBody User user) throws UserNotFoundException {
        Optional<User> _user = userRepository.findById(id);
        if(!_user.isPresent()){
            throw new UserNotFoundException("User have not founded");
        } else {
            User updatedUser = _user.get();
            updatedUser.setBirthDate(user.getBirthDate());
            updatedUser.setFullname(user.getFullname());
            updatedUser.setPhone(user.getPhone());
            updatedUser.setTenVien(user.getTenVien());
            updatedUser.setTenPhong(user.getTenPhong());
            updatedUser.setTenBan(user.getTenPhong());
            userRepository.save(updatedUser);
            return ResponseEntity.ok(new MessageResponse("Cập nhật thành công"));
        }
    }

    //delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id")Long id) throws UserNotFoundException{
        Optional<User> _user = userRepository.findById(id);
        if(!_user.isPresent()){
            throw new UserNotFoundException("User have not founded");
        } else {
            userRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Delete succesfully"));
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, OK);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> findUserByUsername(@PathVariable("username")String username) throws UserNotFoundException{
        User _user = userRepository.findUserByUsername(username);
        if(_user!=null){
            return ResponseEntity.ok(_user);
        } else {
            throw new UserNotFoundException("User have not founded");
        }
    }
}
