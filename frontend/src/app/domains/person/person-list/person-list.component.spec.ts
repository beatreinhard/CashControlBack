import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {PersonListComponent} from './person-list.component';
import {PersonDto} from '../../../generated';

describe('PersonListComponent', () => {
  let component: PersonListComponent;
  let fixture: ComponentFixture<PersonListComponent>;

  const persons: PersonDto[] = [
    { id: 'p1', name: 'Gerber', vorname: 'Max', geburtsdatum: '1980-05-17', ahvnummer: '756.1234.5678.90' },
    { id: 'p2', name: 'Arnold', vorname: 'Anna', geburtsdatum: '1990-01-02' }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PersonListComponent],
      providers: [provideNoopAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PersonListComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('items', persons);
    fixture.detectChanges();
  });

  function rows(): HTMLElement[] {
    return Array.from((fixture.nativeElement as HTMLElement).querySelectorAll<HTMLElement>('tr.clickable-row'));
  }

  it('zeigt eine Zeile pro Person', () => {
    expect(rows().length).toBe(2);
    expect(rows()[0].textContent).toContain('Gerber');
    expect(rows()[0].textContent).toContain('17.05.1980');
  });

  it('meldet die ID beim Klick auf eine Zeile', () => {
    const selected: string[] = [];
    component.personSelected.subscribe(id => selected.push(id));

    rows()[1].click();

    expect(selected).toEqual(['p2']);
  });

  it('meldet die ID bei Enter auf einer Zeile', () => {
    const selected: string[] = [];
    component.personSelected.subscribe(id => selected.push(id));

    rows()[0].dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));

    expect(selected).toEqual(['p1']);
  });

  it('aktualisiert die Tabelle, wenn sich die Daten ändern', () => {
    fixture.componentRef.setInput('items', [persons[0]]);
    fixture.detectChanges();

    expect(rows().length).toBe(1);
  });
});
