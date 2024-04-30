import { Component, OnInit } from '@angular/core';
import { Patient } from '../../models/patient.model';
import { PatientService } from '../../services/patient.service';

@Component({
  selector: 'app-patients-list',
  templateUrl: './patients-list.component.html',
  styleUrl: './patients-list.component.css'
})
export class PatientsListComponent implements OnInit {

  patients?: Patient[];
  currentPatient: Patient = {};
  currentIndex = -1;
  keyword = '';
  field = '';

  constructor(private patientService: PatientService) { }

  ngOnInit(): void {
    this.retrievePatients();
  }

  retrievePatients(): void {
    this.patientService.getAll()
      .subscribe({
        next: (data) => {
          this.patients = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  refreshList(): void {
    this.retrievePatients();
    this.currentPatient = {};
    this.currentIndex = -1;
  }

  setActivePatient(patient: Patient, index: number): void {
    this.currentPatient = patient;
    this.currentIndex = index;
  }

  searchKeyword(): void {
    this.currentPatient = {};
    this.currentIndex = -1;

    this.patientService.findByKeyword(this.keyword, this.field)
      .subscribe({
        next: (data) => {
          this.patients = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

}
