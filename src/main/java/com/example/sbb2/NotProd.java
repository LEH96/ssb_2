package com.example.sbb2;

import com.example.sbb2.answer.service.AnswerService;
import com.example.sbb2.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
@Configuration
@Profile({"dev", "test"})
public class NotProd {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Bean
    public CommandLineRunner initData(){
        return args -> {
            questionService.write("sbb1","sbb1 content");
            questionService.write("sbb2","sbb2 content");

            answerService.write("sbb2","sbb2 answer");
        };
    }
}
