package com.example.wildlife.controller;

import com.example.wildlife.model.AuthorisedAnimal;
import com.example.wildlife.repository.AuthorisedAnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/authorised")
public class AuthorisedController {

    @Autowired
    private AuthorisedAnimalRepository repo;

    @PostMapping("/add")
    public String addAuthorised(@RequestParam String animal) {
        String name = animal.toLowerCase();
        if (!repo.existsByName(name)) {
            repo.save(new AuthorisedAnimal(null, name));
        }
        return name + " added to authorised list";
    }

    @PostMapping("/remove")
    public String removeAuthorised(@RequestParam String animal) {
        String name = animal.toLowerCase();
        repo.deleteByNameIgnoreCase(name); // Use fixed method
        return name + " removed from authorised list";
    }

    @GetMapping("/all")
    public Set<String> getAll() {
        return new HashSet<>(repo.findAll().stream().map(AuthorisedAnimal::getName).toList());
    }
}