package com.example.HospitalManSys2.service;

import com.example.HospitalManSys2.dto.request.ReceptionistRequest;
import com.example.HospitalManSys2.dto.response.ReceptionistResponse;

import java.util.List;



import com.example.HospitalManSys2.dto.request.ReceptionistRequest;
import com.example.HospitalManSys2.dto.request.ReceptionistUpdateRequest;
import com.example.HospitalManSys2.dto.response.ReceptionistResponse;

import java.util.List;

public interface ReceptionistService {

    ReceptionistResponse addReceptionist(
            ReceptionistRequest request);

    ReceptionistResponse updateReceptionist(
            Long id,
            ReceptionistUpdateRequest request);

    void deleteReceptionist(Long id);

    ReceptionistResponse getReceptionistById(Long id);

    List<ReceptionistResponse> getAllReceptionists();
}