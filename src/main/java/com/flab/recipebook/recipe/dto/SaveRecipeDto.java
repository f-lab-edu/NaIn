package com.flab.recipebook.recipe.dto;

import java.util.List;

public class SaveRecipeDto {
    private String title;
    private Long userNo;
    //분야
    String recipeType;
    //난이도
    private String content;

    public SaveRecipeDto(String title, Long userNo, List<String> recipeType, String content) {
        this.title = title;
        this.userNo = userNo;
        this.recipeType = convertRecipeType(recipeType);
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public Long getUserNo() {
        return userNo;
    }

    public String getContent() {
        return content;
    }

    public String getRecipeType() {
        return recipeType;
    }

    private String convertRecipeType(List<String> recipeType) {
        if (recipeType == null || recipeType.isEmpty()) {
            return null;
        }
        return String.join(",", recipeType);
    }
}
