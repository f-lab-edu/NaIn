package com.flab.recipebook.user.controller;

import com.flab.recipebook.user.dto.SaveUserDto;
import com.flab.recipebook.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public void saveUser(@RequestBody SaveUserDto request) {
        userService.save(request);
    }

}
