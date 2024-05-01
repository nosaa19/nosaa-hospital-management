package com.nosaa.hospital.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosaa.hospital.domain.DomainConstants;
import com.nosaa.hospital.domain.Patient;
import com.nosaa.hospital.repository.PatientRepository;
import com.nosaa.hospital.service.PatientService;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PatientController.class)
public class PatientControllerTests {

    @MockBean
    PatientService patientService;

    @MockBean
    PatientRepository patientRepository;
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient patientDTO;

    private Page<Patient> pagePatientDTO;

    @BeforeEach
    void setUp() {
        System.out.println("setUp");
        patientDTO = new Patient(
            1, 
            "12345", 
            "unit", 
            "test", 
            LocalDate.of(2020, 1, 8), 
            "A Street", 
            "+61888888888"
        );

        List <Patient> patientList = new ArrayList<>(); 
        patientList.add(patientDTO);
        pagePatientDTO = new PageImpl<>(patientList);
    }

    @Test
    public void testAPIGetPatientById() throws Exception {

        long id = 1;

        // given
        doReturn(Optional.of(patientDTO)).when(patientService).findPatientById(id);

        // when
        ResultActions response = mockMvc.perform(get("/api/patients/{id}", id));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(patientDTO.getId()), Long.class))
                .andExpect(jsonPath("$.patientId", is(patientDTO.getPatientId())))
                .andExpect(jsonPath("$.firstName", is(patientDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(patientDTO.getLastName())))
                .andExpect(jsonPath("$.dob", is(patientDTO.getDob().toString())))
                .andExpect(jsonPath("$.address", is(patientDTO.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(patientDTO.getPhoneNumber())));
    }

    @Test
    public void testAPIGetPatients() throws Exception {
        
        // given
        Page<Patient> expectedPatientPage = pagePatientDTO;
        doReturn(expectedPatientPage)
            .when(patientService)
            .findAllPatientByKeyword(null, 0, PageRequest.of(0, 3, Sort.unsorted()));

        // when
        ResultActions response = mockMvc.perform(get("/api/patients"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(pagePatientDTO.getContent().size())));
    }

    @Test
    public void testAPIGetPatientsByKeywordAndFieldPatientId() throws Exception {
        
        // given
        String keyword = "123";
        String field = Integer.toString(DomainConstants.PATIENT_ID_FIELD);
        Page<Patient> expectedPatientPage = pagePatientDTO;
        doReturn(expectedPatientPage)
            .when(patientService)
            .findAllPatientByKeyword(keyword, Integer.parseInt(field), PageRequest.of(0, 3, Sort.unsorted()));

        // when
        ResultActions response = mockMvc.perform(get("/api/patients")
                                                .param("keyword", keyword)
                                                .param("field", field));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(pagePatientDTO.getContent().size())));
    }

    @Test
    public void testAPIGetPatientsByKeywordAndFieldName() throws Exception {
        
        // given
        String keyword = "123";
        String field = Integer.toString(DomainConstants.PATIENT_NAME_FIELD);
        Page<Patient> expectedPatientPage = pagePatientDTO;
        doReturn(expectedPatientPage)
            .when(patientService)
            .findAllPatientByKeyword(keyword, Integer.parseInt(field), PageRequest.of(0, 3, Sort.unsorted()));

        // when
        ResultActions response = mockMvc.perform(get("/api/patients")
                                                .param("keyword", keyword)
                                                .param("field", field));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(pagePatientDTO.getContent().size())));
    }

    @Test
    public void testAPISetPatient() throws Exception {
        
        // given
        Patient expectedPatient = patientDTO;
        doReturn(expectedPatient).when(patientService).upsertPatient(patientDTO);

        // when
        ResultActions response = mockMvc.perform(post("/api/patients")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(patientDTO)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(patientDTO.getId()), Long.class))
                .andExpect(jsonPath("$.patientId", is(patientDTO.getPatientId())))
                .andExpect(jsonPath("$.firstName", is(patientDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(patientDTO.getLastName())))
                .andExpect(jsonPath("$.dob", is(patientDTO.getDob().toString())))
                .andExpect(jsonPath("$.address", is(patientDTO.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(patientDTO.getPhoneNumber())));
    }


    @Test
    public void testAPIDeletePatientById() throws Exception {

        long id = 1;

        // given
        doNothing().when(patientService).deletePatientById(id);

        // when
        ResultActions response = mockMvc.perform(delete("/api/patients/{id}", id));

        // then
        response.andExpect(status().isNoContent());
    }

}
