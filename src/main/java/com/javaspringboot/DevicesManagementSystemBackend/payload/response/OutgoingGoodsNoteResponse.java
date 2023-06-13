package com.javaspringboot.DevicesManagementSystemBackend.payload.response;

import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class OutgoingGoodsNoteResponse {
    private Long id;
    private String exporter;

    private String receiver;

    private Date exportDate;
    private Set<Device> devices;
}
