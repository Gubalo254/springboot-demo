package com.first99.demo99.service;

import com.first99.demo99.exception.EmailExistsException;
import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegisterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_shouldCreateUserWithEncodedPasswordAndUserRole() {
        // -------- ARRANGE --------
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword";
        String name = "John Doe";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(rawPassword))
                .thenReturn(encodedPassword);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // -------- ACT --------
        User result = userService.registerUser(email, rawPassword, name);

        // -------- ASSERT --------
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals(name, result.getName());
        assertTrue(result.getRoles().contains("USER"));

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowExceptionIfEmailExists() {
        // -------- ARRANGE --------
        String email = "existing@example.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(new User()));

        // -------- ACT + ASSERT --------
        EmailExistsException exception = assertThrows(
                EmailExistsException.class,
                () -> userService.registerUser(email, "password", "Name")
        );

        assertEquals("Email already in use", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}
