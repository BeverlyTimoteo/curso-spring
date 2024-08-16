package com.fvsigeapi.fvsige_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.fvsigeapi.fvsige_api.enums.Category;
import com.fvsigeapi.fvsige_api.model.Course;
import com.fvsigeapi.fvsige_api.model.Lesson;
import com.fvsigeapi.fvsige_api.repository.CourseRepository;

@SpringBootApplication
public class FvsigeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FvsigeApiApplication.class, args);
	}

	@Bean
	@Profile("dev")
	CommandLineRunner initDatabase(CourseRepository courseRepository) {
		return args -> {
			courseRepository.deleteAll();

			Course c = new Course();
			c.setName("Angular é foda");
			c.setCategory(Category.FRONT_END);

			Lesson l = new Lesson();
			l.setNome("Introdução");
			l.setYoutubeUrl("https://aas");
			l.setCurso(c);

			c.getLessons().add(l);

			Lesson l1 = new Lesson();
			l1.setNome("Angular");
			l1.setYoutubeUrl("https://aad");
			l1.setCurso(c);

			c.getLessons().add(l1);

			courseRepository.save(c);
		};
	}
}
