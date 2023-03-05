package com.flab.recipebook.user.dto;

public class SaveUserDto {
    private String userId;
    private String password;
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
