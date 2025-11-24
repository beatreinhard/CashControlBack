import {Component, inject, OnInit, signal} from '@angular/core';
import {AusgabeControllerApi, AusgabeDto} from '../../../generated';
import {AusgabeListComponent} from '../ausgabe-list/ausgabe-list.component';
import {MatProgressSpinner} from '@angular/material/progress-spinner';

@Component({
  selector: 'app-ausgabe-search',
  imports: [
    AusgabeListComponent,
    MatProgressSpinner
  ],
  templateUrl: './ausgabe-search.component.html',
  styleUrl: './ausgabe-search.component.css'
})
export class AusgabeSearchComponent implements OnInit {


  loading = signal(true);
  error = signal<string | undefined>("");

  private ausgabeController = inject(AusgabeControllerApi);

  result = signal<AusgabeDto[]>([]);

  ngOnInit(): void {
    this.ausgabeController.getAllAusgabe().subscribe({
      next: (res) => {
        this.result.set((res as AusgabeDto[]) ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('API error:', err);
        this.error.set('Konnte Ausgaben nicht laden.');
        this.loading.set(false);
      },
    });
  }
}
