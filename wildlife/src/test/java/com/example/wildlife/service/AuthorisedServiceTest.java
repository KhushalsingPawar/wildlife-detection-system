package com.example.wildlife.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AuthorisedServiceTest {

    private AuthorisedService authorisedService = new AuthorisedService();

    @Test
    public void testAddAuthorised() {
        authorisedService.addAuthorised("Elephant");

        boolean isAuthorised = authorisedService.isAuthorised("elephant");
        assertThat(isAuthorised).isTrue();
    }

    @Test
    public void testRemoveAuthorised() {
        authorisedService.addAuthorised("Lion");
        authorisedService.removeAuthorised("LION");

        boolean isAuthorised = authorisedService.isAuthorised("lion");
        assertThat(isAuthorised).isFalse();
    }

    @Test
    public void testGetAll() {
        authorisedService.addAuthorised("Cat");
        authorisedService.addAuthorised("Dog");

        Set<String> all = authorisedService.getAll();
        assertThat(all).contains("cat", "dog");
    }

    @Test
    public void testIsAuthorised_CaseInsensitive() {
        authorisedService.addAuthorised("Bear");

        assertThat(authorisedService.isAuthorised("BEAR")).isTrue();
        assertThat(authorisedService.isAuthorised("bear")).isTrue();
    }

    @Test
    public void testIsAuthorised_NotAuthorised() {
        assertThat(authorisedService.isAuthorised("Tiger")).isFalse();
    }
}