package com.example.HospitalManSys2.repository;

import com.example.HospitalManSys2.entity.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepository
        extends JpaRepository<Receptionist, Long> {
}