package com.smartcalculator.backend.service;

import com.smartcalculator.backend.dto.request.LoginRequest;
import com.smartcalculator.backend.dto.request.RegisterRequest;
import com.smartcalculator.backend.dto.response.AuthResponse;

public interface UserService {

    // Register new user
    AuthResponse register(RegisterRequest request);

    // Login user
    AuthResponse login(LoginRequest request);
}