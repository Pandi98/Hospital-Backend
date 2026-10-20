package com.example.HospitalManSys2.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceptionistResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private Boolean active;
}