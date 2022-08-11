package com.blackrock.flatiron;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class FlatironApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlatironApplication.class, args);
	}


	@Component
	public class StartUpRunner implements CommandLineRunner {
		@Override
		public void run(String... args) throws Exception {

		}

//		@Autowired
//		private ActivityRepository activityRepository;
//		@Override
//		public void run(String... args) throws Exception {
//			System.out.println("Running");
//			activityRepository.saveAll(Arrays.asList(
//					Activity.builder().name("Archery").difficulty(3).build(),
//					Activity.builder().name("Hatchet Throwing").difficulty(5).build(),
//					Activity.builder().name("Firestarting").difficulty(2).build()
//			));
//			System.out.println(activityRepository.count());
//		}
	}

}
