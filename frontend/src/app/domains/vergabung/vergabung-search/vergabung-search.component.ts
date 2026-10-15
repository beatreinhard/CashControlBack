import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {VergabungControllerApi} from '../../../generated';
import {VergabungListComponent} from '../vergabung-list/vergabung-list.component';
import {YearFilterComponent} from '../../../shared/year-filter/year-filter.component';

@Component({
  selector: 'app-vergabung-search',
  imports: [
    MatProgressSpinner,
    VergabungListComponent,
    YearFilterComponent
  ],
  templateUrl: './vergabung-search.component.html',
  styleUrl: './vergabung-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VergabungSearchComponent {

  private readonly vergabungController = inject(VergabungControllerApi);

  /** Gewähltes Jahr; undefined => alle Jahre. */
  protected readonly jahr = signal<number | undefined>(undefined);

  private readonly vergabungenResource = rxResource({
    // Objekt statt nackter Zahl, damit auch "alle Jahre" (undefined) einen Request auslöst
    request: () => ({ jahr: this.jahr() }),
    loader: ({ request }) => this.vergabungController.getVergabungen(request.jahr)
  });

  protected readonly loading = this.vergabungenResource.isLoading;
  protected readonly error = computed(() =>
    this.vergabungenResource.error() ? 'Konnte Vergabungen nicht laden.' : null
  );
  protected readonly result = computed(() => this.vergabungenResource.value() ?? []);
}
