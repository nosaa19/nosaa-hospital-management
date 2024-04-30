import { Component } from '@angular/core';
import { Patient } from '../../models/patient.model';
import { PatientService } from '../../services/patient.service';

@Component({
  selector: 'app-add-patient',
  templateUrl: './add-patient.component.html',
  styleUrl: './add-patient.component.css'
})
export class AddPatientComponent {

  patient: Patient = {
    firstName: '',
    lastName: '',
    dob: new Date(),
    address: '',
    phoneNumber: ''
  };
  submitted = false;
  
  constructor(private patientService: PatientService) { }

  savePatient(): void {
    const data = {
      firstName: this.patient.firstName,
      lastName: this.patient.lastName,
      dob: this.patient.dob,
      address: this.patient.address,
      phoneNumber: this.patient.phoneNumber
    };

    this.patientService.upsert(data)
      .subscribe({
        next: (res: any) => {
          console.log(res);
          this.submitted = true;
        },
        error: (e: any) => console.error(e)
      });
  }

  newPatient(): void {
    this.submitted = false;
    this.patient = {
      firstName: '',
      lastName: '',
      dob: new Date(),
      address: '',
      phoneNumber: ''
    };
  }
}
