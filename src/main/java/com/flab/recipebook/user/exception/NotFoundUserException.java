package com.flab.recipebook.user.exception;

public class NotFoundUserException extends RuntimeException{
    public NotFoundUserException() {
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}
