package com.javaspringboot.DevicesManagementSystemBackend.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UpdateGoodsReceiptNoteRequest {
    @NotBlank
    private String fullname;
    @NotBlank
    private String phone;
    @NotBlank
    private String companyName;
}
