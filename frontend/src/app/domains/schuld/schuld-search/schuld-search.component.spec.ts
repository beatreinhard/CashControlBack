import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {SchuldSearchComponent} from './schuld-search.component';
import {SchuldArtDto, SchuldControllerApi, SchuldDto} from '../../../generated';
import {el, rows, setInputValue, stabilize, submit} from '../../../testing/dom-helpers';

describe('SchuldSearchComponent', () => {
  let fixture: ComponentFixture<SchuldSearchComponent>;
  let api: jasmine.SpyObj<SchuldControllerApi>;

  const schulden: SchuldDto[] = [
    { id: 's1', jahr: 2025, art: SchuldArtDto.Hypothek, glaeubiger: 'UBS', betrag: 300 }
  ];

  beforeEach(async () => {
    api = jasmine.createSpyObj<SchuldControllerApi>('SchuldControllerApi', ['getSchulden']);

    await TestBed.configureTestingModule({
      imports: [SchuldSearchComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: SchuldControllerApi, useValue: api }]
    }).compileComponents();
  });

  async function render(): Promise<void> {
    fixture = TestBed.createComponent(SchuldSearchComponent);
    await stabilize(fixture);
  }

  it('lädt zuerst alle Jahre und danach das gefilterte Jahr', async () => {
    api.getSchulden.and.returnValue(of(schulden) as never);
    await render();
    expect(api.getSchulden).toHaveBeenCalledWith(undefined);
    expect(rows(fixture).length).toBe(1);

    setInputValue(fixture, 'jahr', '2025');
    submit(fixture);
    await stabilize(fixture);

    expect(api.getSchulden).toHaveBeenCalledWith(2025);
  });

  it('sendet keinen Request bei ungültigem Jahr', async () => {
    api.getSchulden.and.returnValue(of(schulden) as never);
    await render();
    api.getSchulden.calls.reset();

    setInputValue(fixture, 'jahr', '1999');
    submit(fixture);
    await stabilize(fixture);

    expect(api.getSchulden).not.toHaveBeenCalled();
  });

  it('navigiert beim Auswählen zur Bearbeitung', async () => {
    api.getSchulden.and.returnValue(of(schulden) as never);
    await render();
    const navigate = spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);

    rows(fixture)[0].click();

    expect(navigate).toHaveBeenCalledWith(['/schuld/edit', 's1']);
  });

  it('zeigt eine Fehlermeldung, wenn das Laden fehlschlägt', async () => {
    api.getSchulden.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).querySelector('.error')?.textContent).toContain('Konnte Schulden nicht laden.');
  });
});
