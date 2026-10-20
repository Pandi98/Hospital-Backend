package com.example.HospitalManSys2.controller;



import com.example.HospitalManSys2.dto.request.PatientRequest;
import com.example.HospitalManSys2.dto.response.ApiResponse;
import com.example.HospitalManSys2.dto.response.PatientResponse;
import com.example.HospitalManSys2.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponse>> registerPatient(
            @Valid @RequestBody PatientRequest request) {

        PatientResponse patient = patientService.registerPatient(request);

        return ResponseEntity.ok(
                ApiResponse.success("Patient registered successfully", patient)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Patient retrieved", patientService.getPatientById(id))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients() {

        return ResponseEntity.ok(
                ApiResponse.success("Patients retrieved", patientService.getAllPatients())
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> searchPatients(
            @RequestParam String name) {

        return ResponseEntity.ok(
                ApiResponse.success("Patients found", patientService.searchPatients(name))
        );
    }
}