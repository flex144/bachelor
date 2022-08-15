package com.example.bachelor;

import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.data.enums.UserRoles;
import com.example.bachelor.repositories.UserRepository;
import com.example.bachelor.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
public class BachelorApplication {

	private static final Logger log = LoggerFactory.getLogger(BachelorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BachelorApplication.class, args);
	}

	@Bean
	public CommandLineRunner mappingDemo(UserRepository userRepository, UserService userService) {
		return args -> {

			log.info("encoded PW="+userService.encode("123456"));

			// create a new book
			/*UserEntity userEntity = new UserEntity();
			userEntity.setEmail("fwilhelm2@gmx.de");
			userEntity.setFirstName("Felix");
			userEntity.setLastName("Wilhelm");
			userEntity.setPassword("123456");
			userEntity.setRole(UserRoles.ADMIN);

			userRepository.deleteAll();

			// save the book
			userRepository.save(userEntity);

			// create and save new pages
			for (UserEntity user :  userRepository.findAll()) {
				log.info(user.toString());
			}

			UserEntity userEntity2 = new UserEntity();
			userEntity2.setEmail("fwilhelm2@gmx.de");
			userEntity2.setFirstName("Felix");
			userEntity2.setLastName("Wilhelm");
			userEntity2.setPassword("123456");
			userEntity2.setRole(UserRoles.ADMIN);

			userRepository.save(userEntity2);*/
		};
	}

}
