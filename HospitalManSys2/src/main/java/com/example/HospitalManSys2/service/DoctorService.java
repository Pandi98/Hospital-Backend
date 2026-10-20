package com.example.HospitalManSys2.service;

import com.example.HospitalManSys2.dto.request.DoctorRequest;
import com.example.HospitalManSys2.dto.request.DoctorUpdateRequest;
import com.example.HospitalManSys2.dto.response.DoctorResponse;

import java.util.List;

public interface DoctorService {

    DoctorResponse addDoctor(DoctorRequest request);
    DoctorResponse updateDoctor(Long id, DoctorUpdateRequest request);
    void deleteDoctor(Long id);
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getAllDoctors();
    List<DoctorResponse> getAvailableDoctors();
    DoctorResponse getDoctorProfile(String email);

}
