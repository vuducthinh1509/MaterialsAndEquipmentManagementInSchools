package com.javaspringboot.DevicesManagementSystemBackend.payload.request.category;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Data
public class AddCategoryRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;

}
