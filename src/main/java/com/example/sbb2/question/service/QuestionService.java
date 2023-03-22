package com.example.sbb2.question.service;

import com.example.sbb2.DataNotFoundException;
import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Question> getList() {
        return questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        Optional<Question> oq = questionRepository.findById(id);
        if(oq.isPresent())
            return oq.get();
        else
            throw new DataNotFoundException("question not found");
    }

    public void addQuestion(String subject, String content) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        questionRepository.save(question);
    }
}
