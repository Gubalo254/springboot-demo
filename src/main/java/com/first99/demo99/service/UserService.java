package com.first99.demo99.service;

import com.first99.demo99.dto.UpdateUserRequest;
import com.first99.demo99.model.User;

import java.util.List;


public interface UserService {

    User registerUser(String email, String password, String name);              // register a new user
    User loginUser(String email, String password); // login with email & password
    User updateUserProfile(String email, String name);
    List<User> getAllUsers();
    User makeAdmin(String email);
}

