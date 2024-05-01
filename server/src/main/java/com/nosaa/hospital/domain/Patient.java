package com.nosaa.hospital.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Size;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;


@Entity
@Table(name="patients")
public class Patient {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

    @Column(name = "patient_id")
    private String patientId;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "address")
    private String address;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    

    public Patient() {
    }

    public Patient(long id, String patientId, String firstName, String lastName, LocalDate dob, String address, String phoneNumber) {
        this.id = id;
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String addreess) {
        this.address = addreess;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Patient id(long id) {
        setId(id);
        return this;
    }

    public Patient patientId(String patientId) {
        setPatientId(patientId);
        return this;
    }

    public Patient firstName(String firstName) {
        setFirstName(firstName);
        return this;
    }

    public Patient lastName(String lastName) {
        setLastName(lastName);
        return this;
    }

    public Patient dob(LocalDate dob) {
        setDob(dob);
        return this;
    }

    public Patient address(String addreess) {
        setAddress(addreess);
        return this;
    }

    public Patient phoneNumber(String phoneNumber) {
        setPhoneNumber(phoneNumber);
        return this;
    }

    @Override
    public boolean equals(Object o) {
      return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, firstName, lastName, dob, address, phoneNumber);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", patientId='" + getPatientId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", dob='" + getDob() + "'" +
            ", addreess='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }    
}
