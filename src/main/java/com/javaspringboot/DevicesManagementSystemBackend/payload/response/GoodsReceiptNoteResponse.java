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
public class GoodsReceiptNoteResponse {
    private String fullname;

    private String phone;

    private String companyName;

    private Date date;

    private String username;

    private Set<Device> devices;
}
