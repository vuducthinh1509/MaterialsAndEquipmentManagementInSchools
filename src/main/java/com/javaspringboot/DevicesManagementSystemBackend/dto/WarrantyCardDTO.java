package com.javaspringboot.DevicesManagementSystemBackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EConfirmStatus;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.DeviceResponse;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
