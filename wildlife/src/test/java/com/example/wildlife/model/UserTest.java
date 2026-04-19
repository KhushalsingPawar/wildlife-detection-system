package com.example.wildlife.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void testNoArgsConstructor() {
        User user = new User();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isNull();
    }

    @Test
    public void testTwoArgsConstructor() {
        User user = new User("John Doe", "john@example.com");

        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getPassword()).isNull();
    }

    @Test
    public void testThreeArgsConstructor() {
        User user = new User("Jane Doe", "jane@example.com", "password123");

        assertThat(user.getName()).isEqualTo("Jane Doe");
        assertThat(user.getEmail()).isEqualTo("jane@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();

        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("secret");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Alice");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        assertThat(user.getPassword()).isEqualTo("secret");
    }
}
