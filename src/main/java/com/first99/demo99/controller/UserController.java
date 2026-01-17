package com.first99.demo99.controller;

import com.first99.demo99.dto.*;
import com.first99.demo99.model.User;
import com.first99.demo99.security.JwtUtil;
import com.first99.demo99.service.TokenBlacklistService;
import com.first99.demo99.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



@SpringBootApplication
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;


    public UserController(UserService userService, JwtUtil jwtUtil, TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = tokenBlacklistService;

    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid UserRegisterRequest request) {
        User user = userService.registerUser(request.getEmail(), request.getPassword(), request.getName());


        return new UserResponse(user.getEmail(), user.getName(),"User registered successfully");
    }


    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid UserLoginRequest request) {
        User user = userService.loginUser(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }

    @PutMapping("/update/name")
    public UserResponse updateProfile(
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName(); // from JWT
        User updatedUser = userService.updateUserProfile(email, request.getName());

        return new UserResponse(
                updatedUser.getEmail(), updatedUser.getName(),
                "Profile updated successfully"
        );
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token); // add token to blacklist
        }

        return ResponseEntity.ok("Logged out successfully");
    }
}
