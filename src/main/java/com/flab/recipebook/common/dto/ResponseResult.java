package com.flab.recipebook.common.dto;

import com.flab.recipebook.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseResult {
    private LocalDateTime timestamp;
//    private HttpStatus status;
    private User user;
    private List<String> errors;

    public ResponseResult() {
        this.timestamp = LocalDateTime.now();
//        this.status = status;
    }

    public ResponseResult(User user) {
        this.timestamp = LocalDateTime.now();
//        this.status = status;
        this.user = user;
    }

    public ResponseResult(List<String> errors) {
        this.timestamp = LocalDateTime.now();
//        this.status = status;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    //    public HttpStatus getStatus() {
//        return status;
//    }

    public List<String> getErrors() {
        return errors;
    }
}
