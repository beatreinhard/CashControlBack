import {Component, inject, OnInit, signal} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {PersonControllerApi, PersonDto} from '../../../generated';
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
export class PersonSearchComponent implements OnInit {

  readonly loading = signal(true);
  readonly error = signal<string | undefined>("");
  readonly result = signal<PersonDto[]>([]);

  protected personController = inject(PersonControllerApi);

  ngOnInit(): void {
    this.personController.getAllPerson().subscribe({
      next: (res) => {
        this.result.set((res as PersonDto[]) ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('API error:', err);
        this.error.set('Konnte Personen nicht laden.');
        this.loading.set(false);
      },
    });
  }
}
