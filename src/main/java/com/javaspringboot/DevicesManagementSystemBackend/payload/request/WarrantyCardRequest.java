package com.javaspringboot.DevicesManagementSystemBackend.payload.request;

import lombok.Data;

@Data
public class WarrantyCardRequest {
    private String serial;
    private String note;
}
