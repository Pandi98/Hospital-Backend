package com.example.HospitalManSys2.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicineResponse {

    private Long id;
    private String medicineName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;

}
