package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.model.Role;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.model.UserRequest;
import com.example.springsecurityjwt.service.UserService;
import com.example.springsecurityjwt.utils.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogInController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    public LogInController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/user-log-in")
    public ResponseEntity<String> logIn(@RequestBody UserRequest request){
        HttpHeaders headers = new HttpHeaders();
        String token = "Bearer ", message = "";
        if(request.getUser() != null && request.getRole() != null){
            request.getUser().setRoles(request.getRole());
            for(Role role: request.getRole()){
                role.setUser(request.getUser());
            }
            User user = userService.saveUser(request.getUser());

            if(user != null){
                token = jwtTokenUtil.generateToken(user.getUserName());
                message = "User Created Successfully.";
            }else{
                message = "Something went wrong";
            }
        }else{
            message = "User Not Found";
        }
        headers.set(HttpHeaders.AUTHORIZATION, token);
        return new ResponseEntity<>(message, headers, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticate(@RequestBody User user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenUtil.generateToken(principal.getUsername()));
        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }
}
