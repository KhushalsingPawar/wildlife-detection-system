package com.example.wildlife.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorisedAnimalTest {

    @Test
    public void testNoArgsConstructor() {
        AuthorisedAnimal animal = new AuthorisedAnimal();

        assertThat(animal).isNotNull();
        assertThat(animal.getId()).isNull();
        assertThat(animal.getName()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        AuthorisedAnimal animal = new AuthorisedAnimal(1L, "Elephant");

        assertThat(animal.getId()).isEqualTo(1L);
        assertThat(animal.getName()).isEqualTo("Elephant");
    }

    @Test
    public void testSettersAndGetters() {
        AuthorisedAnimal animal = new AuthorisedAnimal();

        animal.setId(2L);
        animal.setName("Lion");

        assertThat(animal.getId()).isEqualTo(2L);
        assertThat(animal.getName()).isEqualTo("Lion");
    }
}