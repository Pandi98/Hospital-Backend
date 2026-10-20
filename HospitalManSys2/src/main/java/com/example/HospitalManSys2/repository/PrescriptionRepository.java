package com.example.HospitalManSys2.repository;

import com.example.HospitalManSys2.entity.Appointment;
import com.example.HospitalManSys2.entity.Doctor;
import com.example.HospitalManSys2.entity.Patient;
import com.example.HospitalManSys2.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {

    List<Prescription> findByPatient(Patient patient);
    List<Prescription> findByDoctor(Doctor doctor);
    List<Prescription> findByAppointment(Appointment appointment);
}
