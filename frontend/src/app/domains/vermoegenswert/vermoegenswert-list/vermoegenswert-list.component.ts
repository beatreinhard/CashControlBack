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
import {VermoegenswertDto} from '../../../generated';

@Component({
  selector: 'app-vermoegenswert-list',
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
  templateUrl: './vermoegenswert-list.component.html',
  styleUrls: ['../../../shared/list-styles.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VermoegenswertListComponent implements AfterViewInit {
  readonly items = input.required<VermoegenswertDto[]>();
  readonly vermoegenswertSelected = output<string>();

  private readonly paginator = viewChild.required(MatPaginator);
  private readonly sort = viewChild.required(MatSort);

  protected readonly displayedColumns: string[] = [
    'jahr', 'bezeichnung', 'anschaffungsjahr', 'anschaffungspreis'
  ];
  protected readonly dataSource = new MatTableDataSource<VermoegenswertDto>([]);

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

  protected selectRow(vermoegenswert: VermoegenswertDto): void {
    if (vermoegenswert.id) this.vermoegenswertSelected.emit(vermoegenswert.id);
  }
}
