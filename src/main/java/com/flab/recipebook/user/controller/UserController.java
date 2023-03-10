package com.flab.recipebook.user.controller;

import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.dto.SaveUserDto;
import com.flab.recipebook.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<String> saveUser(@RequestBody @Valid SaveUserDto request) {
            userService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users/{userNo}")
    public ResponseEntity<User> findById(@PathVariable Long userNo) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userNo));
    }
}
