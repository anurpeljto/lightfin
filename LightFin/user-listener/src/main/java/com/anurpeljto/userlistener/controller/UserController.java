package com.anurpeljto.userlistener.controller;

import com.anurpeljto.userlistener.domain.User;
import com.anurpeljto.userlistener.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/user/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping(path = "/users")
    public List<User> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size
            ) {
        return userService.getAllUsers(PageRequest.of(page, size)).getContent();
    }

    @GetMapping(path = "/user/email/{email}")
    public User getUserByEmail(
            @PathVariable("email") String email
    ){
        return userService.getUserByEmail(email);
    }
}
