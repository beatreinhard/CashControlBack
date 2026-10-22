import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {GrundstueckunterhaltListComponent} from './grundstueckunterhalt-list.component';
import {GrundstueckunterhaltDto} from '../../../generated';
import {rows} from '../../../testing/dom-helpers';

describe('GrundstueckunterhaltListComponent', () => {
  let component: GrundstueckunterhaltListComponent;
  let fixture: ComponentFixture<GrundstueckunterhaltListComponent>;

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
    await TestBed.configureTestingModule({
      imports: [GrundstueckunterhaltListComponent],
      providers: [provideNoopAnimations()]
    }).compileComponents();

    fixture = TestBed.createComponent(GrundstueckunterhaltListComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('items', eintraege);
    fixture.detectChanges();
  });

  it('zeigt eine Zeile pro Eintrag', () => {
    expect(rows(fixture).length).toBe(2);
    expect(rows(fixture)[0].textContent).toContain('Maler Muster AG');
    expect(rows(fixture)[0].textContent).toContain('15.04.2025');
  });

  it('meldet die ID bei Klick und Enter', () => {
    const selected: string[] = [];
    component.grundstueckunterhaltSelected.subscribe(id => selected.push(id));

    rows(fixture)[0].click();
    rows(fixture)[1].dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));

    expect(selected).toEqual(['g1', 'g2']);
  });
});
