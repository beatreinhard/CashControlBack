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
  MatNoDataRow,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {KostenViewDto} from '../../../generated';
import {FullNamePipe} from '../../../shared/full-name.pipe';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-kosten-list',
  imports: [
    DecimalPipe,
    FullNamePipe,
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
    MatTable,
    MatNoDataRow,
    MatIcon
  ],
  templateUrl: './kosten-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class KostenListComponent implements AfterViewInit {
  readonly items = input.required<KostenViewDto[]>();
  readonly kostenSelected = output<string>();

  private readonly paginator = viewChild.required(MatPaginator);
  private readonly sort = viewChild.required(MatSort);

  protected readonly displayedColumns: string[] = ['jahr', 'art', 'zahlender', 'empfaenger', 'bemerkung', 'betrag'];
  protected readonly dataSource = new MatTableDataSource<KostenViewDto>([]);

  constructor() {
    // Sortierung nach angezeigtem Namen statt nach Personen-ID
    this.dataSource.sortingDataAccessor = (row, column) =>
      column === 'zahlender'
        ? `${row.zahlenderName ?? ''} ${row.zahlenderVorname ?? ''}`.trim().toLowerCase()
        : (row as unknown as Record<string, string | number>)[column];

    // Bridge: Signal -> imperatives API von MatTableDataSource
    effect(() => {
      this.dataSource.data = this.items();
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator();
    this.dataSource.sort = this.sort();
  }

  protected selectRow(kosten: KostenViewDto): void {
    if (kosten.id) this.kostenSelected.emit(kosten.id);
  }
}
