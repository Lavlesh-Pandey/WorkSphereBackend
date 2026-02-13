package com.WorkSphere.WorkSphereBackend.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WorkSphere.WorkSphereBackend.config.JWTUtil;
import com.WorkSphere.WorkSphereBackend.dto.LoginRequestDto;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.repository.UsersRepository;


@Service
public class AuthService {
 
    @Autowired
    private UsersRepository userRepository;
 
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    @Autowired
    private JWTUtil jwtUtil;
 
    public Map<String, Object> login(LoginRequestDto request) {
 
        Users user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
 
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
 
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );
 
        return  Map.of(
                "token", token,
                "role", user.getRole(),
                "userId", user.getUserId()
        );
    }
}



