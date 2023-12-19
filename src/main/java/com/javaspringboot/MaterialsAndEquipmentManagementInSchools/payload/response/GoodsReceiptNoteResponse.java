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
public class GoodsReceiptNoteResponse {
    private Long id;
    private String fullname;

    private String phone;

    private String companyName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant export_date;

    private String exporter;

    private List<DeviceResponse> devices;
}
