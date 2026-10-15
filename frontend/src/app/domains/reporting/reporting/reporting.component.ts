import {ChangeDetectionStrategy, Component, DestroyRef, inject, signal} from '@angular/core';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatProgressBar} from '@angular/material/progress-bar';
import {finalize} from 'rxjs';
import {ReportingControllerApi} from '../../../generated';
import {NotificationService} from '../../../shared/notification.service';

const REVOKE_URL_DELAY_MS = 10_000;

@Component({
  selector: 'app-reporting',
  imports: [
    MatButton,
    MatCard,
    MatCardActions,
    MatCardContent,
    MatCardTitle,
    MatProgressBar
  ],
  templateUrl: './reporting.component.html',
  styleUrl: './reporting.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReportingComponent {

  private readonly reportingController = inject(ReportingControllerApi);
  private readonly notification = inject(NotificationService);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly generatePdfInProgress = signal(false);

  protected pdfGenerieren(): void {
    if (this.generatePdfInProgress()) return;

    // Fenster synchron im Klick-Handler öffnen, sonst blockiert der Popup-Blocker
    const pdfWindow = window.open('', '_blank');
    this.generatePdfInProgress.set(true);

    this.reportingController.generateReport()
      .pipe(
        finalize(() => this.generatePdfInProgress.set(false)),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: (blob: Blob) => {
          if (blob.type !== 'application/pdf') {
            pdfWindow?.close();
            this.notification.error('Die Antwort war kein PDF.');
            return;
          }

          const fileURL = URL.createObjectURL(blob);
          if (pdfWindow) {
            pdfWindow.location.href = fileURL;
          } else {
            this.notification.error('Das PDF-Fenster wurde vom Browser blockiert.');
          }
          this.notification.success('PDF erfolgreich generiert.');
          setTimeout(() => URL.revokeObjectURL(fileURL), REVOKE_URL_DELAY_MS);
        },
        error: () => {
          pdfWindow?.close();
          this.notification.error('Fehler beim Generieren des PDF.');
        }
      });
  }
}
