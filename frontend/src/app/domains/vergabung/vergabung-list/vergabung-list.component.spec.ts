import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {VergabungListComponent} from './vergabung-list.component';
import {VergabungDto} from '../../../generated';
import {el, rows} from '../../../testing/dom-helpers';

describe('VergabungListComponent', () => {
  let fixture: ComponentFixture<VergabungListComponent>;

  const vergabungen: VergabungDto[] = [
    { id: 'v1', jahr: 2025, zahlungsDatum: '2025-06-01', empfaenger: 'Rotes Kreuz', betrag: 100 },
    { id: 'v2', jahr: 2025, zahlungsDatum: '2025-01-15', empfaenger: 'WWF', betrag: 50 }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VergabungListComponent],
      providers: [provideNoopAnimations()]
    }).compileComponents();

    fixture = TestBed.createComponent(VergabungListComponent);
    fixture.componentRef.setInput('items', vergabungen);
    fixture.detectChanges();
  });

  it('zeigt eine Zeile pro Vergabung mit Datum', () => {
    expect(rows(fixture).length).toBe(2);
    expect(rows(fixture)[0].textContent).toContain('01.06.2025');
  });

  it('sortiert nach Zahlungsdatum', () => {
    el(fixture).querySelector<HTMLElement>('th.mat-column-zahlungsDatum .mat-sort-header-container')!.click();
    fixture.detectChanges();

    expect(rows(fixture)[0].textContent).toContain('WWF');
  });
});
