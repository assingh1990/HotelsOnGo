package com.hotelongo.Authorization_Service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hotelongo.Authorization_Service.entity.User;
import com.hotelongo.Authorization_Service.repo.UserRepository;

@EnableDiscoveryClient
@SpringBootApplication
public class AuthorizationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
	    return args -> {
	        if (repo.findByUsername("ashok").isEmpty()) {
	            User user = new User();
	            user.setUsername("ashok");
	            user.setPassword(encoder.encode("1234"));
	            user.setEmail("er.ashok1990@gmail.com");
	            user.setRole("ROLE_USER");
	            user.setIsActive(true);
	            repo.save(user);
	        }
	    };
	}

}
