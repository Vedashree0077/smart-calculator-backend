package com.smartcalculator.backend.service.impl;

import com.smartcalculator.backend.dto.request.LoginRequest;
import com.smartcalculator.backend.dto.request.RegisterRequest;
import com.smartcalculator.backend.dto.response.AuthResponse;
import com.smartcalculator.backend.entity.User;
import com.smartcalculator.backend.repository.UserRepository;
import com.smartcalculator.backend.security.JwtUtil;
import com.smartcalculator.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {

        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthResponse(
                    "Username already exists",
                    null
            );
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());

        // Encode password before saving
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Save user
        userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(
                "User registered successfully",
                token
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        // Find user by username
        Optional<User> optionalUser =
                userRepository.findByUsername(request.getUsername());

        // Username not found
        if (optionalUser.isEmpty()) {
            return new AuthResponse(
                    "Invalid credentials",
                    null
            );
        }

        User user = optionalUser.get();

        // Check password using BCrypt
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            return new AuthResponse(
                    "Invalid credentials",
                    null
            );
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(
                "Login successful",
                token
        );
    }
}