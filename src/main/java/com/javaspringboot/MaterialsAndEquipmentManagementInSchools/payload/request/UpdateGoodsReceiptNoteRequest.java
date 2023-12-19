package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateGoodsReceiptNoteRequest {
    @NotBlank
    private String fullname;
    @NotBlank
    private String phone;
    @NotBlank
    private String companyName;
}
