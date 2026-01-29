import {AfterViewInit, Component, effect, inject, input, ViewChild} from '@angular/core';
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
import {SchuldDto} from '../../../generated';
import {Router} from '@angular/router';
import {MatChip, MatChipSet} from '@angular/material/chips';

@Component({
  selector: 'app-schuld-list',
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
    MatHeaderCellDef,
    MatFooterCellDef
  ],
  templateUrl: './schuld-list.component.html',
  styleUrl: './schuld-list.component.css'
})
export class SchuldListComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  protected displayedColumns: string[] = ['jahr','art', 'glaeubiger', 'text','betrag','zinsen'];
  protected dataSource = new MatTableDataSource<SchuldDto>([]);

  private router = inject(Router);

  readonly items = input.required<SchuldDto[]>();

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
    this.router.navigate(['/schuld/edit', rowId]);
  }
}
