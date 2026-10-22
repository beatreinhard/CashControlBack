import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {MatDialog} from '@angular/material/dialog';
import {of, throwError} from 'rxjs';

import {VermoegenswertEditComponent} from './vermoegenswert-edit.component';
import {VermoegenswertControllerApi, VermoegenswertDto} from '../../../generated';
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

describe('VermoegenswertEditComponent', () => {
  let fixture: ComponentFixture<VermoegenswertEditComponent>;
  let api: jasmine.SpyObj<VermoegenswertControllerApi>;
  let notification: jasmine.SpyObj<NotificationService>;
  let dialog: { open: jasmine.Spy };
  let router: Router;

  const vermoegenswert: VermoegenswertDto = {
    id: 'v1',
    jahr: 2025,
    bezeichnung: 'Auto VW Golf',
    anschaffungsjahr: 2022,
    anschaffungspreis: 32000
  };

  beforeEach(async () => {
    api = jasmine.createSpyObj<VermoegenswertControllerApi>('VermoegenswertControllerApi', [
      'getVermoegenswertById', 'createVermoegenswert', 'updateVermoegenswert', 'deleteVermoegenswertById'
    ]);
    notification = jasmine.createSpyObj<NotificationService>('NotificationService', ['success', 'error']);
    dialog = { open: jasmine.createSpy('open').and.returnValue({ afterClosed: () => of(true) }) };

    await TestBed.configureTestingModule({
      imports: [VermoegenswertEditComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: VermoegenswertControllerApi, useValue: api },
        { provide: NotificationService, useValue: notification },
        { provide: MatDialog, useValue: dialog }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.resolveTo(true);
  });

  async function render(vermoegenswertId: string | null = null): Promise<void> {
    fixture = TestBed.createComponent(VermoegenswertEditComponent);
    fixture.componentRef.setInput('vermoegenswertId', vermoegenswertId);
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
        'Bezeichnung ist erforderlich', 'Anschaffungsjahr ist erforderlich', 'Anschaffungspreis ist erforderlich'
      ]));
      expect(api.createVermoegenswert).not.toHaveBeenCalled();
      expect(buttonByText(fixture, 'Löschen')).toBeUndefined();
    });

    it('lehnt einen negativen Anschaffungspreis ab', () => {
      setInputValue(fixture, 'anschaffungspreis', '-1');
      expect(errorTexts(fixture)).toContain('Anschaffungspreis darf nicht negativ sein');
    });

    it('lehnt ein Anschaffungsjahr nach dem Steuerjahr ab, auch wenn das Steuerjahr nachträglich sinkt', () => {
      setInputValue(fixture, 'jahr', '2025');
      setInputValue(fixture, 'anschaffungsjahr', '2024');
      expect(form().controls.anschaffungsjahr.valid).toBeTrue();

      setInputValue(fixture, 'jahr', '2023');

      expect(errorTexts(fixture)).toContain('Anschaffungsjahr darf nicht nach dem Steuerjahr liegen');
    });

    it('ruft createVermoegenswert auf und navigiert', () => {
      api.createVermoegenswert.and.returnValue(of('v2') as never);
      form().patchValue({ jahr: 2025, bezeichnung: ' E-Bike ', anschaffungsjahr: 2023, anschaffungspreis: 4500 });

      submit(fixture);

      expect(api.createVermoegenswert).toHaveBeenCalledOnceWith({
        id: undefined, jahr: 2025, bezeichnung: 'E-Bike', anschaffungsjahr: 2023, anschaffungspreis: 4500
      });
      expect(router.navigate).toHaveBeenCalledWith(['/vermoegenswert']);
    });
  });

  describe('Edit-Modus', () => {
    it('ruft updateVermoegenswert mit der ID auf', async () => {
      api.getVermoegenswertById.and.returnValue(of(vermoegenswert) as never);
      api.updateVermoegenswert.and.returnValue(of({}) as never);
      await render('v1');

      submit(fixture);

      expect(api.updateVermoegenswert).toHaveBeenCalledOnceWith('v1', vermoegenswert);
      expect(router.navigate).toHaveBeenCalledWith(['/vermoegenswert']);
    });

    it('löscht nach Bestätigung und navigiert', async () => {
      api.getVermoegenswertById.and.returnValue(of(vermoegenswert) as never);
      api.deleteVermoegenswertById.and.returnValue(of({}) as never);
      await render('v1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(api.deleteVermoegenswertById).toHaveBeenCalledOnceWith('v1');
      expect(router.navigate).toHaveBeenCalledWith(['/vermoegenswert']);
    });

    it('navigiert nicht, wenn das Löschen fehlschlägt', async () => {
      api.getVermoegenswertById.and.returnValue(of(vermoegenswert) as never);
      api.deleteVermoegenswertById.and.returnValue(throwError(() => new Error('boom')));
      await render('v1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(notification.error).toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('zeigt einen Ladefehler und sperrt das Speichern', async () => {
      api.getVermoegenswertById.and.returnValue(throwError(() => new Error('boom')));
      await render('v1');

      expect(el(fixture).querySelector('.error')?.textContent).toContain('Der Vermögenswert konnte nicht geladen werden.');
      expect(submitButton(fixture).disabled).toBeTrue();
    });
  });
});
