package com.javaspringboot.DevicesManagementSystemBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.Set;

@Data
@AllArgsConstructor
@Getter
@Setter
public class OutgoingGoodsNoteDTO {

    @Min(value = 0L,message = "must be positive")
    private String receiver;
    private Set<String> devices;
}
