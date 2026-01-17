package com.first99.demo99.service;


import com.first99.demo99.exception.InvalidCredentialsException;
import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void loginUser_shouldReturnUser_whenCredentialsAreValid() {
        // -------- ARRANGE --------
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$encoded";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRoles(Set.of("USER"));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(true);

        // -------- ACT --------
        User result = userService.loginUser(email, rawPassword);

        // -------- ASSERT --------
        assertNotNull(result);
        assertEquals(email, result.getEmail());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1))
                .matches(rawPassword, encodedPassword);
    }

    @Test
    void loginUser_shouldThrowException_whenEmailDoesNotExist() {
        // -------- ARRANGE --------
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        // -------- ACT + ASSERT --------
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> userService.loginUser("missing@example.com", "password")
        );

        assertEquals("Invalid credentials", exception.getMessage());

        verify(userRepository, times(1))
                .findByEmail("missing@example.com");
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void loginUser_shouldThrowException_whenPasswordIsIncorrect() {
        // -------- ARRANGE --------
        String email = "test@example.com";
        String rawPassword = "wrongPassword";
        String encodedPassword = "$2a$10$encoded";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(false);

        // -------- ACT + ASSERT --------
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> userService.loginUser(email, rawPassword)
        );

        assertEquals("Invalid credentials", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1))
                .matches(rawPassword, encodedPassword);
    }
}
