package com.example.sbb2.user.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {
    @Size(min=3, max=25)
    @NotEmpty(message = "사용자ID를 입력해주세요.")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotEmpty(message = "비밀번호 확인을 입력해주세요.")
    private String passwordCheck;

    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email //이메일 형식인지 확인
    private String email;
}
