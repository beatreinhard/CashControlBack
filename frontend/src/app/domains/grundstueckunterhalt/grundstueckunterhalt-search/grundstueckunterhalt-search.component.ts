import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {GrundstueckunterhaltControllerApi} from '../../../generated';
import {GrundstueckunterhaltListComponent} from '../grundstueckunterhalt-list/grundstueckunterhalt-list.component';
import {YearFilterComponent} from '../../../shared/year-filter/year-filter.component';

@Component({
  selector: 'app-grundstueckunterhalt-search',
  imports: [
    GrundstueckunterhaltListComponent,
    MatProgressSpinner,
    YearFilterComponent
  ],
  templateUrl: './grundstueckunterhalt-search.component.html',
  styleUrl: './grundstueckunterhalt-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GrundstueckunterhaltSearchComponent {

  private readonly grundstueckunterhaltController = inject(GrundstueckunterhaltControllerApi);
  private readonly router = inject(Router);

  /** Gewähltes Jahr; undefined => alle Jahre. */
  protected readonly jahr = signal<number | undefined>(undefined);

  // Die API kennt keinen Jahr-Parameter: einmal alles laden, im Client filtern
  private readonly eintraegeResource = rxResource({
    loader: () => this.grundstueckunterhaltController.getAllGrundstueckunterhalt()
  });

  protected readonly loading = this.eintraegeResource.isLoading;
  protected readonly error = computed(() =>
    this.eintraegeResource.error() ? 'Konnte Grundstückunterhalt nicht laden.' : null
  );
  protected readonly result = computed(() => {
    const eintraege = this.eintraegeResource.value() ?? [];
    const jahr = this.jahr();
    return jahr === undefined ? eintraege : eintraege.filter(g => g.jahr === jahr);
  });

  protected openGrundstueckunterhalt(grundstueckunterhaltId: string): void {
    this.router.navigate(['/grundstueckunterhalt/edit', grundstueckunterhaltId]);
  }
}
