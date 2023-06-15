package com.javaspringboot.DevicesManagementSystemBackend.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WarrantyCardRequest {
    @NotBlank
    private String serial;
    private String note;
}
