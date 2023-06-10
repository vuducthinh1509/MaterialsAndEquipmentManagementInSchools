package com.javaspringboot.DevicesManagementSystemBackend.payload.response;

import com.javaspringboot.DevicesManagementSystemBackend.models.Device;
import lombok.*;

import javax.validation.constraints.Min;
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
    private Set<Device> devices;
}
