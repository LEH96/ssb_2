package com.example.sbb2;

import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.question.entity.Question;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Sbb2Application {

	public static void main(String[] args) {
		SpringApplication.run(Sbb2Application.class, args);
	}

}
