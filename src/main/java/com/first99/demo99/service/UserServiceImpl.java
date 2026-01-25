package com.first99.demo99.service;


import com.first99.demo99.exception.EmailExistsException;
import com.first99.demo99.exception.InvalidCredentialsException;
import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {


    private  final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {

        this.passwordEncoder = passwordEncoder;

        this.userRepository = userRepository;
    }
    @Override
    public User registerUser(String email, String password, String name) {

        boolean exists = userRepository.findByEmail(email).isPresent();
        if (exists) {
            throw new EmailExistsException("Email already in use");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);

        // Assign default role

        user.setRoles(new HashSet<>(Set.of("USER")));

        userRepository.save(user);
        return user;
    }

    @Override
    public User loginUser(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return user;
    }
    @Override
    public User updateUserProfile(String email, String name) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(name);

        return userRepository.save(user);
    }
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User makeAdmin(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<String> roles = user.getRoles();
        roles.add("ADMIN");
        user.setRoles(roles);
        return userRepository.save(user);
    }


}