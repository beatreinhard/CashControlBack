import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {KostenControllerApi} from '../../../generated';
import {KostenListComponent} from '../kosten-list/kosten-list.component';
import {YearFilterComponent} from '../../../shared/year-filter/year-filter.component';

@Component({
  selector: 'app-kosten-search',
  imports: [
    KostenListComponent,
    MatProgressSpinner,
    YearFilterComponent
  ],
  templateUrl: './kosten-search.component.html',
  styleUrl: './kosten-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class KostenSearchComponent {

  private readonly kostenController = inject(KostenControllerApi);
  private readonly router = inject(Router);

  /** Gewähltes Jahr; undefined => alle Jahre. */
  protected readonly jahr = signal<number | undefined>(undefined);

  private readonly kostenResource = rxResource({
    // Objekt statt nackter Zahl, damit auch "alle Jahre" (undefined) einen Request auslöst
    request: () => ({ jahr: this.jahr() }),
    loader: ({ request }) => this.kostenController.getAllKostenView(request.jahr)
  });

  protected readonly loading = this.kostenResource.isLoading;
  protected readonly error = computed(() =>
    this.kostenResource.error() ? 'Konnte Kosten nicht laden.' : null
  );
  protected readonly result = computed(() => this.kostenResource.value() ?? []);

  protected openKosten(kostenId: string): void {
    this.router.navigate(['/kosten/edit', kostenId]);
  }
}
