import {Component, computed, inject} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {PersonControllerApi} from '../../../generated';
import {PersonListComponent} from '../person-list/person-list.component';

@Component({
  selector: 'app-person-search',
  imports: [
    MatProgressSpinner,
    PersonListComponent
  ],
  templateUrl: './person-search.component.html',
  styleUrl: './person-search.component.css'
})
export class PersonSearchComponent {

  private readonly personController = inject(PersonControllerApi);

  private readonly personsResource = rxResource({
    loader: () => this.personController.getAllPerson()
  });

  protected readonly loading = this.personsResource.isLoading;
  protected readonly error = computed(() =>
    this.personsResource.error() ? 'Konnte Personen nicht laden.' : null
  );
  protected readonly result = computed(() => this.personsResource.value() ?? []);
}
