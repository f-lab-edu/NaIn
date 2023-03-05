package com.flab.recipebook.user.domain;

import com.flab.recipebook.user.dto.SaveUserDto;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String userId;
    private String password;
    private String email;
    private UserRole userRole;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    private User(SaveUserDto saveUserDto) {
        this.userId = saveUserDto.getUserId();
        this.password = saveUserDto.getPassword();
        this.email = saveUserDto.getEmail();
        this.userRole = UserRole.USER;
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

    public static User saveUerConvert(SaveUserDto saveUserDto){
        return new User(saveUserDto);
    }

}
