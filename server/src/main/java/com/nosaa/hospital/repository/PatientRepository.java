package com.nosaa.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nosaa.hospital.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Page<Patient> findByPatientId(String patientId, Pageable pageable);
    Page<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
}
