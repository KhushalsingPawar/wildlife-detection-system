package com.example.wildlife.repository;

import com.example.wildlife.model.AuthorisedAnimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthorisedAnimalRepositoryTest {

    @Autowired
    private AuthorisedAnimalRepository authorisedAnimalRepository;

    @Test
    public void testSaveAuthorisedAnimal() {
        AuthorisedAnimal animal = new AuthorisedAnimal();
        animal.setName("Elephant");

        AuthorisedAnimal saved = authorisedAnimalRepository.save(animal);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Elephant");
    }

    @Test
    public void testExistsByName() {
        AuthorisedAnimal animal = new AuthorisedAnimal();
        animal.setName("lion");
        authorisedAnimalRepository.save(animal);

        boolean exists = authorisedAnimalRepository.existsByName("lion");
        assertThat(exists).isTrue();

        boolean notExists = authorisedAnimalRepository.existsByName("tiger");
        assertThat(notExists).isFalse();
    }

    @Test
    public void testDeleteByNameIgnoreCase() {
        AuthorisedAnimal animal = new AuthorisedAnimal();
        animal.setName("Bear");
        authorisedAnimalRepository.save(animal);

        authorisedAnimalRepository.deleteByNameIgnoreCase("BEAR");

        boolean exists = authorisedAnimalRepository.existsByName("bear");
        assertThat(exists).isFalse();
    }

    @Test
    public void testFindAll() {
        AuthorisedAnimal cat = new AuthorisedAnimal();
        cat.setName("Cat");
        authorisedAnimalRepository.save(cat);

        AuthorisedAnimal dog = new AuthorisedAnimal();
        dog.setName("Dog");
        authorisedAnimalRepository.save(dog);

        var all = authorisedAnimalRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
    }
}
