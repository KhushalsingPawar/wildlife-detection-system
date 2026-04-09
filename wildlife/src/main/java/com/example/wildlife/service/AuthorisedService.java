package com.example.wildlife.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorisedService {

    private final Set<String> authorised = new HashSet<>();

    public void addAuthorised(String animal) {
        authorised.add(animal.toLowerCase());
    }

    public void removeAuthorised(String animal) {
        authorised.remove(animal.toLowerCase());
    }

    public Set<String> getAll() {
        return new HashSet<>(authorised);
    }

    public boolean isAuthorised(String animal) {
        return authorised.contains(animal.toLowerCase());
    }
}