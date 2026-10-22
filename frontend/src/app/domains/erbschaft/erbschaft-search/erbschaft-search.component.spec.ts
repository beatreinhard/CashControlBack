import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {ErbschaftSearchComponent} from './erbschaft-search.component';
import {ErbschaftArtDto, ErbschaftControllerApi, ErbschaftDto} from '../../../generated';
import {el, rows, setInputValue, stabilize, submit} from '../../../testing/dom-helpers';

describe('ErbschaftSearchComponent', () => {
  let fixture: ComponentFixture<ErbschaftSearchComponent>;
  let api: jasmine.SpyObj<ErbschaftControllerApi>;

  const erbschaften: ErbschaftDto[] = [
    {
      id: 'e1', jahr: 2025, art: ErbschaftArtDto.Erbschaft, betrag: 50000, datum: '2025-03-31',
      geber: 'Hans Muster', verwandtschaftsverhaeltnis: 'Vater', gegenstand: 'Bargeld'
    },
    {
      id: 'e2', jahr: 2024, art: ErbschaftArtDto.SchenkungVorempfang, betrag: 10000, datum: '2024-06-01',
      geber: 'Anna Muster', verwandtschaftsverhaeltnis: 'Tante', gegenstand: 'Wertschriften'
    }
  ];

  beforeEach(async () => {
    api = jasmine.createSpyObj<ErbschaftControllerApi>('ErbschaftControllerApi', ['getAllErbschaft']);

    await TestBed.configureTestingModule({
      imports: [ErbschaftSearchComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: ErbschaftControllerApi, useValue: api }]
    }).compileComponents();
  });

  async function render(): Promise<void> {
    fixture = TestBed.createComponent(ErbschaftSearchComponent);
    await stabilize(fixture);
  }

  it('zeigt zuerst alle Jahre und filtert danach im Client', async () => {
    api.getAllErbschaft.and.returnValue(of(erbschaften) as never);
    await render();
    expect(rows(fixture).length).toBe(2);

    setInputValue(fixture, 'jahr', '2025');
    submit(fixture);
    await stabilize(fixture);

    expect(rows(fixture).length).toBe(1);
    expect(rows(fixture)[0].textContent).toContain('Hans Muster');
    expect(api.getAllErbschaft).toHaveBeenCalledTimes(1);
  });

  it('navigiert beim Auswählen zur Bearbeitung', async () => {
    api.getAllErbschaft.and.returnValue(of(erbschaften) as never);
    await render();
    const navigate = spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);

    rows(fixture)[0].click();

    expect(navigate).toHaveBeenCalledWith(['/erbschaft/edit', 'e1']);
  });

  it('zeigt eine Fehlermeldung, wenn das Laden fehlschlägt', async () => {
    api.getAllErbschaft.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).querySelector('.error')?.textContent).toContain('Konnte Erbschaften nicht laden.');
  });
});
