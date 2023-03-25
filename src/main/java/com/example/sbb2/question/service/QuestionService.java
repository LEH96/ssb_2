package com.example.sbb2.question.service;

import com.example.sbb2.DataNotFoundException;
import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.repository.QuestionRepository;
import com.example.sbb2.user.entity.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw) {
        return new Specification<Question>() {
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); //중복 제거
                //question 테이블과 user 테이블을 question_author_id 기준으로 left join
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                //question 테이블과 answer 테이블을 question_id 기준으로 left join
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                //answer 테이블과 user 테이블을 answer_author_id 기준으로 left join
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), //제목 조회
                        cb.like(q.get("content"), "%" + kw + "%"), //질문 내용 조회
                        cb.like(u1.get("username"), "%" + kw + "%"), //질문 작성자 조회
                        cb.like(a.get("content"), "%" + kw + "%"), //답변 내용 조회
                        cb.like(u2.get("username"), "%" + kw + "%")); //답변 작성자 조회
            }
        };
    }

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

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        questionRepository.save(question);
    }

    public  Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAllByKeyword(kw, pageable);
    }
}
