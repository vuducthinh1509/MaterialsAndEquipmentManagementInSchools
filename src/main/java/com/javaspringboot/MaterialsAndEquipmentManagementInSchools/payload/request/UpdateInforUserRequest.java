package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@Getter
@Setter
public class UpdateInforUserRequest {
    @Email(message = "Email is not null")
    @Size(max = 50)
    private String email;

    @NotBlank
    private String fullname;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date birthDate;
    @NotEmpty
    private String phone;
    @NotBlank
    private String tenVien;

    private String tenPhong;

    private String tenBan;
}
