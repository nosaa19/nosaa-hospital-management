package com.nosaa.hospital.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nosaa.hospital.domain.DomainConstants;
import com.nosaa.hospital.domain.Patient;
import com.nosaa.hospital.repository.PatientRepository;


@Service
public class PatientService {
    
    @Autowired
    PatientRepository patientRepository;

    public Page<Patient> findAllPatientByKeyword(String keyword, int field, Pageable pageable) {

        Page<Patient> patientPage = new PageImpl<>(Collections.emptyList());
        
        // check keyword and field
        switch (field) {
            case DomainConstants.PATIENT_ID_FIELD:
                System.out.println(DomainConstants.PATIENT_ID_FIELD);
                patientPage = patientRepository.findByPatientId(keyword, pageable);
                break;
            case DomainConstants.PATIENT_NAME_FIELD:
                System.out.println(DomainConstants.PATIENT_NAME_FIELD);
                patientPage = patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword, pageable);
                break;
            default:
                patientPage = patientRepository.findAll(pageable);
                break;
        }

        return patientPage;
    }

    public Patient upsertPatient(Patient patient) throws Exception {

        Optional<Patient> getPatient = patientRepository.findById(patient.getId());
        Patient upsertPatient = new Patient();

        if(getPatient.isPresent()){
            upsertPatient = getPatient.get();
            upsertPatient.setPatientId(patient.getPatientId());
            upsertPatient.setFirstName(patient.getFirstName());
            upsertPatient.setLastName(patient.getLastName());
            upsertPatient.setDob(patient.getDob());
            upsertPatient.setAddress(patient.getAddress());
            upsertPatient.setPhoneNumber(patient.getPhoneNumber());
            upsertPatient = patientRepository.save(upsertPatient);
        }
        else {
            upsertPatient.setPatientId(patient.getPatientId());
            upsertPatient.setFirstName(patient.getFirstName());
            upsertPatient.setLastName(patient.getLastName());
            upsertPatient.setDob(patient.getDob());
            upsertPatient.setAddress(patient.getAddress());
            upsertPatient.setPhoneNumber(patient.getPhoneNumber());
            upsertPatient = patientRepository.save(upsertPatient);

        }
        return upsertPatient;
    }

    public void deletePatient(long id) throws Exception{
        patientRepository.deleteById(id);
    }
}