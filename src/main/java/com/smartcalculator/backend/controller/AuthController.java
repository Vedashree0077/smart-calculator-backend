package com.smartcalculator.backend.controller;

import com.smartcalculator.backend.dto.request.LoginRequest;
import com.smartcalculator.backend.dto.request.RegisterRequest;
import com.smartcalculator.backend.dto.response.AuthResponse;
import com.smartcalculator.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}