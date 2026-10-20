package com.example.HospitalManSys2.service;

import com.example.HospitalManSys2.dto.request.PatientRequest;
import com.example.HospitalManSys2.dto.response.PatientResponse;

import java.util.List;

public interface PatientService {

    PatientResponse registerPatient(PatientRequest request);
    PatientResponse updatePatient(Long id, PatientRequest request);
    PatientResponse getPatientById(Long id);
    List<PatientResponse> getAllPatients();
    List<PatientResponse> searchPatients(String name);

}
