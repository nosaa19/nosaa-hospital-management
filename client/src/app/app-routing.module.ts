import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PatientsListComponent } from './components/patients-list/patients-list.component';
import { PatientDetailsComponent } from './components/patient-details/patient-details.component';
import { AddPatientComponent } from './components/add-patient/add-patient.component';

const routes: Routes = [
  { path: '', redirectTo: 'patients', pathMatch: 'full' },
  { path: 'patients', component: PatientsListComponent },
  { path: 'patients/:id', component: PatientDetailsComponent },
  { path: 'add', component: AddPatientComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
