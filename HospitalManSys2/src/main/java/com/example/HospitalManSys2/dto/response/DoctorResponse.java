package com.example.HospitalManSys2.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DoctorResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private String licenseNumber;
    private String department;
    private String consultationFee;
    private boolean available;
    private LocalDateTime createdAt;

}
