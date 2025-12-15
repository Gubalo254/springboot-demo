package com.first99.demo99.service;

import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    User registerUser(User user);              // register a new user
    User loginUser(String email, String password); // login with email & password
    void deleteUser(Long id);                  // delete a user by id
    List<User> getAllUsers();                  // optional: list all users

}

