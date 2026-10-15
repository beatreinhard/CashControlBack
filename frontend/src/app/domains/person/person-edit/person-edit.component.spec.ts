import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatDialog} from '@angular/material/dialog';
import {of, throwError} from 'rxjs';

import {PersonEditComponent} from './person-edit.component';
import {PersonControllerApi, PersonDto} from '../../../generated';
import {NotificationService} from '../../../shared/notification.service';

describe('PersonEditComponent', () => {
  let fixture: ComponentFixture<PersonEditComponent>;
  let api: jasmine.SpyObj<PersonControllerApi>;
  let notification: jasmine.SpyObj<NotificationService>;
  let dialog: { open: jasmine.Spy };
  let router: Router;

  const person: PersonDto = {
    id: 'p1',
    name: 'Gerber',
    vorname: 'Max',
    geburtsdatum: '1980-05-17',
    ahvnummer: '756.1234.5678.90'
  };

  beforeEach(async () => {
    api = jasmine.createSpyObj<PersonControllerApi>('PersonControllerApi', [
      'getPersonById',
      'createPerson',
      'updatePerson',
      'deletePersonById'
    ]);
    notification = jasmine.createSpyObj<NotificationService>('NotificationService', ['success', 'error']);
    dialog = { open: jasmine.createSpy('open').and.returnValue({ afterClosed: () => of(true) }) };

    await TestBed.configureTestingModule({
      imports: [PersonEditComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        provideNativeDateAdapter(),
        { provide: PersonControllerApi, useValue: api },
        { provide: NotificationService, useValue: notification },
        { provide: MatDialog, useValue: dialog }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.resolveTo(true);
  });

  async function render(personId: string | null = null): Promise<void> {
    fixture = TestBed.createComponent(PersonEditComponent);
    fixture.componentRef.setInput('personId', personId);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
  }

  function element(): HTMLElement {
    return fixture.nativeElement as HTMLElement;
  }

  function setInput(name: string, value: string): void {
    const input = element().querySelector<HTMLInputElement>(`input[formcontrolname="${name}"]`)!;
    input.value = value;
    input.dispatchEvent(new Event('input'));
    input.dispatchEvent(new Event('blur'));
  }

  function submit(): void {
    element().querySelector<HTMLButtonElement>('button[type="submit"]')!.click();
    fixture.detectChanges();
  }

  function deleteButton(): HTMLButtonElement | undefined {
    return Array.from(element().querySelectorAll<HTMLButtonElement>('button'))
      .find(b => b.textContent?.trim() === 'Löschen');
  }

  function errorTexts(): string[] {
    return Array.from(element().querySelectorAll('mat-error')).map(e => e.textContent!.trim());
  }

  describe('Create-Modus', () => {
    beforeEach(async () => await render());

    it('zeigt den Titel für eine neue Person und keinen Löschen-Button', () => {
      expect(element().querySelector('mat-card-title')?.textContent).toContain('Neue Person erfassen');
      expect(deleteButton()).toBeUndefined();
      expect(api.getPersonById).not.toHaveBeenCalled();
    });

    it('zeigt Pflichtfeld-Meldungen und speichert nicht bei ungültigem Formular', () => {
      submit();

      expect(errorTexts()).toEqual(jasmine.arrayContaining([
        'Name ist erforderlich',
        'Vorname ist erforderlich',
        'Geburtsdatum ist erforderlich'
      ]));
      expect(api.createPerson).not.toHaveBeenCalled();
    });

    it('meldet eine AHV-Nummer im falschen Format', () => {
      setInput('ahvnummer', '7561234567890');
      fixture.detectChanges();

      expect(errorTexts()).toContain('Format: 756.1234.5678.90');
    });

    it('ruft createPerson auf und navigiert danach zur Liste', () => {
      api.createPerson.and.returnValue(of({ ...person }) as never);
      setInput('name', ' Gerber ');
      setInput('vorname', 'Max');
      fixture.componentInstance['form'].controls.geburtsdatum.setValue(new Date(1980, 4, 17));

      submit();

      expect(api.createPerson).toHaveBeenCalledOnceWith({
        id: undefined,
        name: 'Gerber',
        vorname: 'Max',
        geburtsdatum: '1980-05-17',
        ahvnummer: undefined
      });
      expect(notification.success).toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['/person']);
    });

    it('bleibt bei einem Speicherfehler auf der Seite', () => {
      api.createPerson.and.returnValue(throwError(() => new Error('boom')));
      setInput('name', 'Gerber');
      setInput('vorname', 'Max');
      fixture.componentInstance['form'].controls.geburtsdatum.setValue(new Date(1980, 4, 17));

      submit();

      expect(notification.error).toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });
  });

  describe('Edit-Modus', () => {
    beforeEach(async () => {
      api.getPersonById.and.returnValue(of(person) as never);
      await render('p1');
    });

    it('lädt die Person und befüllt das Formular', () => {
      expect(api.getPersonById).toHaveBeenCalledWith('p1');
      expect(element().querySelector('mat-card-title')?.textContent).toContain('Person bearbeiten');
      expect(fixture.componentInstance['form'].getRawValue()).toEqual(jasmine.objectContaining({
        name: 'Gerber',
        vorname: 'Max',
        ahvnummer: '756.1234.5678.90'
      }));
    });

    it('ruft updatePerson mit der ID auf', () => {
      api.updatePerson.and.returnValue(of(person) as never);

      submit();

      expect(api.updatePerson).toHaveBeenCalledOnceWith('p1', person);
      expect(api.createPerson).not.toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['/person']);
    });

    it('löscht nach Bestätigung und navigiert erst nach Erfolg', () => {
      api.deletePersonById.and.returnValue(of(undefined) as never);

      deleteButton()!.click();

      expect(dialog.open).toHaveBeenCalled();
      expect(api.deletePersonById).toHaveBeenCalledWith('p1');
      expect(router.navigate).toHaveBeenCalledWith(['/person']);
    });

    it('löscht nicht, wenn der Dialog abgebrochen wird', () => {
      dialog.open.and.returnValue({ afterClosed: () => of(false) });

      deleteButton()!.click();

      expect(api.deletePersonById).not.toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('navigiert nicht, wenn das Löschen fehlschlägt', () => {
      api.deletePersonById.and.returnValue(throwError(() => new Error('boom')));

      deleteButton()!.click();

      expect(notification.error).toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });
  });

  describe('Ladefehler', () => {
    it('zeigt eine Fehlermeldung und sperrt das Speichern', async () => {
      api.getPersonById.and.returnValue(throwError(() => new Error('boom')));
      await render('p1');

      expect(element().querySelector('.error')?.textContent).toContain('Die Person konnte nicht geladen werden.');
      expect(element().querySelector<HTMLButtonElement>('button[type="submit"]')!.disabled).toBeTrue();
    });
  });
});
