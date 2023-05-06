package com.flab.recipebook.common.dto;

import com.flab.recipebook.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseResult {
    private LocalDateTime timestamp;
//    private HttpStatus status;
    private Object value;
    private List<String> errors;

    public ResponseResult() {
        this.timestamp = LocalDateTime.now();
//        this.status = status;
    }

    public ResponseResult(Object value) {
        this.timestamp = LocalDateTime.now();
//        this.status = status;
        this.value = value;
    }

    public ResponseResult(List<String> errors) {
        this.timestamp = LocalDateTime.now();
//        this.status = status;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Object getValue() {
        return value;
    }

    //    public HttpStatus getStatus() {
//        return status;
//    }

    public List<String> getErrors() {
        return errors;
    }
}
