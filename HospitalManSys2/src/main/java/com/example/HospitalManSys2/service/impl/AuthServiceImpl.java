package com.example.HospitalManSys2.service.impl;




import com.example.HospitalManSys2.entity.Doctor;
import com.example.HospitalManSys2.entity.Role;
import com.example.HospitalManSys2.repository.DoctorRepository;


import com.example.HospitalManSys2.dto.request.LoginRequest;
import com.example.HospitalManSys2.dto.request.RegisterRequest;
import com.example.HospitalManSys2.dto.response.AuthResponse;
import com.example.HospitalManSys2.entity.User;
import com.example.HospitalManSys2.exception.DuplicateResourceException;
import com.example.HospitalManSys2.repository.UserRepository;
import com.example.HospitalManSys2.security.JwtService;
import com.example.HospitalManSys2.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User already exists with email: " + request.getEmail());
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == Role.DOCTOR) {

            Doctor doctor = Doctor.builder()
                    .user(savedUser)
                    .specialization("General")
                    .qualification("MBBS")
                    .experienceYears(1)
                    .licenseNumber("TEMP-" + savedUser.getId())
                    .available(true)
                    .build();

            doctorRepository.save(doctor);
        }


        String token = jwtService.generateToken(savedUser);

        log.info("User registered successfully: {}", savedUser.getEmail());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .message("User registered successfully")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        log.info("User logged in: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .message("Login successful")
                .build();
    }

}
