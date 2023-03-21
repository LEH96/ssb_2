package com.example.sbb2.base;

import com.example.sbb2.answer.service.AnswerService;
import com.example.sbb2.question.service.QuestionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    public CommandLineRunner initData(QuestionService questionService, AnswerService answerService){
        return args -> {
            questionService.write("sbb1","sbb1 content");
            questionService.write("sbb2","sbb2 content");

            answerService.write("sbb2","sbb2 answer");
        };
    }
}
