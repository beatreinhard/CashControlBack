import {AfterViewInit, Component, effect, inject, input, OnInit, signal, ViewChild} from '@angular/core';
import {AusgabeDto, VergabungControllerApi, VergabungDto} from '../../../generated';
import {DatePipe, JsonPipe} from '@angular/common';
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
import {MatChip, MatChipSet} from '@angular/material/chips';

@Component({
  selector: 'app-vergabung-list',
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
  templateUrl: './vergabung-list.component.html',
  styleUrl: './vergabung-list.component.css'
})
export class VergabungListComponent implements OnInit, AfterViewInit{
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  protected displayedColumns: string[] = ['jahr', 'datum','empfaenger', 'betrag'];
  protected dataSource = new MatTableDataSource<VergabungDto>([]);

  private router = inject(Router);

  //readonly items = input.required<VergabungDto[]>();
  //readonly error = signal<string | undefined>(undefined);





  vergabungData: any;

  constructor(private vergabungController: VergabungControllerApi) {
    // // Bridge: Signal -> Imperative API von MatTableDataSource
    // effect(() => {
    //   const data = this.items(); // kein ?? [] nötig bei required
    //   this.dataSource.data = data;
    // });
  }

  ngOnInit() {
 //   this.dataSource = new MatTableDataSource<VergabungDto>([]);

    this.vergabungController.getAllVergabungen().subscribe({
      next: (res) => {
        console.log('API response Vergabung:', res);
        console.log(this.dataSource.data);
        this.dataSource.data = res;
        this.vergabungData = res;
        console.log(this.dataSource.data);
      },
      error: (err) => console.error('API error:', err),
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}
