package com.flab.recipebook.user.controller;

import com.flab.recipebook.common.dto.ResponseResult;
import com.flab.recipebook.user.dto.SaveUserDto;
import com.flab.recipebook.user.dto.UpdateUserDto;
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

    @PostMapping("/signup")
    public ResponseEntity<ResponseResult> saveUser(@RequestBody @Valid SaveUserDto request) {
        userService.save(request);
        return new ResponseEntity<>(new ResponseResult(), HttpStatus.CREATED);
    }

    @GetMapping("/users/profile/{userNo}")
    public ResponseEntity<ResponseResult> findById(@PathVariable Long userNo) {
        return new ResponseEntity<>(new ResponseResult(userService.findById(userNo)), HttpStatus.OK);
    }

    @DeleteMapping("/users/profile/{userNo}")
    public ResponseEntity<ResponseResult> deleteUser(@PathVariable Long userNo) {
        userService.deleteById(userNo);
        return new ResponseEntity<>(new ResponseResult(), HttpStatus.OK);
    }

    @PutMapping("/users/profile")
    public ResponseEntity<ResponseResult> updateUser(@Valid @RequestBody UpdateUserDto request) {
        userService.updateUser(request);
        return new ResponseEntity<>(new ResponseResult(), HttpStatus.CREATED);
    }
}
