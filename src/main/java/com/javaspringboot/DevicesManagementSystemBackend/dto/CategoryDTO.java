package com.javaspringboot.DevicesManagementSystemBackend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;


}
