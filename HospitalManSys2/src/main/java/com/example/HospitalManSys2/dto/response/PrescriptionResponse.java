package com.example.HospitalManSys2.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class PrescriptionResponse {

    private Long id;
    private Long appointmentId;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String diagnosis;
    private String instructions;
    private String followUpDate;
    private LocalDateTime createdAt;
    private List<MedicineResponse> medicines;

}
