import {AfterViewInit, ChangeDetectionStrategy, Component, effect, input, output, viewChild} from '@angular/core';
import {DecimalPipe} from '@angular/common';
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
import {BerufDto} from '../../../generated';

/** Beruf mit aufgelöstem Namen des Arbeitnehmers (Person). */
export type BerufRow = BerufDto & { arbeitnehmerName: string };

@Component({
  selector: 'app-beruf-list',
  imports: [
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
  templateUrl: './beruf-list.component.html',
  styleUrls: ['../../../shared/list-styles.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BerufListComponent implements AfterViewInit {
  readonly items = input.required<BerufRow[]>();
  readonly berufSelected = output<string>();

  private readonly paginator = viewChild.required(MatPaginator);
  private readonly sort = viewChild.required(MatSort);

  protected readonly displayedColumns: string[] = [
    'jahr', 'arbeitnehmerName', 'arbeitgeber', 'arbeitsort', 'beschaeftigungsgrad', 'arbeitstage', 'fahrtkilometerProTag'
  ];
  protected readonly dataSource = new MatTableDataSource<BerufRow>([]);

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

  protected selectRow(beruf: BerufRow): void {
    if (beruf.id) this.berufSelected.emit(beruf.id);
  }
}
