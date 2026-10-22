import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {MatDialog} from '@angular/material/dialog';
import {of, throwError} from 'rxjs';

import {BerufEditComponent} from './beruf-edit.component';
import {BerufControllerApi, BerufDto, PersonControllerApi, PersonDto} from '../../../generated';
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

describe('BerufEditComponent', () => {
  let fixture: ComponentFixture<BerufEditComponent>;
  let api: jasmine.SpyObj<BerufControllerApi>;
  let personApi: jasmine.SpyObj<PersonControllerApi>;
  let notification: jasmine.SpyObj<NotificationService>;
  let dialog: { open: jasmine.Spy };
  let router: Router;

  const persons: PersonDto[] = [
    { id: 'p1', name: 'Reinhard', vorname: 'Beat', geburtsdatum: '1970-01-01' }
  ];

  const beruf: BerufDto = {
    id: 'b1',
    jahr: 2025,
    arbeitnehmer: 'p1',
    arbeitgeber: 'Muster AG',
    arbeitsort: 'Bern',
    beschaeftigungsgrad: 100,
    arbeitstage: 220,
    fahrtkilometerProTag: 20,
    grundAutobenutzung: 'Kein ÖV-Anschluss',
    ansatzAuswaertigeVerpflegung: 15,
    bemerkung: 'Homeoffice an 2 Tagen pro Woche'
  };

  beforeEach(async () => {
    api = jasmine.createSpyObj<BerufControllerApi>('BerufControllerApi', [
      'getBerufById', 'createBeruf', 'updateBeruf', 'deleteBerufById'
    ]);
    personApi = jasmine.createSpyObj<PersonControllerApi>('PersonControllerApi', ['getAllPerson']);
    personApi.getAllPerson.and.returnValue(of(persons) as never);
    notification = jasmine.createSpyObj<NotificationService>('NotificationService', ['success', 'error']);
    dialog = { open: jasmine.createSpy('open').and.returnValue({ afterClosed: () => of(true) }) };

    await TestBed.configureTestingModule({
      imports: [BerufEditComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: BerufControllerApi, useValue: api },
        { provide: PersonControllerApi, useValue: personApi },
        { provide: NotificationService, useValue: notification },
        { provide: MatDialog, useValue: dialog }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.resolveTo(true);
  });

  async function render(berufId: string | null = null): Promise<void> {
    fixture = TestBed.createComponent(BerufEditComponent);
    fixture.componentRef.setInput('berufId', berufId);
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
        'Arbeitnehmer ist erforderlich', 'Arbeitgeber ist erforderlich', 'Arbeitsort ist erforderlich',
        'Arbeitstage sind erforderlich', 'Ansatz ist erforderlich'
      ]));
      expect(api.createBeruf).not.toHaveBeenCalled();
      expect(buttonByText(fixture, 'Löschen')).toBeUndefined();
    });

    it('lehnt einen Beschäftigungsgrad über 100 % ab', () => {
      setInputValue(fixture, 'beschaeftigungsgrad', '120');
      expect(errorTexts(fixture)).toContain('Beschäftigungsgrad muss zwischen 1 und 100 liegen');
    });

    it('lehnt negative Fahrtkilometer ab', () => {
      setInputValue(fixture, 'fahrtkilometerProTag', '-1');
      expect(errorTexts(fixture)).toContain('Fahrtkilometer dürfen nicht negativ sein');
    });

    it('ruft createBeruf auf, lässt leere optionale Felder weg und navigiert', () => {
      api.createBeruf.and.returnValue(of('b1') as never);
      form().patchValue({
        jahr: 2025, arbeitnehmer: 'p1', arbeitgeber: 'Muster AG', arbeitsort: 'Bern',
        beschaeftigungsgrad: 80, arbeitstage: 180, ansatzAuswaertigeVerpflegung: 15
      });

      submit(fixture);

      expect(api.createBeruf).toHaveBeenCalledOnceWith({
        id: undefined, jahr: 2025, arbeitnehmer: 'p1', arbeitgeber: 'Muster AG', arbeitsort: 'Bern',
        beschaeftigungsgrad: 80, arbeitstage: 180, fahrtkilometerProTag: undefined, grundAutobenutzung: undefined,
        ansatzAuswaertigeVerpflegung: 15, bemerkung: undefined
      });
      expect(router.navigate).toHaveBeenCalledWith(['/beruf']);
    });
  });

  it('zeigt eine Meldung, wenn die Personen nicht geladen werden können', async () => {
    personApi.getAllPerson.and.returnValue(throwError(() => new Error('boom')));
    await render();

    expect(el(fixture).querySelector('mat-hint.error')?.textContent).toContain('Personen konnten nicht geladen werden.');
  });

  describe('Edit-Modus', () => {
    it('ruft updateBeruf mit der ID auf', async () => {
      api.getBerufById.and.returnValue(of(beruf) as never);
      api.updateBeruf.and.returnValue(of({}) as never);
      await render('b1');

      submit(fixture);

      expect(api.updateBeruf).toHaveBeenCalledOnceWith('b1', beruf);
      expect(router.navigate).toHaveBeenCalledWith(['/beruf']);
    });

    it('löscht nach Bestätigung und navigiert', async () => {
      api.getBerufById.and.returnValue(of(beruf) as never);
      api.deleteBerufById.and.returnValue(of({}) as never);
      await render('b1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(api.deleteBerufById).toHaveBeenCalledOnceWith('b1');
      expect(router.navigate).toHaveBeenCalledWith(['/beruf']);
    });

    it('navigiert nicht, wenn das Löschen fehlschlägt', async () => {
      api.getBerufById.and.returnValue(of(beruf) as never);
      api.deleteBerufById.and.returnValue(throwError(() => new Error('boom')));
      await render('b1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(notification.error).toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('zeigt einen Ladefehler und sperrt das Speichern', async () => {
      api.getBerufById.and.returnValue(throwError(() => new Error('boom')));
      await render('b1');

      expect(el(fixture).querySelector('.error')?.textContent).toContain('Der Beruf konnte nicht geladen werden.');
      expect(submitButton(fixture).disabled).toBeTrue();
    });
  });
});
