package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;
import java.util.List;

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
    private Instant exportDate;
    private List<DeviceResponse> devices;
}
