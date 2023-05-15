package com.flab.recipebook.recipe.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ResponseRecipeDto {
    private Long recipeId;
    private String title;
    private List<String> recipeType;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String userId;
    //난이도

    public ResponseRecipeDto(Long recipeId, String title, String recipeType, String content, LocalDateTime createDate, LocalDateTime modifyDate, String userId) {
        this.recipeId = recipeId;
        this.title = title;
        this.recipeType = setRecipeType(recipeType);
        this.content = content;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.userId = userId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getRecipeType() {
        return recipeType;
    }

    public List<String> setRecipeType(String recipeType) {
        if (recipeType == null) {
            return null;
        }
        String[] typeList = recipeType.split(",");
        return Arrays.asList(typeList);
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ResponseRecipeDto{" +
                "recipeId=" + recipeId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", create_date=" + createDate +
                ", modify_date=" + modifyDate +
                ", user_id='" + userId + '\'' +
                '}';
    }
}
