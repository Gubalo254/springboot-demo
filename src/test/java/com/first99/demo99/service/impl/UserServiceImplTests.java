package com.first99.demo99.service.impl;

import com.first99.demo99.exception.EmailExistsException;
import com.first99.demo99.exception.InvalidCredentialsException;
import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import com.first99.demo99.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
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


    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        // Arrange
        User user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setName("User One");
        user1.setRoles(Set.of("USER"));

        User user2 = new User();
        user2.setEmail("admin@test.com");
        user2.setName("Admin User");
        user2.setRoles(Set.of("USER", "ADMIN"));

        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(user1, user2);

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    void makeAdmin_shouldAddAdminRoleAndSaveUser() {
        // -------- ARRANGE --------
        User user = new User();
        user.setEmail("test@example.com");
        user.setRoles(new HashSet<>(Set.of("USER")));


        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // -------- ACT --------
        User result = userService.makeAdmin("test@example.com");

        // -------- ASSERT --------
        assertNotNull(result);
        assertTrue(result.getRoles().contains("USER"));
        assertTrue(result.getRoles().contains("ADMIN"));
        assertEquals(2, result.getRoles().size());

        verify(userRepository, times(1))
                .findByEmail("test@example.com");
        verify(userRepository, times(1))
                .save(user);
    }

    @Test
    void makeAdmin_shouldThrowExceptionIfUserNotFound() {
        // -------- ARRANGE --------
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        // -------- ACT + ASSERT --------
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.makeAdmin("missing@example.com")
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1))
                .findByEmail("missing@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserProfile_shouldUpdateNameAndSaveUser() {
        // Arrange
        String email = "user@test.com";
        String newName = "Updated Name";

        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setName("Old Name");
        existingUser.setRoles(Set.of("USER"));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        // Act
        User result = userService.updateUserProfile(email, newName);

        // Assert
        assertThat(result.getName()).isEqualTo(newName);
        assertThat(result.getEmail()).isEqualTo(email);

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(existingUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUserProfile_shouldThrowException_whenUserNotFound() {
        // Arrange
        String email = "missing@test.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() ->
                userService.updateUserProfile(email, "New Name")
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any());
    }
}
