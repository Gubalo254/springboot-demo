package com.first99.demo99.controller;

import com.first99.demo99.model.User;
import com.first99.demo99.service.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
public class UserController {


        private final UserService userService;

        // Constructor injection
        public UserController(UserService userService) {
            this.userService = userService;
        }

        @GetMapping
        public List<User> getAllUsers() {
            return userService.getUsers();
        }

        @PostMapping
        public User createUser(@RequestBody User user) {
            return userService.addUser(user);
        }




    }

