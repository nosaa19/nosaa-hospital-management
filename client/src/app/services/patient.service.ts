import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Patient } from '../models/patient.model';

const baseUrl = 'http://localhost:8080/api/patients';

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Patient[]> {
    return this.http.get<Patient[]>(baseUrl);
  }

  get(id: any): Observable<Patient> {
    return this.http.get(`${baseUrl}/${id}`);
  }

  upsert(data: any): Observable<any> {
    return this.http.post(baseUrl, data);
  }

  delete(id: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  findByKeyword(keyword: any, field: any): Observable<Patient[]> {
    return this.http.get<Patient[]>(`${baseUrl}?keyword=${keyword}&field=${field}`);
  }
}
