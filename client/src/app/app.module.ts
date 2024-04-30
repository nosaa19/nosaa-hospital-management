import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddPatientComponent } from './components/add-patient/add-patient.component';
import { PatientDetailsComponent } from './components/patient-details/patient-details.component';
import { PatientsListComponent } from './components/patients-list/patients-list.component';

import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    AddPatientComponent,
    PatientDetailsComponent,
    PatientsListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
