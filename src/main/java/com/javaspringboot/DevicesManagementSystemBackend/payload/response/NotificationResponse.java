package com.javaspringboot.DevicesManagementSystemBackend.payload.response;

import com.javaspringboot.DevicesManagementSystemBackend.enumm.ETypeNotification;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
