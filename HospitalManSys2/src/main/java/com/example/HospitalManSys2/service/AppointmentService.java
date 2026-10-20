package com.example.HospitalManSys2.service;

import com.example.HospitalManSys2.dto.request.AppointmentRequest;
import com.example.HospitalManSys2.dto.response.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse bookAppointment(AppointmentRequest request);
    AppointmentResponse assignDoctor(Long appointmentId, Long doctorId);
    AppointmentResponse completeAppointment(Long id);
    AppointmentResponse cancelAppointment(Long id);
    AppointmentResponse getAppointmentById(Long id);
    List<AppointmentResponse> getAllAppointments();
    List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId);
    List<AppointmentResponse> getAppointmentsByPatient(Long patientId);
    List<AppointmentResponse> getMyAppointments(String doctorEmail);
}
