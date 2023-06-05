package com.javaspringboot.DevicesManagementSystemBackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "devices",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "serial"),
        })
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "device_id")
    private Long id;

    private String name;

    private String serial;

    private Integer price;

    private Long warrantyTime;

    private Long maintenanceTime;

    private String status;

    private String warrantyStatus;

    private String maintenanceStatus;

    @OneToMany(mappedBy = "device")
    private Set<WarrantyCard> warrantyCards;

    @ManyToMany
    @JoinTable(name = "device_category",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;
}
