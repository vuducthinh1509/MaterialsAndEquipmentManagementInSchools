package com.javaspringboot.DevicesManagementSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
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

    private Date exportDate;

    @ManyToOne
    @JoinColumn(name = "id_exporter", referencedColumnName = "id")
    @JsonManagedReference
    private User exporter;
    @ManyToOne
    @JoinColumn(name = "id_receiver", referencedColumnName = "id")
    @JsonManagedReference
    private User receiver;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "outgoingGoodsNote",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Device> devices;


    public OutgoingGoodsNote(User exporter, User receiver){
        this.exporter=exporter;
        this.receiver = receiver;
        this.exportDate = new Date();
    }


}
