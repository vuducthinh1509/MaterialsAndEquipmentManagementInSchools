package com.javaspringboot.DevicesManagementSystemBackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PasswordDTO {

    private  String token;

    private String newPassword;
}
