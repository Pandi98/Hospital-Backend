package com.example.HospitalManSys2.dto.request;



import lombok.Data;

@Data
public class ReceptionistUpdateRequest {

    private String fullName;

    private String phone;

    private Boolean active;
}