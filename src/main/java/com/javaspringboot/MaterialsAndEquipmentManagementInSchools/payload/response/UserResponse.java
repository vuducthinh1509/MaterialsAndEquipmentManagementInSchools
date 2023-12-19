package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.ERole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
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
    private Instant joinDate;

    private String tenVien;

    private String tenPhong;

    private String tenBan;

    private boolean isEnabled;

    private List<ERole> roles;
}
