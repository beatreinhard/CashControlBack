import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';

import {ReportingComponent} from './reporting.component';
import {ReportingControllerApi} from '../../../generated';
import {NotificationService} from '../../../shared/notification.service';
import {buttonByText} from '../../../testing/dom-helpers';

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
    expect(pdfWindow.location.href).toMatch(/^blob:/);
    expect(notification.success).toHaveBeenCalled();
    expect(buttonByText(fixture, 'PDF generieren')!.disabled).toBeFalse();
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
