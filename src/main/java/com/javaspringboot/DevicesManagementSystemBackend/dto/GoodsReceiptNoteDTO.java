package com.javaspringboot.DevicesManagementSystemBackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
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
}
