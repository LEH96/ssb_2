package com.example.sbb2.question.controller;

import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.answer.form.AnswerForm;
import com.example.sbb2.answer.service.AnswerService;
import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.form.QuestionForm;
import com.example.sbb2.question.service.QuestionService;
import com.example.sbb2.user.entity.SiteUser;
import com.example.sbb2.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> paging = questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm,
                         @RequestParam(value = "page", defaultValue = "0") int page){
        Question q = questionService.getQuestion(id);
        model.addAttribute("question", q);

        Page<Answer> paging = answerService.getAnswerList(q, page);
        model.addAttribute("paging", paging);

        questionService.updateView(id);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(QuestionForm questionForm) {
        return "question_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal){
        SiteUser user = userService.getUser(principal.getName());

        if(bindingResult.hasErrors())
            return "question_form";
        questionService.addQuestion(questionForm.getSubject(), questionForm.getContent(), user);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, QuestionForm questionForm, Principal principal){
        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, @Valid QuestionForm questionForm,
                         Principal principal, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "question_form";

        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName()))
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");

        questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/detail/%s".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName()))
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String vote(@PathVariable("id") Integer id, Principal principal) {
        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());
        questionService.vote(question, user);
        return "redirect:/question/detail/%s".formatted(id);
    }
}
