import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ReplaySubject, Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { MatSelect } from '@angular/material/select';




export interface Field {
    id: string;
    name: string;
  }

export const FIELDS: Field[] = [
    {name: 'Patient Id', id: '1'},
    {name: 'Patient Name', id: '2'},
  ];

@Component({
  selector: 'app-field-selector',
  templateUrl: './field-selector.component.html',
  styleUrls: ['./field-selector.component.css']
})
export class FieldSelectorComponent implements OnInit, AfterViewInit, OnDestroy {

    /** list of fields */
    protected fields: Field[]= FIELDS;
  
    /** control for the selected field */
    public fieldCtrl: FormControl<Field> = new FormControl<Field>(FIELDS[0], { nonNullable: true });
  
    /** list of banks filtered by search keyword */
    public filteredField: ReplaySubject<Field[]> = new ReplaySubject<Field[]>(1);
  
    @ViewChild('singleSelect', { static: false }) singleSelect!: MatSelect;
  
    /** Subject that emits when the component has been destroyed. */
    protected _onDestroy = new Subject<void>();
  
  
    constructor() { }
  
    ngOnInit() {}
  
    ngAfterViewInit() {
      this.setInitialValue();
    }
  
    ngOnDestroy() {
      this._onDestroy.next();
      this._onDestroy.complete();
    }
  
    /**
     * Sets the initial value after the filteredBanks are loaded initially
     */
    protected setInitialValue() {
      this.filteredField
        .pipe(take(1), takeUntil(this._onDestroy))
        .subscribe(() => {
          // setting the compareWith property to a comparison function
          // triggers initializing the selection according to the initial value of
          // the form control (i.e. _initializeSelection())
          // this needs to be done after the filteredBanks are loaded initially
          // and after the mat-option elements are available
          this.singleSelect.compareWith = (a: Field, b: Field) => a && b && a.id === b.id;
        });
    }
}

