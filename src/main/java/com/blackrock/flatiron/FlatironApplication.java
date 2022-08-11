package com.blackrock.flatiron;

import com.blackrock.flatiron.model.Author;
import com.blackrock.flatiron.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@SpringBootApplication
public class FlatironApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlatironApplication.class, args);
	}

	@Component
	public class StartUpRunner implements CommandLineRunner {
		@Autowired
		private AuthorRepository authorRepository;
		@Override
		public void run(String... args) throws Exception {
			authorRepository.saveAll(Arrays.asList(
					Author.builder().name("James Patterson").build(),
					Author.builder().name("John Grisham").build(),
					Author.builder().name("J R Ward").build()
			));
			System.out.println(authorRepository.count());
		}
		@Bean
		public ModelMapper modelMapper(){
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			return modelMapper;
		}
	}
}
