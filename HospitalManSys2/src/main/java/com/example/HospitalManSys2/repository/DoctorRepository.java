package com.example.HospitalManSys2.repository;

import com.example.HospitalManSys2.entity.Doctor;
import com.example.HospitalManSys2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Optional<Doctor> findByUser_Email(String email);
    List<Doctor> findByAvailableTrue();
    boolean existsByLicenseNumber(String licenseNumber);
    @Query("SELECT d FROM Doctor d WHERE d.user.active = true")
    List<Doctor> findAllActiveDoctors();
    Optional<Doctor> findByUserEmail(String email);
    Optional<Doctor> findByUser(User user);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findBySpecialization(String specialization);




}
