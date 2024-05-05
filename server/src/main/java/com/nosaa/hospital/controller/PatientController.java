package com.nosaa.hospital.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nosaa.hospital.domain.Patient;
import com.nosaa.hospital.service.PatientService;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class PatientController {

    @Autowired
	PatientService patientService;

    @GetMapping("/patients")
	public ResponseEntity<List<Patient>> getPatientsByParams(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int field,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
		try {
			List<Patient> patients = new ArrayList<Patient>();
            Page<Patient> pagePatient;
            Pageable paging = PageRequest.of(page, size);

            pagePatient = patientService.findAllPatientByKeyword(keyword, field, paging);

			if (pagePatient.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

            patients = pagePatient.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("patients", patients);
            response.put("currentPage", pagePatient.getNumber());
            response.put("totalItems", pagePatient.getTotalElements());
            response.put("totalPages", pagePatient.getTotalPages());

			return new ResponseEntity<>(patients, HttpStatus.OK);
		} catch (Exception e) {
            e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") long id) {
		
        Optional<Patient> patientData = patientService.findPatientById(id);
        
		if (patientData.isPresent()) {
			return new ResponseEntity<>(patientData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

    @PostMapping("/patients")
	public ResponseEntity<Patient> setPatient(@RequestBody Patient patient) {
		try {
			Patient _patient = patientService.upsertPatient(patient);
			return new ResponseEntity<>(_patient, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>( null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @DeleteMapping("/patients/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
		try {
			patientService.deletePatientById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
