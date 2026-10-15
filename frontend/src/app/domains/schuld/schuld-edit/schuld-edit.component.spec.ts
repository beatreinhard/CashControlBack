import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {MatDialog} from '@angular/material/dialog';
import {of, throwError} from 'rxjs';

import {SchuldEditComponent} from './schuld-edit.component';
import {SchuldArtDto, SchuldControllerApi, SchuldDto} from '../../../generated';
import {NotificationService} from '../../../shared/notification.service';
import {
  buttonByText,
  el,
  errorTexts,
  setInputValue,
  stabilize,
  submit,
  submitButton
} from '../../../testing/dom-helpers';

describe('SchuldEditComponent', () => {
  let fixture: ComponentFixture<SchuldEditComponent>;
  let api: jasmine.SpyObj<SchuldControllerApi>;
  let notification: jasmine.SpyObj<NotificationService>;
  let dialog: { open: jasmine.Spy };
  let router: Router;

  const schuld: SchuldDto = {
    id: 's1',
    jahr: 2025,
    art: SchuldArtDto.Hypothek,
    glaeubiger: 'UBS',
    text: 'Festhypothek',
    betrag: 500000,
    zinsen: 7500
  };

  beforeEach(async () => {
    api = jasmine.createSpyObj<SchuldControllerApi>('SchuldControllerApi', [
      'getSchuldById', 'createSchuld', 'updateSchuld', 'deleteSchuldById'
    ]);
    notification = jasmine.createSpyObj<NotificationService>('NotificationService', ['success', 'error']);
    dialog = { open: jasmine.createSpy('open').and.returnValue({ afterClosed: () => of(true) }) };

    await TestBed.configureTestingModule({
      imports: [SchuldEditComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: SchuldControllerApi, useValue: api },
        { provide: NotificationService, useValue: notification },
        { provide: MatDialog, useValue: dialog }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.resolveTo(true);
  });

  async function render(schuldId: string | null = null): Promise<void> {
    fixture = TestBed.createComponent(SchuldEditComponent);
    fixture.componentRef.setInput('schuldId', schuldId);
    await stabilize(fixture);
  }

  function form() {
    return fixture.componentInstance['form'];
  }

  describe('Create-Modus', () => {
    beforeEach(async () => await render());

    it('zeigt Pflichtfeld-Meldungen und speichert nicht', () => {
      submit(fixture);

      expect(errorTexts(fixture)).toEqual(jasmine.arrayContaining([
        'Art ist erforderlich', 'Gläubiger ist erforderlich', 'Betrag ist erforderlich'
      ]));
      expect(api.createSchuld).not.toHaveBeenCalled();
      expect(buttonByText(fixture, 'Löschen')).toBeUndefined();
    });

    it('lehnt negative Zinsen ab', () => {
      setInputValue(fixture, 'zinsen', '-1');
      expect(errorTexts(fixture)).toContain('Zinsen dürfen nicht negativ sein');
    });

    it('ruft createSchuld auf, lässt leere optionale Felder weg und navigiert', () => {
      api.createSchuld.and.returnValue(of(schuld) as never);
      form().patchValue({ jahr: 2025, art: SchuldArtDto.Rechnung, glaeubiger: ' Swisscom ', betrag: 99 });

      submit(fixture);

      expect(api.createSchuld).toHaveBeenCalledOnceWith({
        id: undefined, jahr: 2025, art: SchuldArtDto.Rechnung, glaeubiger: 'Swisscom',
        text: undefined, betrag: 99, zinsen: undefined
      });
      expect(router.navigate).toHaveBeenCalledWith(['/schuld']);
    });
  });

  describe('Edit-Modus', () => {
    it('ruft updateSchuld mit der ID auf', async () => {
      api.getSchuldById.and.returnValue(of(schuld) as never);
      api.updateSchuld.and.returnValue(of(schuld) as never);
      await render('s1');

      submit(fixture);

      expect(api.updateSchuld).toHaveBeenCalledOnceWith('s1', schuld);
      expect(router.navigate).toHaveBeenCalledWith(['/schuld']);
    });

    it('löscht nicht, wenn das Löschen fehlschlägt', async () => {
      api.getSchuldById.and.returnValue(of(schuld) as never);
      api.deleteSchuldById.and.returnValue(throwError(() => new Error('boom')));
      await render('s1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(notification.error).toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('zeigt einen Ladefehler und sperrt das Speichern', async () => {
      api.getSchuldById.and.returnValue(throwError(() => new Error('boom')));
      await render('s1');

      expect(el(fixture).querySelector('.error')?.textContent).toContain('Die Schuld konnte nicht geladen werden.');
      expect(submitButton(fixture).disabled).toBeTrue();
    });
  });
});
