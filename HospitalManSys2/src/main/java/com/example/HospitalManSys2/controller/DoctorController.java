package com.example.HospitalManSys2.controller;


import com.example.HospitalManSys2.dto.response.*;
import com.example.HospitalManSys2.dto.request.PrescriptionRequest;
import com.example.HospitalManSys2.dto.response.ApiResponse;
import com.example.HospitalManSys2.dto.response.AppointmentResponse;
import com.example.HospitalManSys2.service.AppointmentService;
import com.example.HospitalManSys2.service.DoctorService;
import com.example.HospitalManSys2.service.PatientService;
import com.example.HospitalManSys2.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','DOCTOR')") // Only ADMIN and DOCTOR can access these endpoints
public class DoctorController {



    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<DoctorResponse>> getProfile(
            Authentication authentication) {

        DoctorResponse doctor =
                doctorService.getDoctorProfile(authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.success("Profile retrieved", doctor)
        );
    }






    // ─────────────────── APPOINTMENT MANAGEMENT ───────────────────

    /**
     * GET /api/doctor/appointments
     * Get all appointments assigned to the currently logged-in doctor
     */
    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<AppointmentResponse> appointments =
                appointmentService.getMyAppointments(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved", appointments));
    }

    /**
     * GET /api/doctor/appointments/{id}
     * Get a specific appointment by ID
     */
    @GetMapping("/appointments/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Appointment retrieved",
                appointmentService.getAppointmentById(id)));
    }

    /**
     * PUT /api/doctor/appointments/{id}/complete
     * Mark an appointment as COMPLETED
     */
    @PutMapping("/appointments/{id}/complete")
    public ResponseEntity<ApiResponse<AppointmentResponse>> completeAppointment(
            @PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.completeAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment marked as completed", appointment));
    }

    // ─────────────────── PRESCRIPTION MANAGEMENT ───────────────────

    /**
     * POST /api/doctor/prescriptions
     * Add a new prescription with medicines for an appointment
     * Doctor can only prescribe for their own appointments
     */
    @PostMapping("/prescriptions")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> addPrescription(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PrescriptionRequest request) {
        PrescriptionResponse prescription =
                prescriptionService.addPrescription(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Prescription added successfully", prescription));
    }

    /**
     * GET /api/doctor/prescriptions
     * Get all prescriptions written by the currently logged-in doctor
     */
    @GetMapping("/prescriptions")
    public ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getMyPrescriptions(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<PrescriptionResponse> prescriptions =
                prescriptionService.getByDoctor(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Prescriptions retrieved", prescriptions));
    }

    /**
     * GET /api/doctor/prescriptions/{id}
     * Get a specific prescription by ID
     */
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> getPrescriptionById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Prescription retrieved",
                prescriptionService.getById(id)));
    }

    // ─────────────────── PATIENT LOOKUP ───────────────────

    /**
     * GET /api/doctor/patients/{patientId}
     * View details of a specific patient
     */
    @GetMapping("/patients/{patientId}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success("Patient retrieved",
                patientService.getPatientById(patientId)));
    }

    /**
     * GET /api/doctor/patients/{patientId}/appointments
     * View all appointments for a specific patient
     */
    @GetMapping("/patients/{patientId}/appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getPatientAppointments(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success("Patient appointments retrieved",
                appointmentService.getAppointmentsByPatient(patientId)));
    }

    /**
     * GET /api/doctor/patients/{patientId}/prescriptions
     * View all prescriptions for a specific patient
     */
    @GetMapping("/patients/{patientId}/prescriptions")
    public ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getPatientPrescriptions(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success("Patient prescriptions retrieved",
                prescriptionService.getByPatient(patientId)));
    }


}
