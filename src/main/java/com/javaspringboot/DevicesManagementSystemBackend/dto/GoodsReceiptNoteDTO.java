package com.javaspringboot.DevicesManagementSystemBackend.dto;

import com.javaspringboot.DevicesManagementSystemBackend.models.Device;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
public class GoodsReceiptNoteDTO {

    @NotBlank
    private String fullname;
    @NotBlank
    private String phone;
    @NotBlank
    private String companyName;

    private Set<DeviceDTO> devices;
    @NotBlank
    private String username;
}
