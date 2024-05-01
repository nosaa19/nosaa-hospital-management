package com.nosaa.hospital.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nosaa.hospital.domain.DomainConstants;
import com.nosaa.hospital.domain.Patient;
import com.nosaa.hospital.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTests {
    
    @InjectMocks
    PatientService patientService;

    @Mock
    PatientRepository patientRepository;

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


    /* Find All By Keyword Usecase */

    private static Stream<Arguments> getPatientFilter() {
        return Stream.of(
            Arguments.of("", 0, PageRequest.of(0, 3, Sort.unsorted()))
        );
    }

    @ParameterizedTest
    @MethodSource("getPatientFilter")
    @DisplayName("find patient by id case success empty keyword")
    void testFindAllPatientByKeywordCaseSuccessEmptyKeyword(String keyword, int field, Pageable pageable) {
        
        // given
        Page<Patient> expectedPatientPage = pagePatientDTO;
        doReturn(expectedPatientPage).when(patientRepository).findAll(pageable);

        // when
        Page<Patient> gotPatientPage = patientService.findAllPatientByKeyword(keyword, field, pageable);

        // then
        verify(patientRepository, times(1)).findAll(pageable);
        assertEquals(expectedPatientPage, gotPatientPage);
    }

    private static Stream<Arguments> getPatientFilterByPatientId() {
        return Stream.of(
            Arguments.of(
            "123", 
            DomainConstants.PATIENT_ID_FIELD, 
            PageRequest.of(0, 3, Sort.unsorted()))
        );
    }

    @ParameterizedTest
    @MethodSource("getPatientFilterByPatientId")
    @DisplayName("find patient by id case success keyword field patient id")
    void testFindAllPatientByKeywordCaseSuccessFieldPatientId(String keyword, int field, Pageable pageable) {
        
        // given
        Page<Patient> expectedPatientPage = pagePatientDTO;
        doReturn(expectedPatientPage).when(patientRepository).findByPatientId(keyword, pageable);

        // when
        Page<Patient> gotPatientPage = patientService.findAllPatientByKeyword(keyword, field, pageable);

        // then
        verify(patientRepository, times(1)).findByPatientId(keyword, pageable);
        assertEquals(expectedPatientPage, gotPatientPage);
    }

    private static Stream<Arguments> getPatientFilterByName() {
        return Stream.of(
            Arguments.of(
            "test", 
            DomainConstants.PATIENT_NAME_FIELD, 
            PageRequest.of(0, 3, Sort.unsorted()))
        );
    }

    @ParameterizedTest
    @MethodSource("getPatientFilterByName")
    @DisplayName("find patient by id case success keyword field name")
    void testFindAllPatientByKeywordCaseSuccessFieldName(String keyword, int field, Pageable pageable) {
        
        // given
        Page<Patient> expectedPatientPage = pagePatientDTO;
        doReturn(expectedPatientPage).when(patientRepository)
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword, pageable);

        // when
        Page<Patient> gotPatientPage = patientService.findAllPatientByKeyword(keyword, field, pageable);

        // then
        verify(patientRepository, times(1))
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword, pageable);
        assertEquals(expectedPatientPage, gotPatientPage);
    }

    /* Find By Id Usecase */

    @ParameterizedTest
    @ValueSource(longs = {1})
    @DisplayName("find patient by id case success")
    void testFindPatientByIdCaseSuccess(long id) {
        
        // given
        Optional<Patient> expectedPatient = Optional.of(
           patientDTO
        );
        doReturn(expectedPatient).when(patientRepository).findById(id);

        // when
        Optional<Patient> gotPatient = patientService.findPatientById(id);

        // then
        verify(patientRepository, times(1)).findById(anyLong());
        assertEquals(expectedPatient, gotPatient);
    }

    /* Upsert Usecase */

    static Stream<Patient> getInputPatient() {
        final var patient =  new Patient(
            1,
            "12345", 
            "unit", 
            "test", 
            LocalDate.of(2020, 1, 8), 
            "A Street", 
            "+61888888888"
        );
        return Stream.of(patient);
    }

    @ParameterizedTest
    @MethodSource("getInputPatient")
    @DisplayName("insert patient case success")
    void testUpsertPatientCaseSuccessInsert(Patient inputPatient) throws Exception {
            
        // given
        inputPatient.setId(0); // new data 
        Patient expectedPatient = patientDTO;
        doReturn(Optional.empty()).when(patientRepository).findById(inputPatient.getId());
        doReturn(expectedPatient).when(patientRepository).save(inputPatient);
        
        // when
        Patient gotPatient = patientService.upsertPatient(inputPatient);
        
        // then
        verify(patientRepository, times(1)).findById(anyLong());
        verify(patientRepository, times(1)).save(any());

        assertEquals(expectedPatient, gotPatient);
    }

    @ParameterizedTest
    @MethodSource("getInputPatient")
    @DisplayName("update patient case success")
    void testUpsertPatientCaseSuccessUpdate(Patient inputPatient) throws Exception {
            
        // given
        Patient expectedPatient = patientDTO;
        doReturn(Optional.of(expectedPatient)).when(patientRepository).findById(inputPatient.getId());
        doReturn(expectedPatient).when(patientRepository).save(inputPatient);
        
        // when
        Patient gotPatient = patientService.upsertPatient(inputPatient);
        
        // then
        verify(patientRepository, times(1)).findById(anyLong());
        verify(patientRepository, times(1)).save(any());

        assertEquals(expectedPatient, gotPatient);
    }

    /* Delete By Id Usecase */

    @ParameterizedTest
    @ValueSource(longs = {1})
    @DisplayName("delete patient by id case success")
    void testDeletePatientCaseSuccess(long id) throws Exception{
        // given
        doNothing().when(patientRepository).deleteById(id);

        // when
        patientService.deletePatientById(id);

        // then
        verify(patientRepository, times(1)).deleteById(anyLong());
    }

}
