package com.example.HospitalManSys2.controller;

import com.example.HospitalManSys2.dto.request.LoginRequest;
import com.example.HospitalManSys2.dto.request.RegisterRequest;
import com.example.HospitalManSys2.dto.response.ApiResponse;
import com.example.HospitalManSys2.dto.response.AuthResponse;
import com.example.HospitalManSys2.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Public endpoint to register a new user (ADMIN, RECEPTIONIST, or DOCTOR account)
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    /**
     * POST /api/auth/login
     * Public endpoint to authenticate and receive JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }


}
