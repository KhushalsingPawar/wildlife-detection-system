package com.example.wildlife.dto;

/** Returned by POST /api/auth/login — includes JWT for Authorization: Bearer */
public record LoginResponse(String token, Long id, String name, String email) {}
