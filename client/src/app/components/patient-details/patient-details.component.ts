import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { PatientService } from '../../services/patient.service';
import { Patient } from '../../models/patient.model';



const MY_DATE_FORMAT = {
  parse: {
    dateInput: 'DD-MM-YYYY', // this is how your date will be parsed from Input
  },
  display: {
    dateInput: 'DD-MM-YYYY', // this is how your date will get displayed on the Input
    monthYearLabel: 'MMMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY'
  }
};

@Component({
  selector: 'app-patient-detail',
  templateUrl: './patient-details.component.html',
  styleUrls: ['./patient-details.component.css'],
  providers: [
    { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] },
    { provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMAT }
  ],
})
export class PatientDetailsComponent implements OnInit {
  patientForm: FormGroup;

  constructor(
    private patientService: PatientService,
    private dialogRef: MatDialogRef<PatientDetailsComponent>,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.patientForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dob: ['', Validators.required],
      address: [''],
      phoneNumber: ['']
    });
  }

  ngOnInit(): void {
    this.patientForm.patchValue(this.data);
  }

  onSubmit() {
    if (this.patientForm.valid) {
      if (this.data) {
        this.mappingValue()
        this.patientService
          .upsert(this.data)
          .subscribe({
            next: (val: any) => {
              alert('Patient details updated!');
              this.dialogRef.close(true);
            },
            error: (err: any) => {
              //console.error("UPDATE WOOOYYYY", err);
              alert("Error while updating the patient!");
            },
          });
      } else {
        this.patientService.upsert(this.patientForm.value).subscribe({
          next: (val: any) => {
            alert('Patient added successfully!');
            this.patientForm.reset();
            this.dialogRef.close(true);
          },
          error: (err: any) => {
            //console.error("INSERT", err);
            alert("Error while adding the patient!");
          },
        });
      }
    }
  }

  mappingValue() {
    this.data as Patient;

    this.data.firstName = this.patientForm.get('firstName')?.value;
    this.data.lastName = this.patientForm.get('lastName')?.value;
    this.data.dob = this.patientForm.get('dob')?.value;
    this.data.address = this.patientForm.get('address')?.value;
    this.data.phoneNumber = this.patientForm.get('phoneNumber')?.value;
  }
}