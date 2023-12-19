package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.EConfirmStatus;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response.DeviceResponse;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class WarrantyCardDTO {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Instant createAt;

    private String status;

    private EConfirmStatus confirmStatus;

    // ngày bàn giao
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date handoverDate;

    private String price;

    private String note;

    private String exporter;

    private String receiver;

    private String confirmer;

    private DeviceResponse device;
}
