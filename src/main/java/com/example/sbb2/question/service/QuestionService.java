package com.example.sbb2.question.service;

import com.example.sbb2.DataNotFoundException;
import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.repository.QuestionRepository;
import com.example.sbb2.user.entity.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    public Question write(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(user);
        questionRepository.save(q);
        return q;
    }

    public List<Question> getList() {
        return questionRepository.findAll();
    }

    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); //10개씩 보여줌
        return this.questionRepository.findAll(pageable);
    }

    public Question getQuestion(Integer id) {
        Optional<Question> oq = questionRepository.findById(id);
        if(oq.isPresent())
            return oq.get();
        else
            throw new DataNotFoundException("question not found");
    }

    public void addQuestion(String subject, String content, SiteUser author) {
        Question question = write(subject, content, author);
        questionRepository.save(question);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }
}
