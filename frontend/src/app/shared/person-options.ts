import {computed, inject, Signal} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {map} from 'rxjs';
import {PersonControllerApi} from '../generated';

export interface SelectOption {
  value: string;
  viewValue: string;
}

export interface PersonOptions {
  options: Signal<SelectOption[]>;
  error: Signal<string | null>;
}

/**
 * Lädt alle Personen als Auswahl-Optionen (z.B. für "Zahlender").
 * Muss im Injection Context aufgerufen werden (Feld-Initializer oder Konstruktor).
 */
export function injectPersonOptions(): PersonOptions {
  const personController = inject(PersonControllerApi);
  const resource = rxResource({
    loader: () =>
      personController.getAllPerson().pipe(
        map(persons =>
          persons.map(p => ({
            value: String(p.id),
            viewValue: `${p.name} ${p.vorname}`.trim()
          }))
        )
      )
  });

  return {
    options: computed(() => resource.value() ?? []),
    error: computed(() => (resource.error() ? 'Personen konnten nicht geladen werden.' : null))
  };
}
