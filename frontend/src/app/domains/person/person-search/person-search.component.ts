import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {PersonControllerApi} from '../../../generated';
import {PersonListComponent} from '../person-list/person-list.component';

@Component({
  selector: 'app-person-search',
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    MatProgressSpinner,
    PersonListComponent
  ],
  templateUrl: './person-search.component.html',
  styleUrl: './person-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PersonSearchComponent {

  private readonly personController = inject(PersonControllerApi);
  private readonly router = inject(Router);

  private readonly personsResource = rxResource({
    loader: () => this.personController.getAllPerson()
  });

  protected readonly searchTerm = signal('');

  protected readonly loading = this.personsResource.isLoading;
  protected readonly error = computed(() =>
    this.personsResource.error() ? 'Konnte Personen nicht laden.' : null
  );
  protected readonly result = computed(() => {
    const persons = this.personsResource.value() ?? [];
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) return persons;
    return persons.filter(p =>
      [p.name, p.vorname, p.ahvnummer].some(value => value?.toLowerCase().includes(term))
    );
  });

  protected onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
  }

  protected openPerson(personId: string): void {
    this.router.navigate(['/person/edit', personId]);
  }
}
