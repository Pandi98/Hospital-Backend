package com.example.HospitalManSys2.service.impl;

import com.example.HospitalManSys2.dto.request.ReceptionistRequest;
import com.example.HospitalManSys2.dto.request.ReceptionistUpdateRequest;
import com.example.HospitalManSys2.dto.response.ReceptionistResponse;
import com.example.HospitalManSys2.entity.Role;
import com.example.HospitalManSys2.entity.User;
import com.example.HospitalManSys2.exception.DuplicateResourceException;
import com.example.HospitalManSys2.exception.ResourceNotFoundException;
import com.example.HospitalManSys2.repository.UserRepository;
import com.example.HospitalManSys2.service.ReceptionistService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceptionistServiceImpl
        implements ReceptionistService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ReceptionistResponse addReceptionist(
            ReceptionistRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "User already exists with email: "
                            + request.getEmail());
        }

        User receptionist = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .phone(request.getPhone())
                .role(Role.RECEPTIONIST)
                .active(true)
                .build();

        User saved = userRepository.save(receptionist);

        log.info("Receptionist added: {}",
                saved.getEmail());

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ReceptionistResponse updateReceptionist(
            Long id,
            ReceptionistUpdateRequest request) {

        User receptionist =
                userRepository.findByIdAndRole(
                                id,
                                Role.RECEPTIONIST)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receptionist",
                                        "id",
                                        id));

        if (request.getFullName() != null)
            receptionist.setFullName(
                    request.getFullName());

        if (request.getPhone() != null)
            receptionist.setPhone(
                    request.getPhone());

        if (request.getActive() != null)
            receptionist.setActive(
                    request.getActive());

        return mapToResponse(
                userRepository.save(receptionist));
    }

    @Override
    @Transactional
    public void deleteReceptionist(Long id) {

        User receptionist =
                userRepository.findByIdAndRole(id, Role.RECEPTIONIST)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receptionist",
                                        "id",
                                        id));

        receptionist.setActive(false);

        userRepository.save(receptionist);

        log.info("Receptionist deleted: {}",
                receptionist.getEmail());
    }

    @Override
    public ReceptionistResponse getReceptionistById(
            Long id) {

        User receptionist =
                userRepository.findByIdAndRole(
                                id,
                                Role.RECEPTIONIST)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receptionist",
                                        "id",
                                        id));

        return mapToResponse(receptionist);
    }

    @Override
    public List<ReceptionistResponse>
    getAllReceptionists() {

        return userRepository
                .findByRoleAndActiveTrue(Role.RECEPTIONIST)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ReceptionistResponse mapToResponse(
            User user) {

        return ReceptionistResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .active(user.isActive())
                .build();
    }
}