package com.example.HospitalManSys2.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DoctorUpdateRequest {

    private String fullName;
    private String phone;
    private String specialization;
    private String qualification;
    @Min(value = 0, message = "Experience years must be non-negative")
    private Integer experienceYears;
    private String department;
    private String consultationFee;
    private Boolean available;

}
