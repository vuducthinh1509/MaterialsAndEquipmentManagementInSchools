package com.javaspringboot.DevicesManagementSystemBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name="id")
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @NotEmpty
  @Size(max = 120)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @NotBlank
  private String fullname;

  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private Date birthDate;

  @NotBlank
  private String phone;

  private Date joinDate;

  @NotBlank
  private String tenVien;

  private String tenPhong;

  private String tenBan;

  @OneToMany(mappedBy = "user1")
  private Set<WarrantyCard> warrantyCards1;

  @OneToMany(mappedBy = "user2")
  private Set<WarrantyCard> warrantyCards2;

  @JsonIgnore
  private String[] authorities;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", 
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User() {
  }

  public User(String username, String email, String password, String fullname,Date birthDate, String phone, String tenVien, String tenPhong, String tenBan) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.fullname = fullname;
    this.birthDate = birthDate;
    this.phone = phone;
    this.joinDate = new Date();
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Date getJoinDate() {
    return joinDate;
  }

  public void setJoinDate(Date joinDate) {
    this.joinDate = joinDate;
  }

  public String getTenVien() {
    return tenVien;
  }

  public void setTenVien(String tenVien) {
    this.tenVien = tenVien;
  }

  public String getTenPhong() {
    return tenPhong;
  }

  public void setTenPhong(String tenPhong) {
    this.tenPhong = tenPhong;
  }

  public String getTenBan() {
    return tenBan;
  }

  public void setTenBan(String tenBan) {
    this.tenBan = tenBan;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public String[] getAuthorities() {
    return authorities;
  }

  public void setAuthorities(String[] authorities) {
    this.authorities = authorities;
  }
}
