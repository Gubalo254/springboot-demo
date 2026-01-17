package com.first99.demo99.service;

import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllUsersTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

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
}
