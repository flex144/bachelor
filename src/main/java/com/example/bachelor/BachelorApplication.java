package com.example.bachelor;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.dto.JournalEntryDto;
import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.JournalEntryEntity;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.data.enums.UserRoles;
import com.example.bachelor.repositories.GuardDayRepository;
import com.example.bachelor.repositories.JournalEntryRepository;
import com.example.bachelor.repositories.UserRepository;
import com.example.bachelor.services.GuardDayService;
import com.example.bachelor.services.JournalService;
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
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableAsync
public class BachelorApplication {

	private static final Logger log = LoggerFactory.getLogger(BachelorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BachelorApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner mappingDemo(UserRepository userRepository, UserService userService,
										 JournalEntryRepository journalEntryRepository,
										 GuardDayRepository guardDayRepository,
										 GuardDayService guardDayService,
										 JournalService journalService) {
		*//*return args -> {

			log.info("encoded PW="+userService.encode("123456"));

			Set<JournalEntryEntity> journalEntryEntitySet = new HashSet<>();

			JournalEntryEntity j1 = new JournalEntryEntity();
			j1.setDescription("hallo Welt");

			JournalEntryEntity j2 = new JournalEntryEntity();
			j2.setDescription("Hallo welt");

			journalEntryEntitySet.add(j1);
			journalEntryEntitySet.add(j2);

			GuardDayEntity guardDayEntity = new GuardDayEntity();

			guardDayEntity.setJournalEntries(journalEntryEntitySet);

			GuardDayEntity ent = guardDayService.saveGuardDayEntity(guardDayEntity);


			GuardDayDto guardRead = guardDayService.readGuardDayById(ent.getGuardDayId());

			JournalEntryDto j3 = new JournalEntryDto();
			j3.setDescription("j3");

			guardRead.getJournalEntries().add(j3);

			guardDayService.saveGuardDayDto(guardRead);

			GuardDayDto guardRead2 = guardDayService.readGuardDayById(guardRead.getGuardDayId());
*//*



*//*			journalService.saveJournal(journalEntity);

			Iterable<JournalEntity> ents = journalRepository.findAll();

			journalRepository.findAll();*//*

			// create a new book
			*//*UserEntity userEntity = new UserEntity();
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

			userRepository.save(userEntity2);*//*
		};*/


}
