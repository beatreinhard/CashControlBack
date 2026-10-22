import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {ErbschaftListComponent} from './erbschaft-list.component';
import {ErbschaftArtDto, ErbschaftDto} from '../../../generated';
import {rows} from '../../../testing/dom-helpers';

describe('ErbschaftListComponent', () => {
  let component: ErbschaftListComponent;
  let fixture: ComponentFixture<ErbschaftListComponent>;

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
    await TestBed.configureTestingModule({
      imports: [ErbschaftListComponent],
      providers: [provideNoopAnimations()]
    }).compileComponents();

    fixture = TestBed.createComponent(ErbschaftListComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('items', erbschaften);
    fixture.detectChanges();
  });

  it('zeigt eine Zeile pro Erbschaft', () => {
    expect(rows(fixture).length).toBe(2);
    expect(rows(fixture)[0].textContent).toContain('Hans Muster');
    expect(rows(fixture)[0].textContent).toContain('31.03.2025');
  });

  it('meldet die ID bei Klick und Enter', () => {
    const selected: string[] = [];
    component.erbschaftSelected.subscribe(id => selected.push(id));

    rows(fixture)[0].click();
    rows(fixture)[1].dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));

    expect(selected).toEqual(['e1', 'e2']);
  });
});
