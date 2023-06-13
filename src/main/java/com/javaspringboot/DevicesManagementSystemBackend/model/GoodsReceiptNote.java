package com.javaspringboot.DevicesManagementSystemBackend.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Set;

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
    @NotEmpty
    private String fullname;
    @NotBlank
    @NotEmpty
    private String phone;
    @NotBlank
    @NotEmpty
    private String companyName;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "goodsReceiptNote",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Device> devices;
    public GoodsReceiptNote(String fullname, String phone, String companyName){
        this.fullname = fullname;
        this.phone = phone;
        this.companyName =companyName;
        this.date = new Date();
    }

}
