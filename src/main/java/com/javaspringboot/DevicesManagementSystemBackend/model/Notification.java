package com.javaspringboot.DevicesManagementSystemBackend.model;

import com.javaspringboot.DevicesManagementSystemBackend.enumm.ETypeNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String message;

    @Enumerated(EnumType.STRING)
    private ETypeNotification type;

    private Instant createdAt;
    // default false
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Notification(String message,ETypeNotification type,User user){
        this.message = message;
        this.type = type;
        this.user = user;
        this.createdAt = Instant.now();
        this.isRead = false;
    }
}
