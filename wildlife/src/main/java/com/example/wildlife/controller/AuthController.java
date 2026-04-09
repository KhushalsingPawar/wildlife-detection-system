package com.example.wildlife.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wildlife.config.JwtUtil;
import com.example.wildlife.dto.LoginResponse;
import com.example.wildlife.model.User;
import com.example.wildlife.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return service.signup(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody User user) {
        User u = service.login(user.getEmail(), user.getPassword());
        String token = jwtUtil.generateToken(u.getEmail(), u.getId());
        return new LoginResponse(token, u.getId(), u.getName(), u.getEmail());
    }
}