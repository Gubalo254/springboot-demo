package com.first99.demo99.service;

import com.first99.demo99.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();

    public List<User> getUsers() { return users; }
    public User addUser(User user) {
        users.add(user);
        return user;
    }
}
