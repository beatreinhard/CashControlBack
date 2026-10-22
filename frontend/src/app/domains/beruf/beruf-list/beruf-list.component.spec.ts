import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {BerufListComponent, BerufRow} from './beruf-list.component';
import {rows} from '../../../testing/dom-helpers';

describe('BerufListComponent', () => {
  let component: BerufListComponent;
  let fixture: ComponentFixture<BerufListComponent>;

  const berufe: BerufRow[] = [
    {
      id: 'b1', jahr: 2025, arbeitnehmer: 'p1', arbeitnehmerName: 'Reinhard Beat', arbeitgeber: 'Muster AG', arbeitsort: 'Bern',
      beschaeftigungsgrad: 100, arbeitstage: 220, fahrtkilometerProTag: 20, ansatzAuswaertigeVerpflegung: 15
    },
    {
      id: 'b2', jahr: 2024, arbeitnehmer: 'p1', arbeitnehmerName: 'Reinhard Beat', arbeitgeber: 'Alt AG', arbeitsort: 'Thun',
      beschaeftigungsgrad: 80, arbeitstage: 180, ansatzAuswaertigeVerpflegung: 15
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BerufListComponent],
      providers: [provideNoopAnimations()]
    }).compileComponents();

    fixture = TestBed.createComponent(BerufListComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('items', berufe);
    fixture.detectChanges();
  });

  it('zeigt eine Zeile pro Beruf', () => {
    expect(rows(fixture).length).toBe(2);
    expect(rows(fixture)[0].textContent).toContain('Muster AG');
    expect(rows(fixture)[0].textContent).toContain('Reinhard Beat');
  });

  it('meldet die ID bei Klick und Enter', () => {
    const selected: string[] = [];
    component.berufSelected.subscribe(id => selected.push(id));

    rows(fixture)[0].click();
    rows(fixture)[1].dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));

    expect(selected).toEqual(['b1', 'b2']);
  });
});
