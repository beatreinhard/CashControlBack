import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {BerufControllerApi} from '../../../generated';
import {BerufListComponent, BerufRow} from '../beruf-list/beruf-list.component';
import {injectPersonOptions} from '../../../shared/person-options';
import {YearFilterComponent} from '../../../shared/year-filter/year-filter.component';

@Component({
  selector: 'app-beruf-search',
  imports: [
    BerufListComponent,
    MatProgressSpinner,
    YearFilterComponent
  ],
  templateUrl: './beruf-search.component.html',
  styleUrl: './beruf-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BerufSearchComponent {

  private readonly berufController = inject(BerufControllerApi);
  private readonly router = inject(Router);
  private readonly personOptions = injectPersonOptions();

  /** Gewähltes Jahr; undefined => alle Jahre. */
  protected readonly jahr = signal<number | undefined>(undefined);

  // Die API kennt keinen Jahr-Parameter: einmal alles laden, im Client filtern
  private readonly berufeResource = rxResource({
    loader: () => this.berufController.getAllBeruf()
  });

  protected readonly loading = this.berufeResource.isLoading;
  protected readonly error = computed(() =>
    this.berufeResource.error() ? 'Konnte Berufe nicht laden.' : null
  );
  /** Person-ID -> "Name Vorname" für die Anzeige des Arbeitnehmers. */
  private readonly personNames = computed(() =>
    new Map(this.personOptions.options().map(o => [o.value, o.viewValue]))
  );

  protected readonly result = computed<BerufRow[]>(() => {
    const berufe = this.berufeResource.value() ?? [];
    const jahr = this.jahr();
    const names = this.personNames();
    return berufe
      .filter(b => jahr === undefined || b.jahr === jahr)
      .map(b => ({ ...b, arbeitnehmerName: names.get(b.arbeitnehmer) ?? '' }));
  });

  protected openBeruf(berufId: string): void {
    this.router.navigate(['/beruf/edit', berufId]);
  }
}
