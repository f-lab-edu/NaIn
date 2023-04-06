package com.flab.recipebook.user.dto;

import com.flab.recipebook.user.domain.UserRole;

import java.time.LocalDateTime;

public class ResponseUserDto {
    private Long userNo;
    private String userId;
    private String password;
    private String email;
    private UserRole userRole;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public ResponseUserDto(Long userNo, String userId, String password, String email, UserRole userRole, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.userNo = userNo;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public Long getUserNo() {
        return userNo;
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

    public UserRole getUserRole() {
        return userRole;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }
}
