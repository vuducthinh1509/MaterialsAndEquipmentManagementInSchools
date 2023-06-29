package com.javaspringboot.DevicesManagementSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EConfirmStatus;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusWarranty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "warrantyCard")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WarrantyCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant createAt;

    @Enumerated(EnumType.STRING)
    private EStatusWarranty status;

    @Enumerated(EnumType.STRING)
    private EConfirmStatus confirmStatus;

    // ngày bàn giao
    private Date handoverDate;

    private String price;

    private String note;
    @ManyToOne
    @JoinColumn(name="id_exporter",referencedColumnName = "id")
    private User exporter;

    @ManyToOne
    @JoinColumn(name="id_receiver", nullable=false,referencedColumnName = "id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name="id_confirmer",referencedColumnName = "id")
    private User confirmer;
    @ManyToOne
    @JoinColumn(name="device_id", nullable=false,referencedColumnName = "device_id")
    @JsonManagedReference
    private Device device;

    public WarrantyCard(String note, User receiver, Device device) {
        this.note = note;
        this.receiver = receiver;
        this.device = device;
        this.confirmStatus = EConfirmStatus.CHUA_XAC_NHAN;
        this.createAt = Instant.now();
    }
}
