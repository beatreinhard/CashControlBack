import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {PersonSearchComponent} from './person-search.component';
import {PersonControllerApi, PersonDto} from '../../../generated';

describe('PersonSearchComponent', () => {
  let fixture: ComponentFixture<PersonSearchComponent>;
  let api: jasmine.SpyObj<PersonControllerApi>;

  const persons: PersonDto[] = [
    { id: 'p1', name: 'Gerber', vorname: 'Max', geburtsdatum: '1980-05-17', ahvnummer: '756.1234.5678.90' },
    { id: 'p2', name: 'Arnold', vorname: 'Anna', geburtsdatum: '1990-01-02' }
  ];

  beforeEach(async () => {
    api = jasmine.createSpyObj<PersonControllerApi>('PersonControllerApi', ['getAllPerson']);

    await TestBed.configureTestingModule({
      imports: [PersonSearchComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: PersonControllerApi, useValue: api }
      ]
    })
    .compileComponents();
  });

  async function render(): Promise<void> {
    fixture = TestBed.createComponent(PersonSearchComponent);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
  }

  function element(): HTMLElement {
    return fixture.nativeElement as HTMLElement;
  }

  function rows(): HTMLElement[] {
    return Array.from(element().querySelectorAll<HTMLElement>('tr.person-row'));
  }

  function search(term: string): void {
    const input = element().querySelector<HTMLInputElement>('input[type="search"]')!;
    input.value = term;
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  }

  it('lädt alle Personen und zeigt sie an', async () => {
    api.getAllPerson.and.returnValue(of(persons) as never);
    await render();

    expect(api.getAllPerson).toHaveBeenCalledTimes(1);
    expect(rows().length).toBe(2);
  });

  it('filtert nach Name, Vorname und AHV-Nummer', async () => {
    api.getAllPerson.and.returnValue(of(persons) as never);
    await render();

    search('anna');
    expect(rows().length).toBe(1);
    expect(rows()[0].textContent).toContain('Arnold');

    search('756.1234');
    expect(rows().length).toBe(1);
    expect(rows()[0].textContent).toContain('Gerber');

    search('');
    expect(rows().length).toBe(2);
  });

  it('navigiert beim Auswählen einer Person zur Bearbeitung', async () => {
    api.getAllPerson.and.returnValue(of(persons) as never);
    await render();
    const navigate = spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);

    rows()[0].click();

    expect(navigate).toHaveBeenCalledWith(['/person/edit', 'p1']);
  });

  it('zeigt eine Fehlermeldung, wenn das Laden fehlschlägt', async () => {
    api.getAllPerson.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(element().querySelector('.error')?.textContent).toContain('Konnte Personen nicht laden.');
    expect(element().querySelector('app-person-list')).toBeNull();
  });
});
