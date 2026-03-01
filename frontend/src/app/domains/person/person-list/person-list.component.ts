import {AfterViewInit, Component, effect, inject, input, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatFooterCell,
  MatFooterCellDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {PersonDto} from '../../../generated';
import {Router} from '@angular/router';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-person-list',
  imports: [
    DatePipe,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatFooterCell,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatSort,
    MatTable,
    MatHeaderCellDef,
    MatFooterCellDef
  ],
  templateUrl: './person-list.component.html',
  styleUrl: './person-list.component.css'
})
export class PersonListComponent  implements AfterViewInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  protected displayedColumns: string[] = ['name','vorname', 'geburtsdatum','ahvnummer'];
  protected dataSource = new MatTableDataSource<PersonDto>([]);

  private router = inject(Router);

  readonly items = input.required<PersonDto[]>();

  constructor() {
    // Bridge: Signal -> Imperative API von MatTableDataSource
    effect(() => {
      const data = this.items(); // kein ?? [] nötig bei required
      this.dataSource.data = data;
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }


  selectRow(rowId: string) {
    this.router.navigate(['/person/edit', rowId]);
  }
}
