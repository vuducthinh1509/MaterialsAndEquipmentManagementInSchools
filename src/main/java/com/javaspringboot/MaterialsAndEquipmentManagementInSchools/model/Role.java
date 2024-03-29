package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;


  public Role(ERole name) {
    this.name = name;
  }
}