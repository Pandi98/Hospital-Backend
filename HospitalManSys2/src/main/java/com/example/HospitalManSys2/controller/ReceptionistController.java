package com.example.HospitalManSys2.controller;

import com.example.HospitalManSys2.dto.response.*;
import com.example.HospitalManSys2.dto.request.AppointmentRequest;
import com.example.HospitalManSys2.dto.request.PatientRequest;
import com.example.HospitalManSys2.dto.response.ApiResponse;
import com.example.HospitalManSys2.dto.response.PatientResponse;
import com.example.HospitalManSys2.service.AppointmentService;
import com.example.HospitalManSys2.service.DoctorService;
import com.example.HospitalManSys2.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/receptionist")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

public class ReceptionistController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    // ─────────────────── PATIENT MANAGEMENT ───────────────────

    /**
     * POST /api/receptionist/patients
     * Register a new patient
     */
    @PostMapping("/patients")
    public ResponseEntity<ApiResponse<PatientResponse>> registerPatient(
            @Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.registerPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Patient registered successfully", patient));
    }

    /**
     * PUT /api/receptionist/patients/{id}
     * Update patient details
     */
    @PutMapping("/patients/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.updatePatient(id, request);
        return ResponseEntity.ok(ApiResponse.success("Patient updated successfully", patient));
    }

    /**
     * GET /api/receptionist/patients
     * List all patients, optionally filtered by name search
     * ?search=John
     */
    @GetMapping("/patients")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients(
            @RequestParam(required = false) String search) {
        List<PatientResponse> patients = (search != null && !search.isBlank())
                ? patientService.searchPatients(search)
                : patientService.getAllPatients();
        return ResponseEntity.ok(ApiResponse.success("Patients retrieved", patients));
    }

    /**
     * GET /api/receptionist/patients/{id}
     * Get a single patient by ID
     */
    @GetMapping("/patients/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Patient retrieved",
                patientService.getPatientById(id)));
    }

    // ─────────────────── APPOINTMENT MANAGEMENT ───────────────────

    /**
     * POST /api/receptionist/appointments
     * Book a new appointment for a patient with a doctor
     */
    @PostMapping("/appointments")
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
            @Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse appointment = appointmentService.bookAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment booked successfully", appointment));
    }

    /**
     * PUT /api/receptionist/appointments/{appointmentId}/assign-doctor/{doctorId}
     * Assign or reassign a doctor to an existing appointment
     */
    @PutMapping("/appointments/{appointmentId}/assign-doctor/{doctorId}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> assignDoctor(
            @PathVariable Long appointmentId,
            @PathVariable Long doctorId) {
        AppointmentResponse appointment = appointmentService.assignDoctor(appointmentId, doctorId);
        return ResponseEntity.ok(ApiResponse.success("Doctor assigned successfully", appointment));
    }

    /**
     * PUT /api/receptionist/appointments/{id}/cancel
     * Cancel a scheduled appointment
     */
    @PutMapping("/appointments/{id}/cancel")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled", appointment));
    }

    /**
     * GET /api/receptionist/appointments
     * View all appointments
     */
    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved",
                appointmentService.getAllAppointments()));
    }

    /**
     * GET /api/receptionist/appointments/{id}
     * Get a single appointment by ID
     */
    @GetMapping("/appointments/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Appointment retrieved",
                appointmentService.getAppointmentById(id)));
    }

    // ─────────────────── DOCTOR LOOKUP ───────────────────

    /**
     * GET /api/receptionist/doctors
     * View all active doctors
     */
    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved",
                doctorService.getAllDoctors()));
    }

    /**
     * GET /api/receptionist/doctors/available
     * View only available doctors for booking
     */
    @GetMapping("/doctors/available")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAvailableDoctors() {
        return ResponseEntity.ok(ApiResponse.success("Available doctors retrieved",
                doctorService.getAvailableDoctors()));
    }

}
