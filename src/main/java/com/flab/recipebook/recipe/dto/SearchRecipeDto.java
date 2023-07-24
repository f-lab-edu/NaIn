package com.flab.recipebook.recipe.dto;

public class SearchRecipeDto {
    private String searchType;
    private String keyword;

    public SearchRecipeDto(String searchType, String keyword) {
        this.searchType = searchType;
        this.keyword = keyword;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getKeyword() {
        return keyword;
    }
}
