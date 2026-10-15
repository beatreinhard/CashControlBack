import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {KostenSearchComponent} from './kosten-search.component';
import {KostenArtDto, KostenControllerApi, KostenViewDto} from '../../../generated';
import {el, rows, setInputValue, stabilize, submit} from '../../../testing/dom-helpers';

describe('KostenSearchComponent', () => {
  let fixture: ComponentFixture<KostenSearchComponent>;
  let api: jasmine.SpyObj<KostenControllerApi>;

  const kosten: KostenViewDto[] = [
    { id: 'k1', jahr: 2025, art: KostenArtDto.Krankenkasse, zahlender: 'p1', empfaenger: 'CSS', betrag: 300 }
  ];

  beforeEach(async () => {
    api = jasmine.createSpyObj<KostenControllerApi>('KostenControllerApi', ['getAllKostenView']);

    await TestBed.configureTestingModule({
      imports: [KostenSearchComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: KostenControllerApi, useValue: api }]
    }).compileComponents();
  });

  async function render(): Promise<void> {
    fixture = TestBed.createComponent(KostenSearchComponent);
    await stabilize(fixture);
  }

  it('lädt zuerst alle Jahre und danach das gefilterte Jahr', async () => {
    api.getAllKostenView.and.returnValue(of(kosten) as never);
    await render();
    expect(api.getAllKostenView).toHaveBeenCalledWith(undefined);
    expect(rows(fixture).length).toBe(1);

    setInputValue(fixture, 'jahr', '2025');
    submit(fixture);
    await stabilize(fixture);

    expect(api.getAllKostenView).toHaveBeenCalledWith(2025);
  });

  it('sendet keinen Request bei ungültigem Jahr', async () => {
    api.getAllKostenView.and.returnValue(of(kosten) as never);
    await render();
    api.getAllKostenView.calls.reset();

    setInputValue(fixture, 'jahr', '1999');
    submit(fixture);
    await stabilize(fixture);

    expect(api.getAllKostenView).not.toHaveBeenCalled();
  });

  it('navigiert beim Auswählen zur Bearbeitung', async () => {
    api.getAllKostenView.and.returnValue(of(kosten) as never);
    await render();
    const navigate = spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);

    rows(fixture)[0].click();

    expect(navigate).toHaveBeenCalledWith(['/kosten/edit', 'k1']);
  });

  it('zeigt eine Fehlermeldung, wenn das Laden fehlschlägt', async () => {
    api.getAllKostenView.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).querySelector('.error')?.textContent).toContain('Konnte Kosten nicht laden.');
  });
});
