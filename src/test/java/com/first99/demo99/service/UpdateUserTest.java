package com.first99.demo99.service;

import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class UpdateUserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

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
