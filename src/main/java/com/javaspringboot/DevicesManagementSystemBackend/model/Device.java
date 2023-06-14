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

    private EStatusDevice status;

    private EStatusWarranty warrantyStatus;

    private EStatusMaintenance maintenanceStatus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="goodsReceiptNote_id",nullable = false)
    @JsonBackReference
    private GoodsReceiptNote goodsReceiptNote;

    @ManyToOne
    @JoinColumn(name="outgoingGoodsNote_id")
    @JsonBackReference
    private OutgoingGoodsNote outgoingGoodsNote;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "device")
    @JsonBackReference
    private Set<WarrantyCard> warrantyCards;

    @PreRemove
    private void preRemove() {
        outgoingGoodsNote = null;
        status=EStatusDevice.TRONG_KHO;
    }

    public Device(String name,String serial,Integer price,Long warrantyTime,Long maintenanceTime){
        this.status = EStatusDevice.TRONG_KHO;
        this.name = name;
        this.serial = serial;
        this.price = price;
        this.warrantyTime = warrantyTime;
        this.maintenanceTime = maintenanceTime;
    }
}
