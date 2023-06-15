package com.javaspringboot.DevicesManagementSystemBackend.payload.response;

import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusDevice;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusMaintenance;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusWarranty;
import lombok.Data;

@Data
public class DeviceResponse {

    private String name;
    private String serial;
    private Integer price;
    private Long warrantyTime;
    private Long maintenanceTime;

    private EStatusDevice status;

    private EStatusWarranty warrantyStatus;

    private EStatusMaintenance maintenanceStatus;

    private String category;
}
