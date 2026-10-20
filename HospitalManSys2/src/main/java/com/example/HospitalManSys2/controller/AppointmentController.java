package com.example.HospitalManSys2.controller;






import com.example.HospitalManSys2.dto.request.AppointmentRequest;
import com.example.HospitalManSys2.dto.response.ApiResponse;
import com.example.HospitalManSys2.dto.response.AppointmentResponse;
import com.example.HospitalManSys2.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
            @Valid @RequestBody AppointmentRequest request) {

        AppointmentResponse response = appointmentService.bookAppointment(request);

        return ResponseEntity.ok(
                ApiResponse.success("Appointment booked successfully", response)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {

        return ResponseEntity.ok(
                ApiResponse.success("Appointments retrieved",
                        appointmentService.getAllAppointments())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Appointment retrieved",
                        appointmentService.getAppointmentById(id))
        );
    }
}
