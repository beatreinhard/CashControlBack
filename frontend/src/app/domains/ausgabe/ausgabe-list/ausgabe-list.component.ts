import {Component, Input, OnChanges, signal, SimpleChanges, ViewChild} from '@angular/core';
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
import {MatFormField} from '@angular/material/input';
import {MatChip, MatChipSet} from '@angular/material/chips';


@Component({
  selector: 'app-ausgabe-list',
  imports: [
    MatPaginator,
    MatFormField,
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
    MatRow

  ],
  templateUrl: './ausgabe-list.component.html',
  styleUrl: './ausgabe-list.component.css'
})
export class AusgabeListComponent implements OnChanges {
  @Input() items: AusgabeDto[] = [];

  displayedColumns: string[] = ['datum','empfaenger','text','kategorie','betrag'];
  dataSource = new MatTableDataSource<AusgabeDto>([]);


  loading = true;
  error = signal<string | undefined>("");

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  ngOnChanges(changes: SimpleChanges): void {
    if (changes['items']) {
      this.dataSource.data = this.items ?? [];
      if (this.paginator) {
        this.dataSource.paginator = this.paginator;
      }
      if (this.sort) {
        this.dataSource.sort = this.sort;
      }
    }
  }

}
