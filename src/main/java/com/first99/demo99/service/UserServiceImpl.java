package com.first99.demo99.service;


import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Constructor injection (Spring will inject the repository automatically)
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {
        // Here you can add validation, hashing passwords, etc.
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String password) {
        // Example: find by email and check password
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}