package com.javaspringboot.DevicesManagementSystemBackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "devices",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "serial"),
        })
@Getter
@Setter
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

    private String status;

    private String warrantyStatus;

    private String maintenanceStatus;

    @OneToMany(mappedBy = "device")
    private Set<WarrantyCard> warrantyCards;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="goodsReceiptNote_id",nullable = false)
    @JsonBackReference
    private GoodsReceiptNote goodsReceiptNote;

    @ManyToOne
    @JoinColumn(name="ongoingGoodsNote_id")
    @JsonBackReference
    private OutgoingGoodsNote outgoingGoodsNote;

    @PreRemove
    private void preRemove() {
        outgoingGoodsNote = null;
        status="Trong kho";
    }

    public Device(){

    }

    public Device(String name,String serial,Integer price,Long warrantyTime,Long maintenanceTime){
        this.status = "Trong kho";
        this.name = name;
        this.serial = serial;
        this.price = price;
        this.warrantyTime = warrantyTime;
        this.maintenanceTime = maintenanceTime;
    }
}
