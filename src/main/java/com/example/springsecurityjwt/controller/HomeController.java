package com.example.springsecurityjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class HomeController {

    @RolesAllowed("ROLE_USER")
    @GetMapping("/welcome")
    public String getHomePage(){
        return "Welcome";
    }
}
