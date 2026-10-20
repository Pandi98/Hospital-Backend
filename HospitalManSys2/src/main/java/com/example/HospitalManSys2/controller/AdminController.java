package com.example.HospitalManSys2.controller;

import com.example.HospitalManSys2.dto.request.ReceptionistRequest;
import com.example.HospitalManSys2.dto.request.ReceptionistUpdateRequest;
import com.example.HospitalManSys2.dto.response.*;
import com.example.HospitalManSys2.dto.request.DoctorRequest;
import com.example.HospitalManSys2.dto.request.DoctorUpdateRequest;
import com.example.HospitalManSys2.dto.response.ApiResponse;
import com.example.HospitalManSys2.dto.response.DoctorResponse;
import com.example.HospitalManSys2.service.AppointmentService;
import com.example.HospitalManSys2.service.DoctorService;
import com.example.HospitalManSys2.service.PatientService;
import com.example.HospitalManSys2.service.ReceptionistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Only users with ADMIN role can access these endpoints
public class AdminController {


    private final DoctorService doctorService;
    private final ReceptionistService receptionistService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    // ─────────────────── DOCTOR MANAGEMENT ───────────────────

    /**
     * POST /api/admin/doctors
     * Add a new doctor (also creates a User account with DOCTOR role)
     */
    @PostMapping("/doctors")
    public ResponseEntity<ApiResponse<DoctorResponse>> addDoctor(
            @Valid @RequestBody DoctorRequest request) {
        DoctorResponse doctor = doctorService.addDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor added successfully", doctor));
    }

    /**
     * PUT /api/admin/doctors/{id}
     * Update doctor details
     */
    @PutMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorUpdateRequest request) {
        DoctorResponse doctor = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(ApiResponse.success("Doctor updated successfully", doctor));
    }

    /**
     * DELETE /api/admin/doctors/{id}
     * Soft-delete a doctor (deactivates account, marks unavailable)
     */
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor deleted successfully"));
    }

    /**
     * GET /api/admin/doctors
     * Retrieve all active doctors
     */
    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved successfully",
                doctorService.getAllDoctors()));
    }

    /**
     * GET /api/admin/doctors/{id}
     * Retrieve a single doctor by ID
     */
    @GetMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Doctor retrieved",
                doctorService.getDoctorById(id)));
    }


    // ─────────────────── RECEPTIONIST MANAGEMENT ───────────────────

    @PostMapping("/receptionists")
    public ResponseEntity<ApiResponse<ReceptionistResponse>> addReceptionist(
            @Valid @RequestBody ReceptionistRequest request) {

        ReceptionistResponse receptionist =
                receptionistService.addReceptionist(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Receptionist added successfully",
                        receptionist));
    }

    @PutMapping("/receptionists/{id}")
    public ResponseEntity<ApiResponse<ReceptionistResponse>> updateReceptionist(
            @PathVariable Long id,
            @Valid @RequestBody ReceptionistUpdateRequest request) {

        ReceptionistResponse receptionist =
                receptionistService.updateReceptionist(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receptionist updated successfully",
                        receptionist));
    }

    @DeleteMapping("/receptionists/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReceptionist(
            @PathVariable Long id) {

        receptionistService.deleteReceptionist(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receptionist deleted successfully"));
    }

    @GetMapping("/receptionists")
    public ResponseEntity<ApiResponse<List<ReceptionistResponse>>> getAllReceptionists() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receptionists retrieved successfully",
                        receptionistService.getAllReceptionists()));
    }

    @GetMapping("/receptionists/{id}")
    public ResponseEntity<ApiResponse<ReceptionistResponse>> getReceptionistById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receptionist retrieved",
                        receptionistService.getReceptionistById(id)));
    }




    // ─────────────────── PATIENT MANAGEMENT ───────────────────

    /**
     * GET /api/admin/patients
     * Retrieve all registered patients
     */
    @GetMapping("/patients")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients() {
        return ResponseEntity.ok(ApiResponse.success("Patients retrieved successfully",
                patientService.getAllPatients()));
    }

    /**
     * GET /api/admin/patients/{id}
     * Retrieve a patient by ID
     */
    @GetMapping("/patients/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Patient retrieved",
                patientService.getPatientById(id)));
    }

    // ─────────────────── APPOINTMENT MANAGEMENT ───────────────────

    /**
     * GET /api/admin/appointments
     * Retrieve all appointments (ordered by date/time desc)
     */
    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved successfully",
                appointmentService.getAllAppointments()));
    }

    /**
     * GET /api/admin/appointments/{id}
     * Retrieve a single appointment by ID
     */
    @GetMapping("/appointments/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Appointment retrieved",
                appointmentService.getAppointmentById(id)));
    }

    /**
     * GET /api/admin/appointments/doctor/{doctorId}
     * Retrieve all appointments for a specific doctor
     */
    @GetMapping("/appointments/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.success("Doctor appointments retrieved",
                appointmentService.getAppointmentsByDoctor(doctorId)));
    }

    /**
     * GET /api/admin/appointments/patient/{patientId}
     * Retrieve all appointments for a specific patient
     */
    @GetMapping("/appointments/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success("Patient appointments retrieved",
                appointmentService.getAppointmentsByPatient(patientId)));
    }


}
