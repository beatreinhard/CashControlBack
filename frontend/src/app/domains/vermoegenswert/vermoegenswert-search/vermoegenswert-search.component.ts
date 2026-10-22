import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {VermoegenswertControllerApi} from '../../../generated';
import {VermoegenswertListComponent} from '../vermoegenswert-list/vermoegenswert-list.component';
import {YearFilterComponent} from '../../../shared/year-filter/year-filter.component';

@Component({
  selector: 'app-vermoegenswert-search',
  imports: [
    VermoegenswertListComponent,
    MatProgressSpinner,
    YearFilterComponent
  ],
  templateUrl: './vermoegenswert-search.component.html',
  styleUrl: './vermoegenswert-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VermoegenswertSearchComponent {

  private readonly vermoegenswertController = inject(VermoegenswertControllerApi);
  private readonly router = inject(Router);

  /** Gewähltes Jahr; undefined => alle Jahre. */
  protected readonly jahr = signal<number | undefined>(undefined);

  // Die API kennt keinen Jahr-Parameter: einmal alles laden, im Client filtern
  private readonly vermoegenswerteResource = rxResource({
    loader: () => this.vermoegenswertController.getAllVermoegenswert()
  });

  protected readonly loading = this.vermoegenswerteResource.isLoading;
  protected readonly error = computed(() =>
    this.vermoegenswerteResource.error() ? 'Konnte Vermögenswerte nicht laden.' : null
  );
  protected readonly result = computed(() => {
    const vermoegenswerte = this.vermoegenswerteResource.value() ?? [];
    const jahr = this.jahr();
    return jahr === undefined ? vermoegenswerte : vermoegenswerte.filter(v => v.jahr === jahr);
  });

  protected openVermoegenswert(vermoegenswertId: string): void {
    this.router.navigate(['/vermoegenswert/edit', vermoegenswertId]);
  }
}
