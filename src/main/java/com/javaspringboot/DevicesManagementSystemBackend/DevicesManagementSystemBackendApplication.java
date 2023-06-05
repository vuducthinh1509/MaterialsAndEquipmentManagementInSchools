package com.javaspringboot.DevicesManagementSystemBackend;

import com.javaspringboot.DevicesManagementSystemBackend.models.ERole;
import com.javaspringboot.DevicesManagementSystemBackend.models.Role;
import com.javaspringboot.DevicesManagementSystemBackend.repository.RoleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevicesManagementSystemBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(DevicesManagementSystemBackendApplication.class, args);
	}
}
