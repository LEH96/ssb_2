package com.example.sbb2.question.controller;

import com.example.sbb2.answer.form.AnswerForm;
import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.form.QuestionForm;
import com.example.sbb2.question.service.QuestionService;
import com.example.sbb2.user.entity.SiteUser;
import com.example.sbb2.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Question> paging = questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){
        Question q = questionService.getQuestion(id);
        model.addAttribute("question", q);
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
}
