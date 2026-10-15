import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {AusgabeControllerApi} from '../../../generated';
import {AusgabeListComponent} from '../ausgabe-list/ausgabe-list.component';

@Component({
  selector: 'app-ausgabe-search',
  imports: [
    AusgabeListComponent,
    MatFormField,
    MatInput,
    MatLabel,
    MatProgressSpinner
  ],
  templateUrl: './ausgabe-search.component.html',
  styleUrl: './ausgabe-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AusgabeSearchComponent {

  private readonly ausgabeController = inject(AusgabeControllerApi);
  private readonly router = inject(Router);

  private readonly ausgabenResource = rxResource({
    loader: () => this.ausgabeController.getAllAusgabeView()
  });

  protected readonly searchTerm = signal('');

  protected readonly loading = this.ausgabenResource.isLoading;
  protected readonly error = computed(() =>
    this.ausgabenResource.error() ? 'Konnte Ausgaben nicht laden.' : null
  );
  protected readonly result = computed(() => {
    const ausgaben = this.ausgabenResource.value() ?? [];
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) return ausgaben;
    return ausgaben.filter(a =>
      [a.empfaenger, a.kategorie, a.zahlenderName, a.zahlenderVorname]
        .some(value => value?.toLowerCase().includes(term))
    );
  });

  protected onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
  }

  protected openAusgabe(ausgabeId: string): void {
    this.router.navigate(['/ausgabe/edit', ausgabeId]);
  }
}
