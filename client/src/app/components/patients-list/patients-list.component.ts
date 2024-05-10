import { Component, OnInit, ViewChild, ChangeDetectorRef} from "@angular/core";

import { MatTableDataSource } from '@angular/material/table';
import { PageEvent} from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';

import { PatientTable } from '../../models/patient.model';
import { PatientService } from '../../services/patient.service';
import { Field } from "../field-selector/field-selector.component";
import { PatientDetailsComponent } from '../patient-details/patient-details.component';

@Component({
    selector: 'app-patients-list',
    templateUrl: './patients-list.component.html',
    styleUrls: ['./patients-list.component.css'],
})
export class PatientsListComponent implements OnInit {

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

    totalData!: number;
    pageIndex: number = 0;
    pageSize: number = 5;
    pageSizes: number[] = [5, 10, 15];
    isLoading = false;

    keyword = '';
    field = '0';

    @ViewChild(MatSort) sort!: MatSort;

    dataSource!: MatTableDataSource<any>;

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

    retrievePatients() {
        const params = this.getRequestParams(this.keyword, this.field, this.pageIndex + 1, this.pageSize);

        this.patientService.getAll(params).subscribe({
            next: (res: PatientTable) => {
                this.dataSource = new MatTableDataSource(res.patients);
                if(res) {
                    this.dataSource.sort = this.sort;
                    this.totalData = res.totalItems;

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
            next: () => {
              alert('Patient deleted!');
              this.retrievePatients();
            },
            error: (err) => {
              console.log(err);
            },
          });
        }
    }

    pageChanged(event: PageEvent) {

      this.pageIndex = event.pageIndex;
      this.pageSize = event.pageSize;
      this.retrievePatients();
    }

    searchKeyword() {
        this.retrievePatients();
    }

    openAddEditEmployeeDialog() {
        const dialogRef = this.dialog.open(PatientDetailsComponent);
        dialogRef.afterClosed().subscribe({
        next: (val) => {
            if (val) {
                this.retrievePatients();
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
                this.retrievePatients();
              }
            }
          });
    }

    ngOnInit(){
      this.retrievePatients();
    }

    ngAfterContentInit() {
        this.cdr.detectChanges();
    }
    
}