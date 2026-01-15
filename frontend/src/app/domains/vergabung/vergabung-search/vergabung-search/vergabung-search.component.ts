import {Component, inject, OnInit, signal} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {VergabungListComponent} from '../../vergabung-list/vergabung-list.component';
import {VergabungControllerApi, VergabungDto} from '../../../../generated';

@Component({
  selector: 'app-vergabung-search',
  imports: [
    MatProgressSpinner,
    VergabungListComponent
  ],
  templateUrl: './vergabung-search.component.html',
  styleUrl: './vergabung-search.component.css'
})
export class VergabungSearchComponent implements OnInit {
  readonly loading = signal(true);
  readonly error = signal<string | undefined>("");
  readonly result = signal<VergabungDto[]>([]);

  protected vergabungController = inject(VergabungControllerApi);

  ngOnInit(): void {
    this.vergabungController.getVergabungen().subscribe({
      next: (res) => {
        this.result.set((res as VergabungDto[]) ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('API error:', err);
        this.error.set('Konnte Vergabungen nicht laden.');
        this.loading.set(false);
      },
    });
  }
}
