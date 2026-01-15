import {AfterViewInit, Component, effect, inject, input, signal, ViewChild} from '@angular/core';
import {VergabungDto} from '../../../generated';
import {DatePipe} from '@angular/common';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
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
import {Router} from '@angular/router';

@Component({
  selector: 'app-vergabung-list',
  imports: [
    MatPaginator,
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
  templateUrl: './vergabung-list.component.html',
  styleUrl: './vergabung-list.component.css'
})
export class VergabungListComponent implements AfterViewInit{
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  protected displayedColumns: string[] = ['jahr', 'datum','empfaenger', 'betrag'];
  protected dataSource = new MatTableDataSource<VergabungDto>([]);

  private router = inject(Router);

  readonly items = input.required<VergabungDto[]>();
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
