package com.flab.recipebook.common.advice;

import com.flab.recipebook.common.dto.ResponseResult;
import com.flab.recipebook.user.exception.DuplicateValueException;
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseResult> exceptionMessageBinding(MethodArgumentNotValidException exception){
        List<String> errors = bindMultiErrors(exception);
        ResponseResult responseResult = new ResponseResult(errors);

        return new ResponseEntity<>(responseResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateValueException.class)
    protected ResponseEntity<ResponseResult> RuntimeExceptionMessageBinding(DuplicateValueException exception){
        List<String> errors = bindSingleError(exception);
        ResponseResult responseResult = new ResponseResult(errors);
        
        return new ResponseEntity<>(responseResult, HttpStatus.BAD_REQUEST);
    }

    private List<String> bindMultiErrors(BindException exception){
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());
    }

    private List<String> bindSingleError(RuntimeException exception){
        return Arrays.asList(exception.getMessage());
    }
}
