import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PatientService } from '../../services/patient.service'
import { Patient } from '../../models/patient.model';

@Component({
  selector: 'app-patient-details',
  templateUrl: './patient-details.component.html',
  styleUrl: './patient-details.component.css'
})
export class PatientDetailsComponent implements OnInit {

  @Input() viewMode = false;

  @Input() currentPatient: Patient = {
    firstName: '',
    lastName: '',
    dob: new Date(),
    address: '',
    phoneNumber: ''
  };

  message = '';

  constructor(
    private patientService: PatientService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    if (!this.viewMode) {
      this.message = '';
      this.getPatient(this.route.snapshot.params["id"]);
    }
  }

  getPatient(id: string): void {
    this.patientService.get(id)
      .subscribe({
        next: (data) => {
          this.currentPatient = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  updatePatient(): void {
    this.message = '';

    this.patientService.upsert(this.currentPatient)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.message = res.message ? res.message : 'This patient was updated successfully!';
        },
        error: (e) => console.error(e)
      });
  }

  deletePatient(): void {
    this.patientService.delete(this.currentPatient.id)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.router.navigate(['/patients']);
        },
        error: (e) => console.error(e)
      });
  }
}
