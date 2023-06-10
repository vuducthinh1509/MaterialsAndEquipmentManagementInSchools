package com.javaspringboot.DevicesManagementSystemBackend.payload.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateDeviceRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String serial;
    @Min(value = 0L, message = "must be positive")
    private Integer price;
    @Min(value = 0L, message = "must be positive")
    private Long warrantyTime;
    @Min(value = 0L, message = "must be positive")
    private Long maintenanceTime;
    private Long categoryId;
}
