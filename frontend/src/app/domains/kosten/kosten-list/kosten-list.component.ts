import {AfterViewInit, Component, effect, inject, input, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatFooterCell,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {KostenDto} from '../../../generated';
import {Router} from '@angular/router';
import {MatChip, MatChipSet} from '@angular/material/chips';

@Component({
  selector: 'app-kosten-list',
  imports: [
    MatCell,
    MatCellDef,
    MatChip,
    MatChipSet,
    MatColumnDef,
    MatFooterCell,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatSort,
    MatTable,
    MatHeaderCellDef
  ],
  templateUrl: './kosten-list.component.html',
  styleUrl: './kosten-list.component.css'
})
export class KostenListComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  protected displayedColumns: string[] = ['jahr','art','zahlender','empfaenger','bemerkung','betrag'];
  protected dataSource = new MatTableDataSource<KostenDto>([]);

  private router = inject(Router);

  readonly items = input.required<KostenDto[]>();
  // readonly error = signal<string | undefined>(undefined);


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


  selectRow(rowId: string) {
    this.router.navigate(['/kosten/edit', rowId]);
  }
}
