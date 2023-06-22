package com.javaspringboot.DevicesManagementSystemBackend.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
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
    private Date export_date;

    private String exporter;

    private List<DeviceResponse> devices;
}
