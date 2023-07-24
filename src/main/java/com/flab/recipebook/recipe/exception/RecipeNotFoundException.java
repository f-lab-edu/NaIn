package com.flab.recipebook.recipe.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException() {
        super();
    }

    public RecipeNotFoundException(String message) {
        super(message);
    }
}
