package com.example.springsecurityjwt.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRequest {
    private User user;
    private List<Role> role;

    public UserRequest(){

    }
    public UserRequest(User user, List<Role> role){
        this.user = user;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }
}
