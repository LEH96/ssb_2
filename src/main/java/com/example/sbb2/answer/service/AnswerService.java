package com.example.sbb2.answer.service;

import com.example.sbb2.DataNotFoundException;
import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.answer.repository.AnswerRepository;
import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.repository.QuestionRepository;
import com.example.sbb2.user.entity.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    @Transactional
    public void write(String question_subject, String content, SiteUser author) {
        Optional<Question> oq = questionRepository.findBySubject(question_subject);
        if(oq.isPresent()){
            Question q = oq.get();
            Answer answer = new Answer();
            answer.setContent(content);
            q.addAnswer(answer);
            answer.setAuthor(author);
            answerRepository.save(answer);
        }
    }

    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        question.addAnswer(answer);
        answer.setAuthor(author);
        answerRepository.save(answer);
        return answer;
    }

    public Answer getAnswer(Integer id){
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isPresent())
            return answer.get();
        else
            throw new DataNotFoundException("answer not found");
    }

    public void modify(Answer answer, String content){
        answer.setContent(content);
        answerRepository.save(answer);
    }

    public void delete(Answer answer){
        answerRepository.delete(answer);
    }

    public void vote(Answer answer, SiteUser user) {
        answer.getVoter().add(user);
        answerRepository.save(answer);
    }
}
