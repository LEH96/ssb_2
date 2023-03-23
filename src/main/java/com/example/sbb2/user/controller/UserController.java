package com.example.sbb2.user.controller;

import com.example.sbb2.user.form.UserForm;
import com.example.sbb2.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserForm userForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserForm userForm, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "signup_form";

        if(!userForm.getPassword().equals(userForm.getPasswordCheck())) {
            //특정 필드에 대한 오류처리
            bindingResult.rejectValue("passwordCheck", "passwordInCorrect",
                    "패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        try {
            userService.create(userForm.getUsername(), userForm.getPassword(), userForm.getEmail());
        } catch(DataIntegrityViolationException e){
            //일반적인 오류 처리
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch(Exception e){
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }
}
