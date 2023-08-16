package com.javaspringboot.DevicesManagementSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusDevice;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusMaintenance;
import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusWarranty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "devices",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "serial"),
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "device_id")
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String serial;
    @Min(value = 0L, message = "must be positive")
    private Integer price;
    @Min(value = 0L, message = "must be positive")
    private Long warrantyTime;
    @Min(value = 0L, message = "must be positive")
    private Long maintenanceTime;

    @Enumerated(EnumType.STRING)
    private EStatusDevice status;
    @Enumerated(EnumType.STRING)
    private EStatusWarranty warrantyStatus;
    @Enumerated(EnumType.STRING)
    private EStatusMaintenance maintenanceStatus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="goodsReceiptNote_id",nullable = false)
    @JsonBackReference
    private GoodsReceiptNote goodsReceiptNote;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="outgoingGoodsNote_id")
    @JsonBackReference
    private OutgoingGoodsNote outgoingGoodsNote;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "device")
    @JsonBackReference
    private Set<WarrantyCard> warrantyCards;

    public Device(String name,String serial,Integer price,Long warrantyTime,Long maintenanceTime){
        this.status = EStatusDevice.TRONG_KHO;
        this.name = name;
        this.serial = serial;
        this.price = price;
        this.warrantyTime = warrantyTime;
        this.maintenanceTime = maintenanceTime;
    }

    public Device removeOutgoingGoodsNote(){
        this.setOutgoingGoodsNote(null);
        this.setMaintenanceStatus(null);
        this.setWarrantyStatus(null);
        this.setStatus(EStatusDevice.TRONG_KHO);
        return this;
    }
}
