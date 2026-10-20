package com.example.HospitalManSys2.service.impl;

import com.example.HospitalManSys2.dto.request.DoctorRequest;
import com.example.HospitalManSys2.dto.request.DoctorUpdateRequest;
import com.example.HospitalManSys2.dto.response.DoctorResponse;
import com.example.HospitalManSys2.entity.Doctor;
import com.example.HospitalManSys2.entity.Role;
import com.example.HospitalManSys2.entity.User;
import com.example.HospitalManSys2.exception.DuplicateResourceException;
import com.example.HospitalManSys2.exception.ResourceNotFoundException;
import com.example.HospitalManSys2.repository.DoctorRepository;
import com.example.HospitalManSys2.repository.UserRepository;
import com.example.HospitalManSys2.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {


    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public DoctorResponse addDoctor(DoctorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User already exists with email: " + request.getEmail());
        }
        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new DuplicateResourceException("Doctor already exists with license number: " + request.getLicenseNumber());
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.DOCTOR)
                .active(true)
                .build();
        User savedUser = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .user(savedUser)
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .licenseNumber(request.getLicenseNumber())
                .department(request.getDepartment())
                .consultationFee(request.getConsultationFee())
                .available(true)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Doctor added: {}", savedUser.getEmail());
        return mapToResponse(savedDoctor);
    }

    @Override
    public DoctorResponse getDoctorProfile(String email) {

        Doctor doctor = doctorRepository
                .findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor",
                                "email",
                                email));

        return mapToResponse(doctor);
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorUpdateRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        User user = doctor.getUser();

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        userRepository.save(user);

        if (request.getSpecialization() != null) doctor.setSpecialization(request.getSpecialization());
        if (request.getQualification() != null) doctor.setQualification(request.getQualification());
        if (request.getExperienceYears() != null) doctor.setExperienceYears(request.getExperienceYears());
        if (request.getDepartment() != null) doctor.setDepartment(request.getDepartment());
        if (request.getConsultationFee() != null) doctor.setConsultationFee(request.getConsultationFee());
        if (request.getAvailable() != null) doctor.setAvailable(request.getAvailable());

        return mapToResponse(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor",
                                "id",
                                id));
        doctor.getUser().setActive(false);
        doctor.setAvailable(false);
        userRepository.save(doctor.getUser());
        doctorRepository.save(doctor);
        log.info("Doctor soft-deleted: {}", id);
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        return mapToResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAllActiveDoctors().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> getAvailableDoctors() {
        return doctorRepository.findByAvailableTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DoctorResponse mapToResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUser().getId())
                .fullName(doctor.getUser().getFullName())
                .email(doctor.getUser().getEmail())
                .phone(doctor.getUser().getPhone())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .licenseNumber(doctor.getLicenseNumber())
                .department(doctor.getDepartment())
                .consultationFee(doctor.getConsultationFee())
                .available(doctor.isAvailable())
                .createdAt(doctor.getCreatedAt())
                .build();
    }

}
