package com.example.wildlife.repository;

import com.example.wildlife.model.User; // Assuming a User entity exists
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@SpringJUnitConfig
@DisplayName("User Repository Tests")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should save and find user by ID")
    void shouldSaveAndFindUserById() {
        User user = new User("kp", "khushal@gmail.com");
        User savedUser = entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("kp");
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        User user = new User("Jane Doe", "jane@example.com");
        entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findByEmail("jane@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void shouldReturnEmptyWhenUserNotFound() {
        Optional<User> foundUser = userRepository.findById(999L);

        assertThat(foundUser).isEmpty();
    }
}
