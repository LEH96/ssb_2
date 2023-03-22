package com.example.sbb2.answer.controller;

import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.answer.service.AnswerService;
import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.service.QuestionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, String content) throws Exception {
        Question question = questionService.getQuestion(id);
        answerService.create(question, content);
        return "redirect:/question/detail/%d".formatted(id);
    }

}
