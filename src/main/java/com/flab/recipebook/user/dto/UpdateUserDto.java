package com.flab.recipebook.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UpdateUserDto {
    @NotBlank
    private Long userNo;

    @NotBlank
    private String currentPassword;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-z])(?=.*[!@#$%_])[a-z\\d!@#$%]{8,16}$",
            message = "비밀번호는 영문자, 숫자, 특수기호(!@#$%)가 1개 이상 포함되어야 합니다.")
    private String modifyPassword;

    @NotBlank(message = "이메일를 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    public UpdateUserDto(Long userNo, String currentPassword, String modifyPassword, String email) {
        this.userNo = userNo;
        this.currentPassword = currentPassword;
        this.modifyPassword = modifyPassword;
        this.email = email;
    }

    public Long getUserNo() {
        return userNo;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getModifyPassword() {
        return modifyPassword;
    }

    public String getEmail() {
        return email;
    }
}
