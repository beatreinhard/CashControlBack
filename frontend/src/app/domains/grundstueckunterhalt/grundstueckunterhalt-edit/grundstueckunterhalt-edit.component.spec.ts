import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatDialog} from '@angular/material/dialog';
import {of, throwError} from 'rxjs';

import {GrundstueckunterhaltEditComponent} from './grundstueckunterhalt-edit.component';
import {GrundstueckunterhaltControllerApi, GrundstueckunterhaltDto} from '../../../generated';
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

describe('GrundstueckunterhaltEditComponent', () => {
  let fixture: ComponentFixture<GrundstueckunterhaltEditComponent>;
  let api: jasmine.SpyObj<GrundstueckunterhaltControllerApi>;
  let notification: jasmine.SpyObj<NotificationService>;
  let dialog: { open: jasmine.Spy };
  let router: Router;

  const eintrag: GrundstueckunterhaltDto = {
    id: 'g1',
    jahr: 2025,
    rgDatum: '2025-04-15',
    ausfuehrendeFirma: 'Maler Muster AG',
    arbeitsArt: 'Fassade streichen',
    betragNetto: 12000,
    anteilAndereKosten: 2000,
    anteilUnterhalt: 10000
  };

  beforeEach(async () => {
    api = jasmine.createSpyObj<GrundstueckunterhaltControllerApi>('GrundstueckunterhaltControllerApi', [
      'getGrundstueckunterhaltById', 'createGrundstueckunterhalt', 'updateGrundstueckunterhalt',
      'deleteGrundstueckunterhaltById'
    ]);
    notification = jasmine.createSpyObj<NotificationService>('NotificationService', ['success', 'error']);
    dialog = { open: jasmine.createSpy('open').and.returnValue({ afterClosed: () => of(true) }) };

    await TestBed.configureTestingModule({
      imports: [GrundstueckunterhaltEditComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        provideNativeDateAdapter(),
        { provide: GrundstueckunterhaltControllerApi, useValue: api },
        { provide: NotificationService, useValue: notification },
        { provide: MatDialog, useValue: dialog }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.resolveTo(true);
  });

  async function render(grundstueckunterhaltId: string | null = null): Promise<void> {
    fixture = TestBed.createComponent(GrundstueckunterhaltEditComponent);
    fixture.componentRef.setInput('grundstueckunterhaltId', grundstueckunterhaltId);
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
        'Rechnungsdatum ist erforderlich', 'Ausführende Firma ist erforderlich', 'Arbeitsart ist erforderlich',
        'Betrag netto ist erforderlich'
      ]));
      expect(api.createGrundstueckunterhalt).not.toHaveBeenCalled();
      expect(buttonByText(fixture, 'Löschen')).toBeUndefined();
    });

    it('berechnet den Anteil Unterhalt laufend', () => {
      setInputValue(fixture, 'betragNetto', '1000.50');
      setInputValue(fixture, 'anteilAndereKosten', '200.20');

      expect(el(fixture).querySelector('.anteil-unterhalt')?.textContent).toContain('800.30');
    });

    it('lehnt andere Kosten über dem Nettobetrag ab, auch wenn der Nettobetrag nachträglich sinkt', () => {
      setInputValue(fixture, 'betragNetto', '1000');
      setInputValue(fixture, 'anteilAndereKosten', '500');
      expect(form().controls.anteilAndereKosten.valid).toBeTrue();

      setInputValue(fixture, 'betragNetto', '400');

      expect(errorTexts(fixture)).toContain('Anteil andere Kosten darf nicht grösser als der Betrag netto sein');
    });

    it('ruft createGrundstueckunterhalt mit berechnetem Anteil Unterhalt auf und navigiert', () => {
      api.createGrundstueckunterhalt.and.returnValue(of('g1') as never);
      form().patchValue({
        jahr: 2025, rgDatum: new Date(2025, 3, 15), ausfuehrendeFirma: ' Maler Muster AG ',
        arbeitsArt: 'Fassade streichen', betragNetto: 12000, anteilAndereKosten: 2000
      });

      submit(fixture);

      expect(api.createGrundstueckunterhalt).toHaveBeenCalledOnceWith({
        id: undefined, jahr: 2025, rgDatum: '2025-04-15', ausfuehrendeFirma: 'Maler Muster AG',
        arbeitsArt: 'Fassade streichen', betragNetto: 12000, anteilAndereKosten: 2000, anteilUnterhalt: 10000
      });
      expect(router.navigate).toHaveBeenCalledWith(['/grundstueckunterhalt']);
    });
  });

  describe('Edit-Modus', () => {
    it('ruft updateGrundstueckunterhalt mit der ID auf', async () => {
      api.getGrundstueckunterhaltById.and.returnValue(of(eintrag) as never);
      api.updateGrundstueckunterhalt.and.returnValue(of({}) as never);
      await render('g1');

      submit(fixture);

      expect(api.updateGrundstueckunterhalt).toHaveBeenCalledOnceWith('g1', eintrag);
      expect(router.navigate).toHaveBeenCalledWith(['/grundstueckunterhalt']);
    });

    it('löscht nach Bestätigung und navigiert', async () => {
      api.getGrundstueckunterhaltById.and.returnValue(of(eintrag) as never);
      api.deleteGrundstueckunterhaltById.and.returnValue(of({}) as never);
      await render('g1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(api.deleteGrundstueckunterhaltById).toHaveBeenCalledOnceWith('g1');
      expect(router.navigate).toHaveBeenCalledWith(['/grundstueckunterhalt']);
    });

    it('navigiert nicht, wenn das Löschen fehlschlägt', async () => {
      api.getGrundstueckunterhaltById.and.returnValue(of(eintrag) as never);
      api.deleteGrundstueckunterhaltById.and.returnValue(throwError(() => new Error('boom')));
      await render('g1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(notification.error).toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('zeigt einen Ladefehler und sperrt das Speichern', async () => {
      api.getGrundstueckunterhaltById.and.returnValue(throwError(() => new Error('boom')));
      await render('g1');

      expect(el(fixture).querySelector('.error')?.textContent)
        .toContain('Der Grundstückunterhalt konnte nicht geladen werden.');
      expect(submitButton(fixture).disabled).toBeTrue();
    });
  });
});
