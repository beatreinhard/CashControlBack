import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatDialog} from '@angular/material/dialog';
import {of, throwError} from 'rxjs';

import {ErbschaftEditComponent} from './erbschaft-edit.component';
import {ErbschaftArtDto, ErbschaftControllerApi, ErbschaftDto} from '../../../generated';
import {NotificationService} from '../../../shared/notification.service';
import {buttonByText, el, errorTexts, stabilize, submit, submitButton} from '../../../testing/dom-helpers';

describe('ErbschaftEditComponent', () => {
  let fixture: ComponentFixture<ErbschaftEditComponent>;
  let api: jasmine.SpyObj<ErbschaftControllerApi>;
  let notification: jasmine.SpyObj<NotificationService>;
  let dialog: { open: jasmine.Spy };
  let router: Router;

  const erbschaft: ErbschaftDto = {
    id: 'e1',
    jahr: 2025,
    art: ErbschaftArtDto.Erbschaft,
    betrag: 50000,
    datum: '2025-03-31',
    geber: 'Hans Muster',
    verwandtschaftsverhaeltnis: 'Vater',
    gegenstand: 'Bargeld',
    bemerkung: 'Erbteilung abgeschlossen'
  };

  beforeEach(async () => {
    api = jasmine.createSpyObj<ErbschaftControllerApi>('ErbschaftControllerApi', [
      'getErbschaftById', 'createErbschaft', 'updateErbschaft', 'deleteErbschaftById'
    ]);
    notification = jasmine.createSpyObj<NotificationService>('NotificationService', ['success', 'error']);
    dialog = { open: jasmine.createSpy('open').and.returnValue({ afterClosed: () => of(true) }) };

    await TestBed.configureTestingModule({
      imports: [ErbschaftEditComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        provideNativeDateAdapter(),
        { provide: ErbschaftControllerApi, useValue: api },
        { provide: NotificationService, useValue: notification },
        { provide: MatDialog, useValue: dialog }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.resolveTo(true);
  });

  async function render(erbschaftId: string | null = null): Promise<void> {
    fixture = TestBed.createComponent(ErbschaftEditComponent);
    fixture.componentRef.setInput('erbschaftId', erbschaftId);
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
        'Art ist erforderlich', 'Datum ist erforderlich', 'Betrag ist erforderlich', 'Geber ist erforderlich',
        'Verwandtschaftsverhältnis ist erforderlich', 'Gegenstand ist erforderlich'
      ]));
      expect(api.createErbschaft).not.toHaveBeenCalled();
      expect(buttonByText(fixture, 'Löschen')).toBeUndefined();
    });

    it('lehnt ein Datum in der Zukunft ab', () => {
      const tomorrow = new Date();
      tomorrow.setDate(tomorrow.getDate() + 1);
      form().controls.datum.setValue(tomorrow);
      form().controls.datum.markAsTouched();
      fixture.detectChanges();

      expect(errorTexts(fixture)).toContain('Datum darf nicht in der Zukunft liegen');
    });

    it('ruft createErbschaft auf, lässt leere optionale Felder weg und navigiert', () => {
      api.createErbschaft.and.returnValue(of('e1') as never);
      form().patchValue({
        jahr: 2025, art: ErbschaftArtDto.SchenkungVorempfang, datum: new Date(2025, 2, 31), betrag: 10000,
        geber: ' Anna Muster ', verwandtschaftsverhaeltnis: 'Tante', gegenstand: 'Wertschriften'
      });

      submit(fixture);

      expect(api.createErbschaft).toHaveBeenCalledOnceWith({
        id: undefined, jahr: 2025, art: ErbschaftArtDto.SchenkungVorempfang, datum: '2025-03-31', betrag: 10000,
        geber: 'Anna Muster', verwandtschaftsverhaeltnis: 'Tante', gegenstand: 'Wertschriften', bemerkung: undefined
      });
      expect(router.navigate).toHaveBeenCalledWith(['/erbschaft']);
    });
  });

  describe('Edit-Modus', () => {
    it('ruft updateErbschaft mit der ID auf', async () => {
      api.getErbschaftById.and.returnValue(of(erbschaft) as never);
      api.updateErbschaft.and.returnValue(of({}) as never);
      await render('e1');

      submit(fixture);

      expect(api.updateErbschaft).toHaveBeenCalledOnceWith('e1', erbschaft);
      expect(router.navigate).toHaveBeenCalledWith(['/erbschaft']);
    });

    it('löscht nach Bestätigung und navigiert', async () => {
      api.getErbschaftById.and.returnValue(of(erbschaft) as never);
      api.deleteErbschaftById.and.returnValue(of({}) as never);
      await render('e1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(api.deleteErbschaftById).toHaveBeenCalledOnceWith('e1');
      expect(router.navigate).toHaveBeenCalledWith(['/erbschaft']);
    });

    it('navigiert nicht, wenn das Löschen fehlschlägt', async () => {
      api.getErbschaftById.and.returnValue(of(erbschaft) as never);
      api.deleteErbschaftById.and.returnValue(throwError(() => new Error('boom')));
      await render('e1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(notification.error).toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('zeigt einen Ladefehler und sperrt das Speichern', async () => {
      api.getErbschaftById.and.returnValue(throwError(() => new Error('boom')));
      await render('e1');

      expect(el(fixture).querySelector('.error')?.textContent).toContain('Die Erbschaft konnte nicht geladen werden.');
      expect(submitButton(fixture).disabled).toBeTrue();
    });
  });
});
