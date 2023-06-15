package com.javaspringboot.DevicesManagementSystemBackend.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date exportDate;
    private Set<Device> devices;
}
