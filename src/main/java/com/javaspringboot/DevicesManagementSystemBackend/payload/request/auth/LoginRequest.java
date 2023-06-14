package com.javaspringboot.DevicesManagementSystemBackend.payload.request.auth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
public class LoginRequest {
	@NotBlank
	private String username;

	@NotEmpty
	private String password;
}
