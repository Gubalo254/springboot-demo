package com.first99.demo99.service;

import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }


    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }
}
