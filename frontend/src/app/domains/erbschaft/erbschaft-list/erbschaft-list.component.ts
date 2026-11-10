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
  MatNoDataRow,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {ErbschaftDto} from '../../../generated';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-erbschaft-list',
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
    MatTable,
    MatNoDataRow,
    MatIcon
  ],
  templateUrl: './erbschaft-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ErbschaftListComponent implements AfterViewInit {
  readonly items = input.required<ErbschaftDto[]>();
  readonly erbschaftSelected = output<string>();

  private readonly paginator = viewChild.required(MatPaginator);
  private readonly sort = viewChild.required(MatSort);

  protected readonly displayedColumns: string[] = [
    'jahr', 'datum', 'art', 'geber', 'verwandtschaftsverhaeltnis', 'gegenstand', 'betrag'
  ];
  protected readonly dataSource = new MatTableDataSource<ErbschaftDto>([]);

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

  protected selectRow(erbschaft: ErbschaftDto): void {
    if (erbschaft.id) this.erbschaftSelected.emit(erbschaft.id);
  }
}
