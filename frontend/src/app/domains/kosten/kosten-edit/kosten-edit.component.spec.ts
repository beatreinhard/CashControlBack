import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {MatDialog} from '@angular/material/dialog';
import {of, throwError} from 'rxjs';

import {KostenEditComponent} from './kosten-edit.component';
import {KostenArtDto, KostenControllerApi, KostenDto, PersonControllerApi} from '../../../generated';
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

describe('KostenEditComponent', () => {
  let fixture: ComponentFixture<KostenEditComponent>;
  let api: jasmine.SpyObj<KostenControllerApi>;
  let notification: jasmine.SpyObj<NotificationService>;
  let dialog: { open: jasmine.Spy };
  let router: Router;

  const kosten: KostenDto = {
    id: 'k1',
    jahr: 2025,
    art: KostenArtDto.Krankenkasse,
    zahlender: 'p1',
    empfaenger: 'CSS',
    bemerkung: 'Prämie',
    betrag: 300
  };

  beforeEach(async () => {
    api = jasmine.createSpyObj<KostenControllerApi>('KostenControllerApi', [
      'getKostenById', 'createKosten', 'updateKosten', 'deleteKostenById'
    ]);
    const personApi = jasmine.createSpyObj<PersonControllerApi>('PersonControllerApi', ['getAllPerson']);
    personApi.getAllPerson.and.returnValue(of([]) as never);
    notification = jasmine.createSpyObj<NotificationService>('NotificationService', ['success', 'error']);
    dialog = { open: jasmine.createSpy('open').and.returnValue({ afterClosed: () => of(true) }) };

    await TestBed.configureTestingModule({
      imports: [KostenEditComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: KostenControllerApi, useValue: api },
        { provide: PersonControllerApi, useValue: personApi },
        { provide: NotificationService, useValue: notification },
        { provide: MatDialog, useValue: dialog }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.resolveTo(true);
  });

  async function render(kostenId: string | null = null): Promise<void> {
    fixture = TestBed.createComponent(KostenEditComponent);
    fixture.componentRef.setInput('kostenId', kostenId);
    await stabilize(fixture);
  }

  function form() {
    return fixture.componentInstance['form'];
  }

  describe('Create-Modus', () => {
    beforeEach(async () => await render());

    it('schlägt das aktuelle Jahr vor und zeigt keinen Löschen-Button', () => {
      expect(form().controls.jahr.value).toBe(new Date().getFullYear());
      expect(buttonByText(fixture, 'Löschen')).toBeUndefined();
    });

    it('zeigt Pflichtfeld-Meldungen und speichert nicht', () => {
      submit(fixture);

      expect(errorTexts(fixture)).toEqual(jasmine.arrayContaining([
        'Art ist erforderlich', 'Empfänger ist erforderlich', 'Betrag ist erforderlich'
      ]));
      expect(api.createKosten).not.toHaveBeenCalled();
    });

    it('lehnt Jahre vor 2000 ab', () => {
      setInputValue(fixture, 'jahr', '1999');
      expect(errorTexts(fixture)).toContain('Jahr muss 2000 oder grösser sein');
    });

    it('ruft createKosten auf und navigiert zur Liste', () => {
      api.createKosten.and.returnValue(of(kosten) as never);
      form().patchValue({ jahr: 2025, art: KostenArtDto.Krankenkasse, empfaenger: 'CSS', betrag: 300 });

      submit(fixture);

      expect(api.createKosten).toHaveBeenCalledOnceWith({
        id: undefined, jahr: 2025, art: KostenArtDto.Krankenkasse, empfaenger: 'CSS',
        zahlender: '', bemerkung: undefined, betrag: 300
      });
      expect(router.navigate).toHaveBeenCalledWith(['/kosten']);
    });
  });

  describe('Edit-Modus', () => {
    it('ruft updateKosten mit der ID auf', async () => {
      api.getKostenById.and.returnValue(of(kosten) as never);
      api.updateKosten.and.returnValue(of(kosten) as never);
      await render('k1');

      submit(fixture);

      expect(api.updateKosten).toHaveBeenCalledOnceWith('k1', kosten);
      expect(router.navigate).toHaveBeenCalledWith(['/kosten']);
    });

    it('löscht nach Bestätigung und navigiert erst nach Erfolg', async () => {
      api.getKostenById.and.returnValue(of(kosten) as never);
      api.deleteKostenById.and.returnValue(of(undefined) as never);
      await render('k1');

      buttonByText(fixture, 'Löschen')!.click();

      expect(api.deleteKostenById).toHaveBeenCalledWith('k1');
      expect(router.navigate).toHaveBeenCalledWith(['/kosten']);
    });

    it('ist schreibgeschützt, wenn die Kosten aus einer Ausgabe stammen', async () => {
      api.getKostenById.and.returnValue(of({ ...kosten, ausgabeId: 'a1' }) as never);
      await render('k1');

      expect(el(fixture).textContent).toContain('können nur dort bearbeitet werden');
      expect(form().disabled).toBeTrue();
      expect(buttonByText(fixture, 'Löschen')).toBeUndefined();
      expect(el(fixture).querySelector('button[type="submit"]')).toBeNull();
    });

    it('zeigt einen Ladefehler und sperrt das Speichern', async () => {
      api.getKostenById.and.returnValue(throwError(() => new Error('boom')));
      await render('k1');

      expect(el(fixture).querySelector('.error')?.textContent).toContain('Die Kosten konnten nicht geladen werden.');
      expect(submitButton(fixture).disabled).toBeTrue();
    });
  });
});
