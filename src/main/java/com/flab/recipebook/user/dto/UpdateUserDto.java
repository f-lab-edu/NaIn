package com.flab.recipebook.user.dto;

public class UpdateUserDto {
    private String password;
    private String email;

    public UpdateUserDto(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
