package com.first99.demo99.repository;

import com.first99.demo99.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom method to find a user by email
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
