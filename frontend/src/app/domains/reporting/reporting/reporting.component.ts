import {ChangeDetectionStrategy, Component, DestroyRef, inject, signal} from '@angular/core';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatHint, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressBar} from '@angular/material/progress-bar';
import {finalize} from 'rxjs';
import {ReportingControllerApi} from '../../../generated';
import {NotificationService} from '../../../shared/notification.service';
import {MIN_YEAR} from '../../../shared/year-filter/year-filter.component';

const REVOKE_URL_DELAY_MS = 10_000;

@Component({
  selector: 'app-reporting',
  imports: [
    MatButton,
    MatCard,
    MatCardActions,
    MatCardContent,
    MatCardTitle,
    MatError,
    MatFormField,
    MatHint,
    MatInput,
    MatLabel,
    MatProgressBar,
    ReactiveFormsModule
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

  protected readonly minYear = MIN_YEAR;
  protected readonly maxYear = new Date().getFullYear();

  /** Steuerjahr des Reports; vorbelegt mit dem Vorjahr, da die Steuererklärung rückwirkend erstellt wird. */
  protected readonly form = inject(FormBuilder).group({
    jahr: [this.maxYear - 1 as number | null, [Validators.required, Validators.min(MIN_YEAR), Validators.max(this.maxYear)]]
  });

  protected pdfGenerieren(): void {
    if (this.generatePdfInProgress()) return;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const jahr = this.form.controls.jahr.value!;

    // Fenster synchron im Klick-Handler öffnen, sonst blockiert der Popup-Blocker
    const pdfWindow = window.open('', '_blank');
    this.generatePdfInProgress.set(true);

    this.reportingController.generateReport(jahr)
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
