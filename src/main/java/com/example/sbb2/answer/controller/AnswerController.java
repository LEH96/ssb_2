package com.example.sbb2.answer.controller;

import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.answer.form.AnswerForm;
import com.example.sbb2.answer.service.AnswerService;
import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.service.QuestionService;
import com.example.sbb2.user.entity.SiteUser;
import com.example.sbb2.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) throws Exception {
        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());

        if(bindingResult.hasErrors()){
            model.addAttribute("question", question);
            return "question_detail";
        }

        answerService.create(question, answerForm.getContent(), user);
        return "redirect:/question/detail/%d".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, AnswerForm answerForm,
                         Principal principal){
        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName()))
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");

        answerForm.setContent(answer.getContent());
        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@PathVariable("id") Integer id, @Valid AnswerForm answerForm,
                         Principal principal, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "answer_form";

        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");

        answerService.modify(answer, answerForm.getContent());
        return "redirect:/question/detail/%s".formatted(answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName()))
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        answerService.delete(answer);
        return "redirect:/question/detail/%s".formatted(answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String vote(@PathVariable("id") Integer id, Principal principal) {
        Answer answer = answerService.getAnswer(id);
        SiteUser user = userService.getUser(principal.getName());
        answerService.vote(answer, user);
        return "redirect:/question/detail/%s".formatted(answer.getQuestion().getId());
    }
}
