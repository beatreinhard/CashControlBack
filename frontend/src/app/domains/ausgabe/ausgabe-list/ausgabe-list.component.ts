import {AfterViewInit, ChangeDetectionStrategy, Component, effect, input, output, viewChild} from '@angular/core';
import {DatePipe, DecimalPipe} from '@angular/common';
import {MatIcon} from '@angular/material/icon';
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
import {AusgabeKategorieDto, AusgabeViewDto} from '../../../generated';
import {kategorieMeta} from '../ausgabe-kategorie-meta';
import {FullNamePipe} from '../../../shared/full-name.pipe';

@Component({
  selector: 'app-ausgabe-list',
  imports: [
    DatePipe,
    DecimalPipe,
    FullNamePipe,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatIcon,
    MatNoDataRow,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatSort,
    MatSortHeader,
    MatTable
  ],
  templateUrl: './ausgabe-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AusgabeListComponent implements AfterViewInit {
  readonly items = input.required<AusgabeViewDto[]>();
  readonly ausgabeSelected = output<string>();

  private readonly paginator = viewChild.required(MatPaginator);
  private readonly sort = viewChild.required(MatSort);

  protected readonly displayedColumns: string[] = ['datum', 'empfaenger', 'zahlender', 'kategorie', 'betrag'];
  protected readonly dataSource = new MatTableDataSource<AusgabeViewDto>([]);

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

  protected meta(kategorie: AusgabeKategorieDto) {
    return kategorieMeta(kategorie);
  }

  protected selectRow(ausgabe: AusgabeViewDto): void {
    this.ausgabeSelected.emit(ausgabe.id);
  }
}
