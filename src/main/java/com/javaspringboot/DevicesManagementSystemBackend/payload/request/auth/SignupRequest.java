package com.javaspringboot.DevicesManagementSystemBackend.payload.request.auth;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @NotEmpty
    private String username;
    @NotBlank
    @NotEmpty
    private String email;
    
    private Set<String> role;
    @NotBlank
    @NotEmpty
    private String password;
    @NotBlank
    @NotEmpty
    private String fullname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    private Date birthDate;
    @NotBlank
    @NotEmpty
    private String phone;
    private Date joinDate;
    @NotBlank
    @NotEmpty
    private String tenVien;

    private String tenPhong;

    private String tenBan;
  
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
    
    public Set<String> getRole() {
      return this.role;
    }
    
    public void setRole(Set<String> role) {
      this.role = role;
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
}
