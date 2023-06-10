package com.javaspringboot.DevicesManagementSystemBackend.dto;

import com.javaspringboot.DevicesManagementSystemBackend.models.Category;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class DeviceDTO {
    private String name;
    private String serial;
    private Integer price;
    private Long warrantyTime;
    private Long maintenanceTime;
    private Long categoryId;
}
