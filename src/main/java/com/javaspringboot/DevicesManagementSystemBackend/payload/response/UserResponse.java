package com.javaspringboot.DevicesManagementSystemBackend.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.ERole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
public class UserResponse {

    private String username;

    private String email;


    private String fullname;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthDate;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;

    private String tenVien;

    private String tenPhong;

    private String tenBan;

    private List<ERole> roles;
}
