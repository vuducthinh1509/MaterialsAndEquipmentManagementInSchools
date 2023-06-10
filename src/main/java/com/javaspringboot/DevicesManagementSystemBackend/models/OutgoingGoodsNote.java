package com.javaspringboot.DevicesManagementSystemBackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    private Set<Device> devices;


    public OutgoingGoodsNote(User exporter, User receiver){
        this.exporter=exporter;
        this.receiver = receiver;
        this.exportDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public User getExporter() {
        return exporter;
    }

    public void setExporter(User exporter) {
        this.exporter = exporter;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }


}
