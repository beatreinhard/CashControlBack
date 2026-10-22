import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {GrundstueckunterhaltSearchComponent} from './grundstueckunterhalt-search.component';
import {GrundstueckunterhaltControllerApi, GrundstueckunterhaltDto} from '../../../generated';
import {el, rows, setInputValue, stabilize, submit} from '../../../testing/dom-helpers';

describe('GrundstueckunterhaltSearchComponent', () => {
  let fixture: ComponentFixture<GrundstueckunterhaltSearchComponent>;
  let api: jasmine.SpyObj<GrundstueckunterhaltControllerApi>;

  const eintraege: GrundstueckunterhaltDto[] = [
    {
      id: 'g1', jahr: 2025, rgDatum: '2025-04-15', ausfuehrendeFirma: 'Maler Muster AG', arbeitsArt: 'Fassade streichen',
      betragNetto: 12000, anteilAndereKosten: 2000, anteilUnterhalt: 10000
    },
    {
      id: 'g2', jahr: 2024, rgDatum: '2024-09-01', ausfuehrendeFirma: 'Gärtner GmbH', arbeitsArt: 'Hecke schneiden',
      betragNetto: 800, anteilAndereKosten: 0, anteilUnterhalt: 800
    }
  ];

  beforeEach(async () => {
    api = jasmine.createSpyObj<GrundstueckunterhaltControllerApi>(
      'GrundstueckunterhaltControllerApi', ['getAllGrundstueckunterhalt']
    );

    await TestBed.configureTestingModule({
      imports: [GrundstueckunterhaltSearchComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: GrundstueckunterhaltControllerApi, useValue: api }
      ]
    }).compileComponents();
  });

  async function render(): Promise<void> {
    fixture = TestBed.createComponent(GrundstueckunterhaltSearchComponent);
    await stabilize(fixture);
  }

  it('zeigt zuerst alle Jahre und filtert danach im Client', async () => {
    api.getAllGrundstueckunterhalt.and.returnValue(of(eintraege) as never);
    await render();
    expect(rows(fixture).length).toBe(2);

    setInputValue(fixture, 'jahr', '2025');
    submit(fixture);
    await stabilize(fixture);

    expect(rows(fixture).length).toBe(1);
    expect(rows(fixture)[0].textContent).toContain('Maler Muster AG');
    expect(api.getAllGrundstueckunterhalt).toHaveBeenCalledTimes(1);
  });

  it('navigiert beim Auswählen zur Bearbeitung', async () => {
    api.getAllGrundstueckunterhalt.and.returnValue(of(eintraege) as never);
    await render();
    const navigate = spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);

    rows(fixture)[0].click();

    expect(navigate).toHaveBeenCalledWith(['/grundstueckunterhalt/edit', 'g1']);
  });

  it('zeigt eine Fehlermeldung, wenn das Laden fehlschlägt', async () => {
    api.getAllGrundstueckunterhalt.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).querySelector('.error')?.textContent).toContain('Konnte Grundstückunterhalt nicht laden.');
  });
});
