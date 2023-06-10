package com.javaspringboot.DevicesManagementSystemBackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;


}
