package com.javaspringboot.DevicesManagementSystemBackend.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EConfirmStatus;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
public class WarrantyCardDTO {
    private Long id;

    private Date date;

    private String status;

    private EConfirmStatus confirmStatus;

    // ngày bàn giao
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date handoverDate;

    private String price;

    private String note;

    private String exporter;

    private String receiver;

    private String confirmer;

    private Device device;
}
