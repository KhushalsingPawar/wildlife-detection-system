package com.example.wildlife.service;

import com.example.wildlife.model.User;
import com.example.wildlife.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    // SIGNUP
    public User signup(User user) {

        // check if email exists
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        return userRepo.save(user);
    }

    // LOGIN
    public User login(String email, String password) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}