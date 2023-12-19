package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.ETypeNotification;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
public class NotificationResponse {
    private Long id;

    @NotBlank
    private String message;

    private String user;

    private ETypeNotification type;

    private Instant createdAt;
    // default false
    private boolean read;
}
