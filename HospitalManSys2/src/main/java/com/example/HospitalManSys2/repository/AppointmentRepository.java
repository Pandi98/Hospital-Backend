package com.example.HospitalManSys2.repository;

import com.example.HospitalManSys2.entity.Appointment;
import com.example.HospitalManSys2.entity.AppointmentStatus;
import com.example.HospitalManSys2.entity.Doctor;
import com.example.HospitalManSys2.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByDoctorAndStatus(Doctor doctor, AppointmentStatus status);
    List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);
    List<Appointment> findByStatus(AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND " +
            "a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorAndDateRange(
            @Param("doctor") Doctor doctor,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT a FROM Appointment a ORDER BY a.appointmentDateTime DESC")
    List<Appointment> findAllOrderByDateTime();

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = 'SCHEDULED'")
    List<Appointment> findScheduledByDoctorId(@Param("doctorId") Long doctorId);
}
