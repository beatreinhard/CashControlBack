import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {AusgabeControllerApi} from '../../../generated';
import {AusgabeListComponent} from '../ausgabe-list/ausgabe-list.component';
import {KATEGORIE_META} from '../ausgabe-kategorie-meta';

@Component({
  selector: 'app-ausgabe-search',
  imports: [
    AusgabeListComponent,
    MatIcon,
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
      [a.empfaenger, a.kategorie, KATEGORIE_META[a.kategorie]?.label, a.zahlenderName, a.zahlenderVorname]
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
