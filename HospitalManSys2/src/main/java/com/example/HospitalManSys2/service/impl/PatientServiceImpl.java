package com.example.HospitalManSys2.service.impl;

import com.example.HospitalManSys2.dto.request.PatientRequest;
import com.example.HospitalManSys2.dto.response.PatientResponse;
import com.example.HospitalManSys2.entity.Patient;
import com.example.HospitalManSys2.exception.DuplicateResourceException;
import com.example.HospitalManSys2.exception.ResourceNotFoundException;
import com.example.HospitalManSys2.repository.PatientRepository;
import com.example.HospitalManSys2.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public PatientResponse registerPatient(PatientRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Patient already exists with email: " + request.getEmail());
        }

        Patient patient = Patient.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .bloodGroup(request.getBloodGroup())
                .allergies(request.getAllergies())
                .medicalHistory(request.getMedicalHistory())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .active(true)
                .build();

        Patient saved = patientRepository.save(patient);
        log.info("Patient registered: {}", saved.getEmail());
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

        patient.setFullName(request.getFullName());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAllergies(request.getAllergies());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());

        return mapToResponse(patientRepository.save(patient));
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        return mapToResponse(patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id)));
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponse> searchPatients(String name) {
        return patientRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse mapToResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .bloodGroup(patient.getBloodGroup())
                .allergies(patient.getAllergies())
                .medicalHistory(patient.getMedicalHistory())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .active(patient.isActive())
                .createdAt(patient.getCreatedAt())
                .build();
    }

}
