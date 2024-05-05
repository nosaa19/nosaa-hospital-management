import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild, Input, Output, EventEmitter} from '@angular/core';
import { FormControl } from '@angular/forms';
import { ReplaySubject, Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { MatSelect } from '@angular/material/select';


export interface Field {
    id: string;
    name: string;
  }

export const FIELDS: Field[] = [
    {name: '- - -', id: '0'},
    {name: 'Patient Id', id: '1'},
    {name: 'Patient Name', id: '2'},
  ];

@Component({
  selector: 'app-field-selector',
  templateUrl: './field-selector.component.html',
  styleUrls: ['./field-selector.component.css']
})
export class FieldSelectorComponent implements OnInit, OnDestroy {

    @Output() fieldChanged: EventEmitter<Field> =   new EventEmitter();

    /** list of fields */
    fields: Field[]= FIELDS;
  
    /** control for the selected field */
    public fieldCtrl: FormControl<Field> = new FormControl<Field>(FIELDS[0], {nonNullable: true});
  
    /** Subject that emits when the component has been destroyed. */
    protected _onDestroy = new Subject<void>();
  
    constructor() { }
  
    ngOnInit() {
      this.fieldCtrl.valueChanges.subscribe(value => {
        this.fieldChanged.emit(value);
   });
    }
  
    ngOnDestroy() {
      this._onDestroy.next();
      this._onDestroy.complete();
    }
}

