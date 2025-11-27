import {AfterViewInit, Component, effect, input, signal, ViewChild} from '@angular/core';
import {AusgabeDto} from '../../../generated';
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
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatChip, MatChipSet} from '@angular/material/chips';
import {DatePipe} from '@angular/common';


@Component({
  selector: 'app-ausgabe-list',
  imports: [
    MatPaginator,
    MatChipSet,
    MatChip,
    MatTable,
    MatHeaderCellDef,
    MatCellDef,
    MatFooterCellDef,
    MatHeaderRowDef,
    MatRowDef,
    MatColumnDef,
    MatHeaderCell,
    MatSort,
    MatCell,
    MatFooterCell,
    MatHeaderRow,
    MatRow,
    DatePipe

  ],
  templateUrl: './ausgabe-list.component.html',
  styleUrl: './ausgabe-list.component.css'
})
export class AusgabeListComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  protected displayedColumns: string[] = ['datum','empfaenger','text','kategorie','betrag'];
  protected dataSource = new MatTableDataSource<AusgabeDto>([]);

  readonly items = input.required<AusgabeDto[]>();
  readonly error = signal<string | undefined>(undefined);


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
}
