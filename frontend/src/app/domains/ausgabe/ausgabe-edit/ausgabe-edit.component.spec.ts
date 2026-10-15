import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatDialog} from '@angular/material/dialog';
import {of, throwError} from 'rxjs';

import {AusgabeEditComponent} from './ausgabe-edit.component';
import {
  AusgabeControllerApi,
  AusgabeDto,
  AusgabeKategorieDto,
  PersonControllerApi,
  PersonDto
} from '../../../generated';
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

describe('AusgabeEditComponent', () => {
  let fixture: ComponentFixture<AusgabeEditComponent>;
  let api: jasmine.SpyObj<AusgabeControllerApi>;
  let personApi: jasmine.SpyObj<PersonControllerApi>;
  let notification: jasmine.SpyObj<NotificationService>;
  let dialog: { open: jasmine.Spy };
  let router: Router;

  const ausgabe: AusgabeDto = {
    id: 'a1',
    datum: '2025-03-31',
    zahlender: 'p1',
    empfaenger: 'Migros',
    kategorie: AusgabeKategorieDto.Haushalt,
    text: 'Einkauf',
    betrag: 42.5
  };
  const persons: PersonDto[] = [{ id: 'p1', name: 'Gerber', vorname: 'Max', geburtsdatum: '1980-05-17' }];

  beforeEach(async () => {
    api = jasmine.createSpyObj<AusgabeControllerApi>('AusgabeControllerApi', [
      'getAusgabeById', 'createAusgabe', 'updateAusgabe', 'deleteAusgabeById'
    ]);
    personApi = jasmine.createSpyObj<PersonControllerApi>('PersonControllerApi', ['getAllPerson']);
    personApi.getAllPerson.and.returnValue(of(persons) as never);
    notification = jasmine.createSpyObj<NotificationService>('NotificationService', ['success', 'error']);
    dialog = { open: jasmine.createSpy('open').and.returnValue({ afterClosed: () => of(true) }) };

    await TestBed.configureTestingModule({
      imports: [AusgabeEditComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        provideNativeDateAdapter(),
        { provide: AusgabeControllerApi, useValue: api },
        { provide: PersonControllerApi, useValue: personApi },
        { provide: NotificationService, useValue: notification },
        { provide: MatDialog, useValue: dialog }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.resolveTo(true);
  });

  async function render(ausgabeId: string | null = null): Promise<void> {
    fixture = TestBed.createComponent(AusgabeEditComponent);
    fixture.componentRef.setInput('ausgabeId', ausgabeId);
    await stabilize(fixture);
  }

  function form() {
    return fixture.componentInstance['form'];
  }

  describe('Create-Modus', () => {
    beforeEach(async () => await render());

    it('zeigt den Erfassen-Titel und keinen Löschen-Button', () => {
      expect(el(fixture).querySelector('mat-card-title')?.textContent).toContain('Neue Ausgabe erfassen');
      expect(buttonByText(fixture, 'Löschen')).toBeUndefined();
    });

    it('zeigt Pflichtfeld-Meldungen und speichert nicht', () => {
      submit(fixture);

      expect(errorTexts(fixture)).toEqual(jasmine.arrayContaining([
        'Datum ist erforderlich', 'Empfänger ist erforderlich', 'Kategorie ist erforderlich', 'Betrag ist erforderlich'
      ]));
      expect(api.createAusgabe).not.toHaveBeenCalled();
    });

    it('lehnt Beträge <= 0 ab', () => {
      setInputValue(fixture, 'betrag', '0');
      expect(errorTexts(fixture)).toContain('Betrag muss grösser als 0 sein');
    });

    it('ruft createAusgabe auf und navigiert zur Liste', () => {
      api.createAusgabe.and.returnValue(of(ausgabe) as never);
      form().patchValue({
        datum: new Date(2025, 2, 31),
        zahlender: 'p1',
        empfaenger: ' Migros ',
        kategorie: AusgabeKategorieDto.Haushalt,
        text: 'Einkauf',
        betrag: 42.5
      });

      submit(fixture);

      expect(api.createAusgabe).toHaveBeenCalledOnceWith({ ...ausgabe, id: undefined });
      expect(router.navigate).toHaveBeenCalledWith(['/ausgabe']);
    });

    it('bleibt bei einem Speicherfehler auf der Seite', () => {
      api.createAusgabe.and.returnValue(throwError(() => new Error('boom')));
      form().patchValue({
        datum: new Date(2025, 2, 31), empfaenger: 'Migros', kategorie: AusgabeKategorieDto.Haushalt, betrag: 1
      });

      submit(fixture);

      expect(notification.error).toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });
  });

  describe('Edit-Modus', () => {
    beforeEach(async () => {
      api.getAusgabeById.and.returnValue(of(ausgabe) as never);
      await render('a1');
    });

    it('lädt die Ausgabe und befüllt das Formular', () => {
      expect(api.getAusgabeById).toHaveBeenCalledWith('a1');
      expect(form().getRawValue()).toEqual(jasmine.objectContaining({
        empfaenger: 'Migros', zahlender: 'p1', kategorie: AusgabeKategorieDto.Haushalt, betrag: 42.5
      }));
    });

    it('ruft updateAusgabe mit der ID auf', () => {
      api.updateAusgabe.and.returnValue(of(ausgabe) as never);

      submit(fixture);

      expect(api.updateAusgabe).toHaveBeenCalledOnceWith('a1', ausgabe);
      expect(router.navigate).toHaveBeenCalledWith(['/ausgabe']);
    });

    it('löscht nach Bestätigung und navigiert erst nach Erfolg', () => {
      api.deleteAusgabeById.and.returnValue(of(undefined) as never);

      buttonByText(fixture, 'Löschen')!.click();

      expect(api.deleteAusgabeById).toHaveBeenCalledWith('a1');
      expect(router.navigate).toHaveBeenCalledWith(['/ausgabe']);
    });

    it('löscht nicht, wenn der Dialog abgebrochen wird', () => {
      dialog.open.and.returnValue({ afterClosed: () => of(false) });

      buttonByText(fixture, 'Löschen')!.click();

      expect(api.deleteAusgabeById).not.toHaveBeenCalled();
    });
  });

  it('zeigt einen Ladefehler und sperrt das Speichern', async () => {
    api.getAusgabeById.and.returnValue(throwError(() => new Error('boom')));
    await render('a1');

    expect(el(fixture).querySelector('.error')?.textContent).toContain('Die Ausgabe konnte nicht geladen werden.');
    expect(submitButton(fixture).disabled).toBeTrue();
  });

  it('blockiert das Speichern nicht, wenn nur die Personen fehlen', async () => {
    personApi.getAllPerson.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).textContent).toContain('Personen konnten nicht geladen werden.');
    expect(submitButton(fixture).disabled).toBeFalse();
  });
});
