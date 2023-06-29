package com.javaspringboot.DevicesManagementSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GoodsReceiptNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String fullname;
    @NotBlank
    private String phone;
    @NotBlank
    private String companyName;

    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "goodsReceiptNote", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Device> devices;

    public GoodsReceiptNote(String fullname, String phone, String companyName) {
        this.fullname = fullname;
        this.phone = phone;
        this.companyName = companyName;
        this.createdAt = Instant.now();
    }

}
