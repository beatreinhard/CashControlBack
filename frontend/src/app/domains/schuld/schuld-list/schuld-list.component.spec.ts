import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {SchuldListComponent} from './schuld-list.component';
import {SchuldArtDto, SchuldDto} from '../../../generated';
import {rows} from '../../../testing/dom-helpers';

describe('SchuldListComponent', () => {
  let component: SchuldListComponent;
  let fixture: ComponentFixture<SchuldListComponent>;

  const schulden: SchuldDto[] = [
    { id: 's1', jahr: 2025, art: SchuldArtDto.Hypothek, glaeubiger: 'UBS', betrag: 500000, zinsen: 7500 },
    { id: 's2', jahr: 2024, art: SchuldArtDto.Rechnung, glaeubiger: 'Swisscom', betrag: 99 }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchuldListComponent],
      providers: [provideNoopAnimations()]
    }).compileComponents();

    fixture = TestBed.createComponent(SchuldListComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('items', schulden);
    fixture.detectChanges();
  });

  it('zeigt eine Zeile pro Schuld', () => {
    expect(rows(fixture).length).toBe(2);
    expect(rows(fixture)[0].textContent).toContain('UBS');
  });

  it('meldet die ID bei Klick und Enter', () => {
    const selected: string[] = [];
    component.schuldSelected.subscribe(id => selected.push(id));

    rows(fixture)[0].click();
    rows(fixture)[1].dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));

    expect(selected).toEqual(['s1', 's2']);
  });
});
