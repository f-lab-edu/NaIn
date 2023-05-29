package com.flab.recipebook.recipe.domain;

import com.flab.recipebook.user.domain.User;

import java.time.LocalDateTime;

public class Recipe {
    private Long recipeId;
    private String title;
    private User user;
    private String recipeType;
    //난이도
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public Recipe() {

    }

    public Recipe(String title, User user, String recipeType, String content) {
        this.title = title;
        this.user = user;
        this.recipeType = recipeType;
        this.content = content;
        this.createDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
    }

    public Recipe(Long recipeId, String title, User user, String recipeType, String content, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.recipeId = recipeId;
        this.title = title;
        this.user = user;
        this.recipeType = recipeType;
        this.content = content;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    //@TODO 삭제예정 (내부 확인용)
    @Override
    public String toString() {
        return "Recipe{\n" +
                "recipeId = " + recipeId + ", \n" +
                "title = " + title + ", \n" +
                "user = {\n" +
                "   userNo = " + user.getUserNo() + ",\n" +
                "   userId = " + user.getUserId() + ",\n" +
                "   password = " + user.getPassword() + ",\n" +
                "   email = " + user.getEmail() + ",\n" +
                "   userRole = " + user.getUserRole() + ",\n" +
                "   createDate = " + user.getCreateDate() + ",\n" +
                "   modifyDate = " + user.getModifyDate() + ",\n" +
                "   refreshToken = " + user.getRefreshToken() + ",\n" +
                "   }\n" +
                "recipeType = " + recipeType + ", \n" +
                "content = " + content + ", \n" +
                "createDate = " + createDate + ", \n" +
                "modifyDate = " + modifyDate + ", \n" +
                '}';
    }
}
