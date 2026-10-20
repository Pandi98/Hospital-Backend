package com.example.HospitalManSys2.service;

import com.example.HospitalManSys2.dto.request.PrescriptionRequest;
import com.example.HospitalManSys2.dto.response.PrescriptionResponse;

import java.util.List;

public interface PrescriptionService {

    PrescriptionResponse addPrescription(String doctorEmail, PrescriptionRequest request);
    List<PrescriptionResponse> getByPatient(Long patientId);
    List<PrescriptionResponse> getByDoctor(String doctorEmail);
    PrescriptionResponse getById(Long id);

}
