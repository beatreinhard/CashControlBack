import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {VermoegenswertListComponent} from './vermoegenswert-list.component';
import {VermoegenswertDto} from '../../../generated';
import {rows} from '../../../testing/dom-helpers';

describe('VermoegenswertListComponent', () => {
  let component: VermoegenswertListComponent;
  let fixture: ComponentFixture<VermoegenswertListComponent>;

  const vermoegenswerte: VermoegenswertDto[] = [
    { id: 'v1', jahr: 2025, bezeichnung: 'Auto VW Golf', anschaffungsjahr: 2022, anschaffungspreis: 32000 },
    { id: 'v2', jahr: 2024, bezeichnung: 'E-Bike', anschaffungsjahr: 2023, anschaffungspreis: 4500 }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VermoegenswertListComponent],
      providers: [provideNoopAnimations()]
    }).compileComponents();

    fixture = TestBed.createComponent(VermoegenswertListComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('items', vermoegenswerte);
    fixture.detectChanges();
  });

  it('zeigt eine Zeile pro Vermögenswert', () => {
    expect(rows(fixture).length).toBe(2);
    expect(rows(fixture)[0].textContent).toContain('Auto VW Golf');
    expect(rows(fixture)[0].textContent).toContain('2022');
  });

  it('meldet die ID bei Klick und Enter', () => {
    const selected: string[] = [];
    component.vermoegenswertSelected.subscribe(id => selected.push(id));

    rows(fixture)[0].click();
    rows(fixture)[1].dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));

    expect(selected).toEqual(['v1', 'v2']);
  });
});
