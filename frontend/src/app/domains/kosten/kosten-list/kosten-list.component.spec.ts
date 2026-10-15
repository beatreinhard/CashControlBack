import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {KostenListComponent} from './kosten-list.component';
import {KostenArtDto, KostenViewDto} from '../../../generated';
import {rows} from '../../../testing/dom-helpers';

describe('KostenListComponent', () => {
  let component: KostenListComponent;
  let fixture: ComponentFixture<KostenListComponent>;

  const kosten: KostenViewDto[] = [
    { id: 'k1', jahr: 2025, art: KostenArtDto.Krankenkasse, zahlender: 'p1', empfaenger: 'CSS', betrag: 300,
      zahlenderName: 'Gerber', zahlenderVorname: 'Max' },
    { id: 'k2', jahr: 2024, art: KostenArtDto.Beruf, zahlender: '', empfaenger: 'SBB', betrag: 50 }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KostenListComponent],
      providers: [provideNoopAnimations()]
    }).compileComponents();

    fixture = TestBed.createComponent(KostenListComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('items', kosten);
    fixture.detectChanges();
  });

  it('zeigt eine Zeile pro Eintrag inkl. Zahlender', () => {
    expect(rows(fixture).length).toBe(2);
    expect(rows(fixture)[0].textContent).toContain('Gerber Max');
    expect(rows(fixture)[0].textContent).toContain('300.00');
  });

  it('meldet die ID bei Klick', () => {
    const selected: string[] = [];
    component.kostenSelected.subscribe(id => selected.push(id));

    rows(fixture)[1].click();

    expect(selected).toEqual(['k2']);
  });
});
