import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {AusgabeListComponent} from './ausgabe-list.component';
import {AusgabeKategorieDto, AusgabeViewDto} from '../../../generated';
import {el, rows} from '../../../testing/dom-helpers';

describe('AusgabeListComponent', () => {
  let component: AusgabeListComponent;
  let fixture: ComponentFixture<AusgabeListComponent>;

  const ausgaben: AusgabeViewDto[] = [
    { id: 'a1', datum: '2025-03-31', empfaenger: 'Migros', kategorie: AusgabeKategorieDto.Haushalt, betrag: 1234.5,
      zahlender: 'p2', zahlenderName: 'Zürcher', zahlenderVorname: 'Anna' },
    { id: 'a2', datum: '2025-04-01', empfaenger: 'SBB', kategorie: AusgabeKategorieDto.Ferien, betrag: 10,
      zahlender: 'p1', zahlenderName: 'Arnold', zahlenderVorname: 'Max' }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AusgabeListComponent],
      providers: [provideNoopAnimations()]
    }).compileComponents();

    fixture = TestBed.createComponent(AusgabeListComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('items', ausgaben);
    fixture.detectChanges();
  });

  it('zeigt eine Zeile pro Ausgabe mit Zahlender und formatiertem Betrag', () => {
    expect(rows(fixture).length).toBe(2);
    expect(rows(fixture)[0].textContent).toContain('Zürcher Anna');
    // Tausendertrennzeichen hängt von der Locale ab (Test: en-US, App: de-CH)
    expect(rows(fixture)[0].textContent).toContain('234.50');
  });

  it('sortiert den Zahlender nach Namen statt nach ID', () => {
    el(fixture).querySelector<HTMLElement>('th.mat-column-zahlender .mat-sort-header-container')!.click();
    fixture.detectChanges();

    expect(rows(fixture)[0].textContent).toContain('Arnold Max');
  });

  it('meldet die ID bei Klick und Enter', () => {
    const selected: string[] = [];
    component.ausgabeSelected.subscribe(id => selected.push(id));

    rows(fixture)[0].click();
    rows(fixture)[1].dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));

    expect(selected).toEqual(['a1', 'a2']);
  });
});
