package com.example.sbb2;

import com.example.sbb2.answer.service.AnswerService;
import com.example.sbb2.question.service.QuestionService;
import com.example.sbb2.user.entity.SiteUser;
import com.example.sbb2.user.service.UserService;
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
    private final UserService userService;

    @Bean
    public CommandLineRunner initData(){
        return args -> {
            for(int i=1;i<=300;i++){
                String subject = "test sbb" + i;
                String content = subject + " content";

                questionService.write(subject, content, null);
            }

            answerService.write("test sbb2","sbb2 answer", null);
            answerService.write("test sbb300","sbb300 answer", null);
            answerService.write("test sbb299","sbb299 answer", null);
            answerService.write("test sbb299","sbb299 answer", null);

            userService.create("user1", "123", "user1@naver.com");
        };
    }
}
