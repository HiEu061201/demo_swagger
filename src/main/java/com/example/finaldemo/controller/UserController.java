package com.example.finaldemo.controller;

import com.example.finaldemo.entities.User;
import com.example.finaldemo.repository.UserRepository;
import com.example.finaldemo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @QueryMapping(name = "getAllUser")
    public List<User> getCurrentUser() {
        return userService.getListUser();
    }

}
