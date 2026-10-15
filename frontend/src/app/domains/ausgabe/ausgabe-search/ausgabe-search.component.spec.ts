import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {AusgabeSearchComponent} from './ausgabe-search.component';
import {AusgabeControllerApi, AusgabeKategorieDto, AusgabeViewDto} from '../../../generated';
import {el, rows, stabilize} from '../../../testing/dom-helpers';

describe('AusgabeSearchComponent', () => {
  let fixture: ComponentFixture<AusgabeSearchComponent>;
  let api: jasmine.SpyObj<AusgabeControllerApi>;

  const ausgaben: AusgabeViewDto[] = [
    { id: 'a1', datum: '2025-03-31', empfaenger: 'Migros', kategorie: AusgabeKategorieDto.Haushalt, betrag: 1 },
    { id: 'a2', datum: '2025-04-01', empfaenger: 'SBB', kategorie: AusgabeKategorieDto.Ferien, betrag: 2,
      zahlenderName: 'Gerber', zahlenderVorname: 'Max' }
  ];

  beforeEach(async () => {
    api = jasmine.createSpyObj<AusgabeControllerApi>('AusgabeControllerApi', ['getAllAusgabeView']);

    await TestBed.configureTestingModule({
      imports: [AusgabeSearchComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: AusgabeControllerApi, useValue: api }]
    }).compileComponents();
  });

  async function render(): Promise<void> {
    fixture = TestBed.createComponent(AusgabeSearchComponent);
    await stabilize(fixture);
  }

  function search(term: string): void {
    const input = el(fixture).querySelector<HTMLInputElement>('input[type="search"]')!;
    input.value = term;
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  }

  it('lädt alle Ausgaben und filtert nach Empfänger, Kategorie und Zahlender', async () => {
    api.getAllAusgabeView.and.returnValue(of(ausgaben) as never);
    await render();
    expect(rows(fixture).length).toBe(2);

    search('migros');
    expect(rows(fixture).length).toBe(1);

    search('ferien');
    expect(rows(fixture)[0].textContent).toContain('SBB');

    search('gerber');
    expect(rows(fixture)[0].textContent).toContain('SBB');
  });

  it('navigiert beim Auswählen zur Bearbeitung', async () => {
    api.getAllAusgabeView.and.returnValue(of(ausgaben) as never);
    await render();
    const navigate = spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);

    rows(fixture)[1].click();

    expect(navigate).toHaveBeenCalledWith(['/ausgabe/edit', 'a2']);
  });

  it('zeigt eine Fehlermeldung, wenn das Laden fehlschlägt', async () => {
    api.getAllAusgabeView.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).querySelector('.error')?.textContent).toContain('Konnte Ausgaben nicht laden.');
  });
});
