package com.flab.recipebook.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class SaveUserDto {
    @NotBlank(message = "아이디를 입력해 주세요")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-z])(?=.*[!@#$%_])[a-z\\d!@#$%]{8,16}$",
            message = "비밀번호는 영문자, 숫자, 특수기호(!@#$%)가 1개 이상 포함되어야 합니다.")
    private String password;
    @NotBlank(message = "이메일를 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    public SaveUserDto(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
