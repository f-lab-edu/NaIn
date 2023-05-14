package com.flab.recipebook.recipe.dto;

public class SaveRecipeDto {
    private String title;
    private Long userNo;
    //분야
    //난이도
    private String content;

    public SaveRecipeDto(String title, Long userNo, String content) {
        this.title = title;
        this.userNo = userNo;
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
}
