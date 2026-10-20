package com.example.HospitalManSys2.service;

import com.example.HospitalManSys2.dto.request.LoginRequest;
import com.example.HospitalManSys2.dto.request.RegisterRequest;
import com.example.HospitalManSys2.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}
