package com.example.wildlife.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wildlife.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}