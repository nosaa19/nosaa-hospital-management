import { Component, OnInit, ViewChild } from "@angular/core";

import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';

import { Patient, PatientTable } from '../../models/patient.model';
import { PatientService } from '../../services/patient.service';

@Component({
    selector: 'app-patients-list',
    templateUrl: './patients-list.component.html',
    styleUrls: ['./patients-list.component.css'],
})
export class PatientsListComponent implements OnInit {

    // dependency injection
    constructor(
        private patientService: PatientService,
        private dialog: MatDialog) { }

    displayedColumns: string[] = [
        'patientId',
        'firstName',
        'lastName',
        'dob',
        'address',
        'action',
    ];

    patientTable!: PatientTable;
    totalData!: number;
    patientData!: Patient[];
    isLoading = false;

    keyword = '';
    field = '0';

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    dataSource!: MatTableDataSource<any>;

    pageSizes = [3, 5, 7];

    getRequestParams(keyword: String, field: String, page: Number, pageSize: Number): any {
        let params: any = {};
    
        if (keyword) {
          params[`keyword`] = keyword;
          params[`field`] = field;
        }
    
        if (page) {
          params[`page`] = page.valueOf() - 1;
        }
    
        if (pageSize) {
          params[`size`] = pageSize;
        }
    
        return params;
      }

    retrievePatients(pageNumber: Number, pageSize: Number) {
        const params = this.getRequestParams(this.keyword, this.field, pageNumber, pageSize);

        this.patientService.getAll(params).subscribe({
            next: (res) => {
              this.dataSource = new MatTableDataSource(res);
              this.dataSource.sort = this.sort;
              this.dataSource.paginator = this.paginator;
              console.log(res);
            },
            error: (err) => {
              console.log(err);
            },
          });

        // this.paginator.page
        // .pipe(
        //   startWith({}),
        //   switchMap(() => {
        //     this.isLoading = true;
        //     return this.patientService.getAll(params)
        //         .pipe(catchError(() => EMPTY))
        //   }),
        //   map((patientData) => {
        //     if (patientData == null) return [];
        //     this.totalData = patientData.total;
        //     this.isLoading = false;
        //     return patientData.data;
        //   })
        // )
        // .subscribe((patientData) => {

        //     console.log("AYAM", patientData)

        //   this.patientData = patientData;
        //   this.dataSource = new MatTableDataSource(this.patientData);

        //   this.dataSource.paginator = this.paginator;    
        // });
    }

    deletePatient(id: any) {
        let confirm = window.confirm("Are you sure you want to delete this patient?");
        if(confirm) {
          this.patientService.delete(id).subscribe({
            next: (res) => {
              alert('Patient deleted!');
              this.retrievePatients(
                this.paginator.pageIndex + 1,
                this.paginator.pageSize
              );
            },
            error: (err) => {
              console.log(err);
            },
          });
        }
    }

    searchKeyword() {
        this.retrievePatients(
            this.paginator.pageIndex + 1,
            this.paginator.pageSize
        );
    }

    openAddEditEmployeeDialog() {}

    openEditForm(data: any) {
        console.log(data);
    }

    ngOnInit(){
        this.retrievePatients(
            0,
            5
        );
    }
    
}