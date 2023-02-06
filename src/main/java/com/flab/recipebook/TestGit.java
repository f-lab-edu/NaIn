package com.flab.recipebook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestGit {

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello";
    }
}
