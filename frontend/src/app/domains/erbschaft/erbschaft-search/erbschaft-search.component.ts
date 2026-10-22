import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {ErbschaftControllerApi} from '../../../generated';
import {ErbschaftListComponent} from '../erbschaft-list/erbschaft-list.component';
import {YearFilterComponent} from '../../../shared/year-filter/year-filter.component';

@Component({
  selector: 'app-erbschaft-search',
  imports: [
    ErbschaftListComponent,
    MatProgressSpinner,
    YearFilterComponent
  ],
  templateUrl: './erbschaft-search.component.html',
  styleUrl: './erbschaft-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ErbschaftSearchComponent {

  private readonly erbschaftController = inject(ErbschaftControllerApi);
  private readonly router = inject(Router);

  /** Gewähltes Jahr; undefined => alle Jahre. */
  protected readonly jahr = signal<number | undefined>(undefined);

  // Die API kennt keinen Jahr-Parameter: einmal alles laden, im Client filtern
  private readonly erbschaftenResource = rxResource({
    loader: () => this.erbschaftController.getAllErbschaft()
  });

  protected readonly loading = this.erbschaftenResource.isLoading;
  protected readonly error = computed(() =>
    this.erbschaftenResource.error() ? 'Konnte Erbschaften nicht laden.' : null
  );
  protected readonly result = computed(() => {
    const erbschaften = this.erbschaftenResource.value() ?? [];
    const jahr = this.jahr();
    return jahr === undefined ? erbschaften : erbschaften.filter(e => e.jahr === jahr);
  });

  protected openErbschaft(erbschaftId: string): void {
    this.router.navigate(['/erbschaft/edit', erbschaftId]);
  }
}
