import {Component, inject, OnInit, signal} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {KostenControllerApi, KostenDto} from '../../../generated';
import {KostenListComponent} from '../kosten-list/kosten-list.component';

@Component({
  selector: 'app-kosten-search',
  imports: [
    MatProgressSpinner,
    KostenListComponent
  ],
  templateUrl: './kosten-search.component.html',
  styleUrl: './kosten-search.component.css'
})
export class KostenSearchComponent implements OnInit {
  readonly loading = signal(true);
  readonly error = signal<string | undefined>("");
  readonly result = signal<KostenDto[]>([]);

  protected kostenController = inject(KostenControllerApi);

  ngOnInit(): void {
    this.kostenController.getKosten().subscribe({
      next: (res) => {
        this.result.set((res as KostenDto[]) ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('API error:', err);
        this.error.set('Konnte Kosten nicht laden.');
        this.loading.set(false);
      },
    });
  }
}
