package com.example.HospitalManSys2.service.impl;

import com.example.HospitalManSys2.dto.request.PrescriptionRequest;
import com.example.HospitalManSys2.dto.response.MedicineResponse;
import com.example.HospitalManSys2.dto.response.PrescriptionResponse;
import com.example.HospitalManSys2.entity.*;
import com.example.HospitalManSys2.exception.ResourceNotFoundException;
import com.example.HospitalManSys2.exception.UnAuthorizedException;
import com.example.HospitalManSys2.exception.UnAuthorizedException;
import com.example.HospitalManSys2.repository.*;
import com.example.HospitalManSys2.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public PrescriptionResponse addPrescription(String doctorEmail, PrescriptionRequest request) {
        Doctor doctor = doctorRepository.findByUser_Email(doctorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found for email: " + doctorEmail));

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", request.getAppointmentId()));

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new UnAuthorizedException("You are not assigned to this appointmenrt");

        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot add prescription to a completed appointment");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot add prescription to a cancelled appointment");
        }

        Prescription prescription = Prescription.builder()
                .appointment(appointment)
                .doctor(doctor)
                .patient(appointment.getPatient())
                .diagnosis(request.getDiagnosis())
                .instructions(request.getInstructions())
                .followUpDate(request.getFollowUpDate())
                .build();

        Prescription saved = prescriptionRepository.save(prescription);

        List<Medicine> medicines = request.getMedicines().stream().map(m ->
                Medicine.builder()
                        .prescription(saved)
                        .medicineName(m.getMedicineName())
                        .dosage(m.getDosage())
                        .frequency(m.getFrequency())
                        .duration(m.getDuration())
                        .instructions(m.getInstructions())
                        .build()
        ).collect(Collectors.toList());

        List<Medicine> savedMedicines = medicineRepository.saveAll(medicines);
        saved.setMedicines(savedMedicines);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        log.info("Prescription added for appointment: {}, by doctor: {}", appointment.getId(), doctor.getId());
        return mapToResponse(saved);





    }

    @Override
    public List<PrescriptionResponse> getByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        return prescriptionRepository.findByPatient(patient).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponse> getByDoctor(String doctorEmail) {
        Doctor doctor = doctorRepository.findByUser_Email(doctorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found for email: " + doctorEmail));
        return prescriptionRepository.findByDoctor(doctor).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionResponse getById(Long id) {
        return mapToResponse(prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", "id", id)));
    }

    private PrescriptionResponse mapToResponse(Prescription p) {
        List<MedicineResponse> medicines = (p.getMedicines() == null) ? List.of() :
                p.getMedicines().stream().map(m -> MedicineResponse.builder()
                        .id(m.getId())
                        .medicineName(m.getMedicineName())
                        .dosage(m.getDosage())
                        .frequency(m.getFrequency())
                        .duration(m.getDuration())
                        .instructions(m.getInstructions())
                        .build()
                ).collect(Collectors.toList());

        return PrescriptionResponse.builder()
                .id(p.getId())
                .appointmentId(p.getAppointment().getId())
                .doctorId(p.getDoctor().getId())
                .doctorName(p.getDoctor().getUser().getFullName())
                .patientId(p.getPatient().getId())
                .patientName(p.getPatient().getFullName())
                .diagnosis(p.getDiagnosis())
                .instructions(p.getInstructions())
                .followUpDate(p.getFollowUpDate())
                .createdAt(p.getCreatedAt())
                .medicines(medicines)
                .build();
    }
}
