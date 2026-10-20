package com.example.HospitalManSys2.repository;

import com.example.HospitalManSys2.entity.Medicine;
import com.example.HospitalManSys2.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Long> {
    List<Medicine> findByPrescription(Prescription prescription);

}
