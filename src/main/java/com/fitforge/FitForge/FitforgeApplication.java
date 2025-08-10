// This is the complete, updated code for FitForgeApplication.java
package com.fitforge.FitForge;

import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FitforgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitforgeApplication.class, args);
	}

	/**
	 * This CommandLineRunner bean will be executed on application startup.
	 * It creates default users with HASHED passwords for testing.
	 * This now works correctly because the full SecurityConfig is loaded first.
	 */
	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Create a regular user with role 'USER'
			if (userRepository.findByUsername("user").isEmpty()) {
				User user = new User();
				user.setUsername("user");
				user.setEmail("user@example.com");
				user.setPassword(passwordEncoder.encode("password")); // Hashed password
				user.setRole("USER");
				userRepository.save(user);
				System.out.println(">>> Created Test User: username='user', password='password'");
			}

			// Create an admin user with role 'ADMIN'
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setEmail("admin@example.com");
				admin.setPassword(passwordEncoder.encode("adminpass")); // Hashed password
				admin.setRole("ADMIN");
				userRepository.save(admin);
				System.out.println(">>> Created Test Admin: username='admin', password='adminpass'");
			}
		};
	}
}