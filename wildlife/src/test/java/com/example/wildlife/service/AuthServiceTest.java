package com.example.wildlife.service;

import com.example.wildlife.model.User;
import com.example.wildlife.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testSignup_Success() {
        User user = new User("John", "john@example.com", "password");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = authService.signup(user);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    public void testSignup_UserExists() {
        User user = new User("John", "john@example.com", "password");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.signup(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User already exists");
    }

    @Test
    public void testLogin_Success() {
        User user = new User("John", "john@example.com", "password");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        User result = authService.login("john@example.com", "password");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    public void testLogin_UserNotFound() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login("john@example.com", "password"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testLogin_InvalidPassword() {
        User user = new User("John", "john@example.com", "password");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login("john@example.com", "wrong"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid password");
    }
}