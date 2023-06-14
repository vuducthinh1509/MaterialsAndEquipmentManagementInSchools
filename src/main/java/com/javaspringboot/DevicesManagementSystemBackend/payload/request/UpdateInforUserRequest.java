package com.javaspringboot.DevicesManagementSystemBackend.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
public class UpdateInforUserRequest {
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    private String fullname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    @NotEmpty
    private String phone;
    @NotBlank
    private String tenVien;

    private String tenPhong;

    private String tenBan;
}
