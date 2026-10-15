import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {VergabungSearchComponent} from './vergabung-search.component';
import {VergabungControllerApi, VergabungDto} from '../../../generated';
import {el, rows, setInputValue, stabilize, submit} from '../../../testing/dom-helpers';

describe('VergabungSearchComponent', () => {
  let fixture: ComponentFixture<VergabungSearchComponent>;
  let api: jasmine.SpyObj<VergabungControllerApi>;

  const vergabungen: VergabungDto[] = [
    { id: 'v1', jahr: 2025, zahlungsDatum: '2025-06-01', empfaenger: 'Rotes Kreuz', betrag: 100 }
  ];

  beforeEach(async () => {
    api = jasmine.createSpyObj<VergabungControllerApi>('VergabungControllerApi', ['getVergabungen']);

    await TestBed.configureTestingModule({
      imports: [VergabungSearchComponent],
      providers: [provideNoopAnimations(), { provide: VergabungControllerApi, useValue: api }]
    }).compileComponents();
  });

  async function render(): Promise<void> {
    fixture = TestBed.createComponent(VergabungSearchComponent);
    await stabilize(fixture);
  }

  it('lädt zuerst alle Jahre und danach das gefilterte Jahr', async () => {
    api.getVergabungen.and.returnValue(of(vergabungen) as never);
    await render();
    expect(api.getVergabungen).toHaveBeenCalledWith(undefined);
    expect(rows(fixture).length).toBe(1);

    setInputValue(fixture, 'jahr', '2025');
    submit(fixture);
    await stabilize(fixture);

    expect(api.getVergabungen).toHaveBeenCalledWith(2025);
  });

  it('zeigt eine Fehlermeldung, wenn das Laden fehlschlägt', async () => {
    api.getVergabungen.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).querySelector('.error')?.textContent).toContain('Konnte Vergabungen nicht laden.');
  });
});
