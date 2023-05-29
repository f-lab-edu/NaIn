package com.flab.recipebook.common.advice;

import com.flab.recipebook.common.dto.ResponseResult;
import com.flab.recipebook.recipe.exception.AccessDeniedException;
import com.flab.recipebook.recipe.exception.RecipeNotFoundException;
import com.flab.recipebook.user.exception.DuplicateValueException;
import com.flab.recipebook.user.exception.NotFoundUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdvice {
    //jwt
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseResult> MethodArgumentNotValidException(MethodArgumentNotValidException exception){
        return new ResponseEntity<>(bindMultiErrors(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateValueException.class)
    protected ResponseEntity<ResponseResult> DuplicateValueException(DuplicateValueException exception){
        return new ResponseEntity<>(bindSingleError(exception), HttpStatus.BAD_REQUEST);
    }

    //Recipe
    @ExceptionHandler(RecipeNotFoundException.class)
    protected ResponseEntity<ResponseResult> RecipeNotFoundException(RecipeNotFoundException exception) {
        return new ResponseEntity<>(bindSingleError(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ResponseResult> AccessDeniedException(AccessDeniedException exception) {
        return new ResponseEntity<>(bindSingleError(exception), HttpStatus.FORBIDDEN);
    }

    //user
    @ExceptionHandler(NotFoundUserException.class)
    protected ResponseEntity<ResponseResult> NotFoundUserException(NotFoundUserException exception) {
        return new ResponseEntity<>(bindSingleError(exception), HttpStatus.BAD_REQUEST);
    }

    private ResponseResult bindMultiErrors(BindException exception){
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseResult(errors);
    }

    private ResponseResult bindSingleError(RuntimeException exception){
        List<String> errors = Arrays.asList(exception.getMessage());
        return new ResponseResult(errors);
    }
}
