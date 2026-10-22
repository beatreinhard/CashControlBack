import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {ReportingComponent} from './reporting.component';
import {ReportingControllerApi} from '../../../generated';
import {NotificationService} from '../../../shared/notification.service';
import {buttonByText, errorTexts, setInputValue} from '../../../testing/dom-helpers';

describe('ReportingComponent', () => {
  let fixture: ComponentFixture<ReportingComponent>;
  let api: jasmine.SpyObj<ReportingControllerApi>;
  let notification: jasmine.SpyObj<NotificationService>;
  let pdfWindow: { close: jasmine.Spy; location: { href: string } };

  beforeEach(async () => {
    api = jasmine.createSpyObj<ReportingControllerApi>('ReportingControllerApi', ['generateReport']);
    notification = jasmine.createSpyObj<NotificationService>('NotificationService', ['success', 'error']);
    pdfWindow = { close: jasmine.createSpy('close'), location: { href: '' } };
    spyOn(window, 'open').and.returnValue(pdfWindow as unknown as Window);

    await TestBed.configureTestingModule({
      imports: [ReportingComponent],
      providers: [
        provideNoopAnimations(),
        { provide: ReportingControllerApi, useValue: api },
        { provide: NotificationService, useValue: notification }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ReportingComponent);
    fixture.detectChanges();
  });

  function generate(): void {
    buttonByText(fixture, 'PDF generieren')!.click();
    fixture.detectChanges();
  }

  it('öffnet das Fenster sofort und zeigt danach das PDF an', () => {
    api.generateReport.and.returnValue(of(new Blob(['%PDF'], { type: 'application/pdf' })) as never);

    generate();

    expect(window.open).toHaveBeenCalledWith('', '_blank');
    expect(api.generateReport).toHaveBeenCalledOnceWith(new Date().getFullYear() - 1);
    expect(pdfWindow.location.href).toMatch(/^blob:/);
    expect(notification.success).toHaveBeenCalled();
    expect(buttonByText(fixture, 'PDF generieren')!.disabled).toBeFalse();
  });

  it('verwendet das erfasste Steuerjahr', () => {
    api.generateReport.and.returnValue(of(new Blob(['%PDF'], { type: 'application/pdf' })) as never);

    setInputValue(fixture, 'jahr', '2023');
    generate();

    expect(api.generateReport).toHaveBeenCalledOnceWith(2023);
  });

  it('generiert nichts und öffnet kein Fenster bei ungültigem Steuerjahr', () => {
    setInputValue(fixture, 'jahr', String(new Date().getFullYear() + 1));
    generate();

    expect(errorTexts(fixture)).toContain(`Steuerjahr muss zwischen 2000 und ${new Date().getFullYear()} liegen`);
    expect(api.generateReport).not.toHaveBeenCalled();
    expect(window.open).not.toHaveBeenCalled();
  });

  it('verlangt ein Steuerjahr', () => {
    setInputValue(fixture, 'jahr', '');
    generate();

    expect(errorTexts(fixture)).toContain('Steuerjahr ist erforderlich');
    expect(api.generateReport).not.toHaveBeenCalled();
  });

  it('schliesst das Fenster, wenn die Antwort kein PDF ist', () => {
    api.generateReport.and.returnValue(of(new Blob(['x'], { type: 'text/html' })) as never);

    generate();

    expect(pdfWindow.close).toHaveBeenCalled();
    expect(notification.error).toHaveBeenCalledWith('Die Antwort war kein PDF.');
  });

  it('schliesst das Fenster bei einem Serverfehler', () => {
    api.generateReport.and.returnValue(throwError(() => new Error('boom')));

    generate();

    expect(pdfWindow.close).toHaveBeenCalled();
    expect(notification.error).toHaveBeenCalledWith('Fehler beim Generieren des PDF.');
  });
});
