import {Component, inject, OnInit, signal} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {SchuldControllerApi, SchuldDto} from '../../../generated';
import {SchuldListComponent} from '../schuld-list/schuld-list.component';

@Component({
  selector: 'app-schuld-search',
  imports: [
    MatProgressSpinner,
    SchuldListComponent
  ],
  templateUrl: './schuld-search.component.html',
  styleUrl: './schuld-search.component.css'
})
export class SchuldSearchComponent implements OnInit {
  readonly loading = signal(true);
  readonly error = signal<string | undefined>("");
  readonly result = signal<SchuldDto[]>([]);

  protected schuldController = inject(SchuldControllerApi);

  ngOnInit(): void {
    this.schuldController.getSchulden().subscribe({
      next: (res) => {
        this.result.set((res as SchuldDto[]) ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('API error:', err);
        this.error.set('Konnte Schulden nicht laden.');
        this.loading.set(false);
      },
    });
  }
}
