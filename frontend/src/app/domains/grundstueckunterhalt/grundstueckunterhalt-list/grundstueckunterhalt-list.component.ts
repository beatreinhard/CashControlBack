import {AfterViewInit, ChangeDetectionStrategy, Component, effect, input, output, viewChild} from '@angular/core';
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
import {GrundstueckunterhaltDto} from '../../../generated';

@Component({
  selector: 'app-grundstueckunterhalt-list',
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
  templateUrl: './grundstueckunterhalt-list.component.html',
  styleUrls: ['../../../shared/list-styles.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GrundstueckunterhaltListComponent implements AfterViewInit {
  readonly items = input.required<GrundstueckunterhaltDto[]>();
  readonly grundstueckunterhaltSelected = output<string>();

  private readonly paginator = viewChild.required(MatPaginator);
  private readonly sort = viewChild.required(MatSort);

  protected readonly displayedColumns: string[] = [
    'jahr', 'rgDatum', 'ausfuehrendeFirma', 'arbeitsArt', 'betragNetto', 'anteilAndereKosten', 'anteilUnterhalt'
  ];
  protected readonly dataSource = new MatTableDataSource<GrundstueckunterhaltDto>([]);

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

  protected selectRow(eintrag: GrundstueckunterhaltDto): void {
    if (eintrag.id) this.grundstueckunterhaltSelected.emit(eintrag.id);
  }
}
