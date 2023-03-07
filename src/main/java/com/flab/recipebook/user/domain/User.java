package com.flab.recipebook.user.domain;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String userId;
    private String password;
    private String email;
    private UserRole userRole;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public User(String userId, String password, String email, UserRole userRole) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
        this.createDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }
}
