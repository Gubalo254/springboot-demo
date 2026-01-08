package com.first99.demo99.controller;
import com.first99.demo99.dto.UserResponse;
import com.first99.demo99.model.User;
import com.first99.demo99.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/users")

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Promote a user to admin
    @PutMapping("/promote/{email}")
    public UserResponse promoteToAdmin(@PathVariable String email) {
        User newAdmin = userService.makeAdmin(email);


        return new UserResponse(
                newAdmin.getEmail(), newAdmin.getName(), " has been promoted to admin!");
    }
}

