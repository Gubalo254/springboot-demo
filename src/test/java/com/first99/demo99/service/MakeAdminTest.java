package com.first99.demo99.service;


import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MakeAdminTest {

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserServiceImpl userService;

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
}
