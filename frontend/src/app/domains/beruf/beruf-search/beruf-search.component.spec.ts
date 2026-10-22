import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {BerufSearchComponent} from './beruf-search.component';
import {BerufControllerApi, BerufDto, PersonControllerApi, PersonDto} from '../../../generated';
import {el, rows, setInputValue, stabilize, submit} from '../../../testing/dom-helpers';

describe('BerufSearchComponent', () => {
  let fixture: ComponentFixture<BerufSearchComponent>;
  let api: jasmine.SpyObj<BerufControllerApi>;
  let personApi: jasmine.SpyObj<PersonControllerApi>;

  const persons: PersonDto[] = [
    { id: 'p1', name: 'Reinhard', vorname: 'Beat', geburtsdatum: '1970-01-01' }
  ];

  const berufe: BerufDto[] = [
    {
      id: 'b1', jahr: 2025, arbeitnehmer: 'p1', arbeitgeber: 'Muster AG', arbeitsort: 'Bern',
      beschaeftigungsgrad: 100, arbeitstage: 220, ansatzAuswaertigeVerpflegung: 15
    },
    {
      id: 'b2', jahr: 2024, arbeitnehmer: 'p1', arbeitgeber: 'Alt AG', arbeitsort: 'Thun',
      beschaeftigungsgrad: 80, arbeitstage: 180, ansatzAuswaertigeVerpflegung: 15
    }
  ];

  beforeEach(async () => {
    api = jasmine.createSpyObj<BerufControllerApi>('BerufControllerApi', ['getAllBeruf']);
    personApi = jasmine.createSpyObj<PersonControllerApi>('PersonControllerApi', ['getAllPerson']);
    personApi.getAllPerson.and.returnValue(of(persons) as never);

    await TestBed.configureTestingModule({
      imports: [BerufSearchComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: BerufControllerApi, useValue: api },
        { provide: PersonControllerApi, useValue: personApi }]
    }).compileComponents();
  });

  async function render(): Promise<void> {
    fixture = TestBed.createComponent(BerufSearchComponent);
    await stabilize(fixture);
  }

  it('zeigt zuerst alle Jahre und filtert danach im Client', async () => {
    api.getAllBeruf.and.returnValue(of(berufe) as never);
    await render();
    expect(rows(fixture).length).toBe(2);
    expect(rows(fixture)[0].textContent).toContain('Reinhard Beat');

    setInputValue(fixture, 'jahr', '2025');
    submit(fixture);
    await stabilize(fixture);

    expect(rows(fixture).length).toBe(1);
    expect(rows(fixture)[0].textContent).toContain('Muster AG');
    expect(api.getAllBeruf).toHaveBeenCalledTimes(1);
  });

  it('navigiert beim Auswählen zur Bearbeitung', async () => {
    api.getAllBeruf.and.returnValue(of(berufe) as never);
    await render();
    const navigate = spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);

    rows(fixture)[0].click();

    expect(navigate).toHaveBeenCalledWith(['/beruf/edit', 'b1']);
  });

  it('zeigt eine Fehlermeldung, wenn das Laden fehlschlägt', async () => {
    api.getAllBeruf.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).querySelector('.error')?.textContent).toContain('Konnte Berufe nicht laden.');
  });
});
