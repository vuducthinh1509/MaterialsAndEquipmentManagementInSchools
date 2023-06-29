package com.javaspringboot.DevicesManagementSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @NotBlank
  private String fullname;

  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private Date birthDate;

  @NotBlank
  private String phone;

  private Instant joinDate;

  @NotBlank
  private String tenVien;

  private String tenPhong;

  private String tenBan;

  private String refreshToken;

  private Instant expiryRefreshToken;

  @OneToMany(mappedBy = "exporter")
  @JsonBackReference
  private Set<WarrantyCard> warrantyCards1;

  @OneToMany(mappedBy = "receiver")
  @JsonBackReference
  private Set<WarrantyCard> warrantyCards2;

  @OneToMany(mappedBy = "exporter")
  @JsonBackReference
  private Set<OutgoingGoodsNote> outgoingGoodsNotes1;

  @OneToMany(mappedBy = "receiver")
  @JsonBackReference
  private Set<OutgoingGoodsNote> outgoingGoodsNotes2;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id"))
  @JsonManagedReference
  private Set<Role> roles = new HashSet<>();

  @NotNull
  private boolean isEnabled = true;

  public User(String username, String email, String password, String fullname,Date birthDate, String phone, String tenVien, String tenPhong, String tenBan) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.fullname = fullname;
    this.birthDate = birthDate;
    this.phone = phone;
    this.joinDate = Instant.now();
    this.tenVien = tenVien;
    this.tenPhong = tenPhong;
    this.tenBan = tenBan;
  }

  public User(String fullname,Date birthDate, String phone, String tenVien, String tenPhong, String tenBan){
    this.fullname=fullname;
    this.birthDate = birthDate;
    this.phone=phone;
    this.tenVien=tenVien;
    this.tenPhong=tenPhong;
    this.tenBan=tenBan;
  }

    public void setDisable(){
      this.isEnabled = !this.isEnabled;
    }
}
