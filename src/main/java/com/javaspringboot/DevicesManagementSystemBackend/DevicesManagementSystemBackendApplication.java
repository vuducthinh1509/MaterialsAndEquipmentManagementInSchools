package com.javaspringboot.DevicesManagementSystemBackend;

import com.javaspringboot.DevicesManagementSystemBackend.enumm.ERole;
import com.javaspringboot.DevicesManagementSystemBackend.model.Role;
import com.javaspringboot.DevicesManagementSystemBackend.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class DevicesManagementSystemBackendApplication implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(DevicesManagementSystemBackendApplication.class, args);
	}
	// Hàm sẽ được gọi sau khi project được khởi chạy
	@Override
	public void run(String... args) throws Exception {
//		roleRepository.save(new Role(ERole.ROLE_ADMIN));
//		roleRepository.save(new Role(ERole.ROLE_USER));
		logger.info("Hi thinhdz");
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns("http://localhost:3000");
			}
		};
	}


}
