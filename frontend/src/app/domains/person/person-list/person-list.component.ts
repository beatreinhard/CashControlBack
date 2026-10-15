import {AfterViewInit, ChangeDetectionStrategy, Component, effect, input, output, viewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort, MatSortHeader} from '@angular/material/sort';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
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
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-person-list',
  imports: [
    DatePipe,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatSort,
    MatSortHeader,
    MatTable
  ],
  templateUrl: './person-list.component.html',
  styleUrl: './person-list.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PersonListComponent implements AfterViewInit {
  readonly items = input.required<PersonDto[]>();
  readonly personSelected = output<string>();

  private readonly paginator = viewChild.required(MatPaginator);
  private readonly sort = viewChild.required(MatSort);

  protected readonly displayedColumns: string[] = ['name', 'vorname', 'geburtsdatum', 'ahvnummer'];
  protected readonly dataSource = new MatTableDataSource<PersonDto>([]);

  constructor() {
    // Bridge: Signal -> imperatives API von MatTableDataSource
    effect(() => {
      this.dataSource.data = this.items();
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator();
    this.dataSource.sort = this.sort();
  }

  protected selectRow(person: PersonDto): void {
    if (person.id) this.personSelected.emit(person.id);
  }
}
