import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {SchuldControllerApi} from '../../../generated';
import {SchuldListComponent} from '../schuld-list/schuld-list.component';
import {YearFilterComponent} from '../../../shared/year-filter/year-filter.component';

@Component({
  selector: 'app-schuld-search',
  imports: [
    SchuldListComponent,
    MatProgressSpinner,
    YearFilterComponent
  ],
  templateUrl: './schuld-search.component.html',
  styleUrl: './schuld-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SchuldSearchComponent {

  private readonly schuldController = inject(SchuldControllerApi);
  private readonly router = inject(Router);

  /** Gewähltes Jahr; undefined => alle Jahre. */
  protected readonly jahr = signal<number | undefined>(undefined);

  private readonly schuldenResource = rxResource({
    // Objekt statt nackter Zahl, damit auch "alle Jahre" (undefined) einen Request auslöst
    request: () => ({ jahr: this.jahr() }),
    loader: ({ request }) => this.schuldController.getSchulden(request.jahr)
  });

  protected readonly loading = this.schuldenResource.isLoading;
  protected readonly error = computed(() =>
    this.schuldenResource.error() ? 'Konnte Schulden nicht laden.' : null
  );
  protected readonly result = computed(() => this.schuldenResource.value() ?? []);

  protected openSchuld(schuldId: string): void {
    this.router.navigate(['/schuld/edit', schuldId]);
  }
}
