package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.model.Role;
import com.example.springsecurityjwt.model.User;

public interface UserService {
    User saveUser(User user);
//    Role saveRole(Role role);
}
