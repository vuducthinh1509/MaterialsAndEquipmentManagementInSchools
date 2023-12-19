package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.dto.CategoryDTO;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.EStatusDevice;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.EStatusMaintenance;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.EStatusWarranty;
import lombok.Data;

@Data
public class DeviceResponse {
    private Long id;

    private String name;
    private String serial;
    private Integer price;
    private Long warrantyTime;
    private Long maintenanceTime;

    private EStatusDevice status;

    private EStatusWarranty warrantyStatus;

    private EStatusMaintenance maintenanceStatus;

    private CategoryDTO category;
}
