package com.javaspringboot.DevicesManagementSystemBackend.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "warrantyCard")
public class WarrantyCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", nullable=false,referencedColumnName = "id")
    private User user1;

    private Date date;

    private String status;

    // ngày bàn giao
    private Date handoverDate;

    private String price;

    @ManyToOne
    @JoinColumn(name="nguoixuat_id", nullable=false,referencedColumnName = "id")
    private User user2;

    @ManyToOne
    @JoinColumn(name="device_id", nullable=false, referencedColumnName = "device_id")
    private Device device;
}
