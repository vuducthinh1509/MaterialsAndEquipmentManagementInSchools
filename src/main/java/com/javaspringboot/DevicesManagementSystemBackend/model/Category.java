package com.javaspringboot.DevicesManagementSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name="categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "description"),
        })
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotEmpty
    private String name;

    @NotBlank
    @NotEmpty
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL) // Quan hệ 1-n với đối tượng ở dưới (Person) (1 địa điểm có nhiều người ở)
    @JsonIgnore
    private Set<Device> devices;

    public Category(){}

    public Category(String description){
        this.description = description;
    }

    public Category(String name, String description){
        this.name=name;
        this.description=description;
    }

}
