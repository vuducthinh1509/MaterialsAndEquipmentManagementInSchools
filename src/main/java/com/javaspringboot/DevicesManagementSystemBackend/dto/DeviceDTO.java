package com.javaspringboot.DevicesManagementSystemBackend.dto;

import lombok.Data;

@Data
public class DeviceDTO {
    private String name;
    private String serial;
    private Integer price;
    private Long warrantyTime;
    private Long maintenanceTime;
    private String categoryName;
    private String categoryDescription;
}
