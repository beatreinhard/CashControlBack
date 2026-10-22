import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {VermoegenswertSearchComponent} from './vermoegenswert-search.component';
import {VermoegenswertControllerApi, VermoegenswertDto} from '../../../generated';
import {el, rows, setInputValue, stabilize, submit} from '../../../testing/dom-helpers';

describe('VermoegenswertSearchComponent', () => {
  let fixture: ComponentFixture<VermoegenswertSearchComponent>;
  let api: jasmine.SpyObj<VermoegenswertControllerApi>;

  const vermoegenswerte: VermoegenswertDto[] = [
    { id: 'v1', jahr: 2025, bezeichnung: 'Auto VW Golf', anschaffungsjahr: 2022, anschaffungspreis: 32000 },
    { id: 'v2', jahr: 2024, bezeichnung: 'E-Bike', anschaffungsjahr: 2023, anschaffungspreis: 4500 }
  ];

  beforeEach(async () => {
    api = jasmine.createSpyObj<VermoegenswertControllerApi>('VermoegenswertControllerApi', ['getAllVermoegenswert']);

    await TestBed.configureTestingModule({
      imports: [VermoegenswertSearchComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: VermoegenswertControllerApi, useValue: api }]
    }).compileComponents();
  });

  async function render(): Promise<void> {
    fixture = TestBed.createComponent(VermoegenswertSearchComponent);
    await stabilize(fixture);
  }

  it('zeigt zuerst alle Jahre und filtert danach im Client', async () => {
    api.getAllVermoegenswert.and.returnValue(of(vermoegenswerte) as never);
    await render();
    expect(rows(fixture).length).toBe(2);

    setInputValue(fixture, 'jahr', '2025');
    submit(fixture);
    await stabilize(fixture);

    expect(rows(fixture).length).toBe(1);
    expect(rows(fixture)[0].textContent).toContain('Auto VW Golf');
    expect(api.getAllVermoegenswert).toHaveBeenCalledTimes(1);
  });

  it('navigiert beim Auswählen zur Bearbeitung', async () => {
    api.getAllVermoegenswert.and.returnValue(of(vermoegenswerte) as never);
    await render();
    const navigate = spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);

    rows(fixture)[0].click();

    expect(navigate).toHaveBeenCalledWith(['/vermoegenswert/edit', 'v1']);
  });

  it('zeigt eine Fehlermeldung, wenn das Laden fehlschlägt', async () => {
    api.getAllVermoegenswert.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).querySelector('.error')?.textContent).toContain('Konnte Vermögenswerte nicht laden.');
  });
});
