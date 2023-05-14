package com.flab.recipebook.recipe.domain;

import java.time.LocalDateTime;

public class Recipe {
    private Long recipeId;
    private String title;
    private Long userNo;
    //분야
    //난이도
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public Recipe(String title, Long userNo, String content) {
        this.title = title;
        this.userNo = userNo;
        this.content = content;
        this.createDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
    }

    public Recipe(Long recipeId, String title, Long user, String content, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.recipeId = recipeId;
        this.title = title;
        this.userNo = user;
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

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
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
}
