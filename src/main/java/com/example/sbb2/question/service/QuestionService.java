package com.example.sbb2.question.service;

import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    public void write(String subject, String content) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        questionRepository.save(q);
    }
}
