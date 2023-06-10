package com.javaspringboot.DevicesManagementSystemBackend.payload.request.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
	@NotBlank
	@NotEmpty
	private String username;

	@NotEmpty
	@NotBlank
	private String password;


}
