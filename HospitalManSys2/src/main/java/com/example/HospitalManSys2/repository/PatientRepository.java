package com.example.HospitalManSys2.repository;

import com.example.HospitalManSys2.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {

    Optional<Patient> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<Patient> findByActiveTrue();
    List<Patient> findByFullNameContainingIgnoreCase(String name);
}
