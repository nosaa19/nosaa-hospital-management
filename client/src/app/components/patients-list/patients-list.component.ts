import { Component, OnInit, ViewChild, ChangeDetectorRef} from "@angular/core";

import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent} from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';

import { Patient, PatientTable } from '../../models/patient.model';
import { PatientService } from '../../services/patient.service';
import { Field } from "../field-selector/field-selector.component";
import { PatientDetailsComponent } from '../patient-details/patient-details.component';

@Component({
    selector: 'app-patients-list',
    templateUrl: './patients-list.component.html',
    styleUrls: ['./patients-list.component.css'],
})
export class PatientsListComponent implements OnInit {

    /* TODO: Page Index Fixing */

    // dependency injection
    constructor(
        private patientService: PatientService,
        private dialog: MatDialog,
        private cdr: ChangeDetectorRef 
    ) { }

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

    pageSizes = [5, 10, 15];

    getRequestParams(keyword: String, field: String, page: Number, pageSize: Number): any {
        let params: any = {};
    
        if (keyword) {
          params[`keyword`] = keyword;
        }

        if(field) {
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
                if(res) {
                    this.dataSource.sort = this.sort;
                    this.dataSource.paginator = this.paginator;
                }
            },
            error: (err) => {
              console.log(err);
            },
        });
    }

    fieldChangedHandler(field: Field) {
        this.field = field.id;
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

    pageChanged(event: PageEvent) {}

    searchKeyword() {
        this.retrievePatients(
            this.paginator.pageIndex + 1,
            this.paginator.pageSize
        );
    }

    openAddEditEmployeeDialog() {
        const dialogRef = this.dialog.open(PatientDetailsComponent);
        dialogRef.afterClosed().subscribe({
        next: (val) => {
            if (val) {
                this.retrievePatients(
                    this.paginator.pageIndex + 1,
                    this.paginator.pageSize
                );
            }
        },
        });
    }

    openEditForm(data: any) {
        const dialogRef = this.dialog.open(PatientDetailsComponent, {
            data,
          });
      
          dialogRef.afterClosed().subscribe({
            next: (val) => {
              if (val) {
                this.retrievePatients(
                    this.paginator.pageIndex + 1,
                    this.paginator.pageSize
                );
              }
            }
          });
    }

    ngOnInit(){
        this.retrievePatients(
            1,
            5
        );
    }

    ngAfterContentInit() {
        this.cdr.detectChanges();
    }
    
}