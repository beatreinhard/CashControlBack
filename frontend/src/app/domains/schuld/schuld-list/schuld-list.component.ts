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
import {SchuldDto} from '../../../generated';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-schuld-list',
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
    MatTable,
    MatNoDataRow,
    MatIcon
  ],
  templateUrl: './schuld-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SchuldListComponent implements AfterViewInit {
  readonly items = input.required<SchuldDto[]>();
  readonly schuldSelected = output<string>();

  private readonly paginator = viewChild.required(MatPaginator);
  private readonly sort = viewChild.required(MatSort);

  protected readonly displayedColumns: string[] = ['jahr', 'art', 'glaeubiger', 'text', 'betrag', 'zinsen'];
  protected readonly dataSource = new MatTableDataSource<SchuldDto>([]);

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

  protected selectRow(schuld: SchuldDto): void {
    if (schuld.id) this.schuldSelected.emit(schuld.id);
  }
}
