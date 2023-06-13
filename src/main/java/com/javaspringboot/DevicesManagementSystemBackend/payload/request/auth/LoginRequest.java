package com.javaspringboot.DevicesManagementSystemBackend.payload.request.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
	@NotBlank
	private String username;

	@NotEmpty
	private String password;


}
