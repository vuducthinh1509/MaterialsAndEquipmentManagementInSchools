package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutgoingGoodsNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @NotNull
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "id_exporter", referencedColumnName = "id")
    @NotNull
    @JsonManagedReference
    private User exporter;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_receiver", referencedColumnName = "id")
    @JsonManagedReference
    private User receiver;

    @OneToMany(mappedBy = "outgoingGoodsNote",fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Device> devices;


    public OutgoingGoodsNote(User exporter, User receiver){
        this.exporter=exporter;
        this.receiver = receiver;
        this.createdAt = Instant.now();
    }
}
