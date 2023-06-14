package com.javaspringboot.DevicesManagementSystemBackend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.ERole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Setter
@Getter
public class UserResponse {

    private String username;

    private String email;


    private String fullname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private String phone;

    private Date joinDate;

    private String tenVien;

    private String tenPhong;

    private String tenBan;

    private List<ERole> roles;
}
