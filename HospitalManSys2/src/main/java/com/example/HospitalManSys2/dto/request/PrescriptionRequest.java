package com.example.HospitalManSys2.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    private String diagnosis;
    private String instructions;
    private String followUpDate;

    @NotEmpty(message = "At least one medicine is required")
    @Valid
    private List<MedicineRequest> medicines;

}
