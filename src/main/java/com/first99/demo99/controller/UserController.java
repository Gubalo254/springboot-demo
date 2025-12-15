package com.first99.demo99.controller;

import com.first99.demo99.model.User;
import com.first99.demo99.service.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
public class UserController {

    private final UserService userService; // interface type, not implementation

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestParam String email, @RequestParam String password) {
        return userService.loginUser(email, password);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    }

