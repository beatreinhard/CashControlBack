import {AfterViewInit, ChangeDetectionStrategy, Component, effect, input, viewChild} from '@angular/core';
import {DatePipe, DecimalPipe} from '@angular/common';
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
import {VergabungDto} from '../../../generated';

/** Vergabungen sind nur lesbar (sie entstehen aus Ausgaben), daher keine klickbaren Zeilen. */
@Component({
  selector: 'app-vergabung-list',
  imports: [
    DatePipe,
    DecimalPipe,
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
  templateUrl: './vergabung-list.component.html',
  styleUrls: ['../../../shared/list-styles.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VergabungListComponent implements AfterViewInit {
  readonly items = input.required<VergabungDto[]>();

  private readonly paginator = viewChild.required(MatPaginator);
  private readonly sort = viewChild.required(MatSort);

  protected readonly displayedColumns: string[] = ['jahr', 'zahlungsDatum', 'empfaenger', 'betrag'];
  protected readonly dataSource = new MatTableDataSource<VergabungDto>([]);

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
}
