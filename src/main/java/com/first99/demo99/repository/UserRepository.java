package com.first99.demo99.repository;

import com.first99.demo99.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
