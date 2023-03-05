package com.flab.recipebook.user.domain;

public enum UserRole {
    USER("사용자"),
    CHEF("요리사"),
    ADMIN("관리자")
    ;
    private final String role;

    UserRole(String role) {
        this.role = role;
    }
    public String getValue(){
        return role;
    }
}
