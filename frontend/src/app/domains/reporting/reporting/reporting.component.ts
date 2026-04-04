import {Component, inject, signal} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatCardActions} from '@angular/material/card';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {ReportingControllerApi} from '../../../generated';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-reporting',
  imports: [
    MatButton,
    MatCardActions,
    MatProgressSpinner
  ],
  templateUrl: './reporting.component.html',
  styleUrl: './reporting.component.css'
})
export class ReportingComponent {

  readonly generatePdfInProgress = signal(false);

  protected reportingController = inject(ReportingControllerApi);
  private readonly snackBar = inject(MatSnackBar);

  protected pdfGenerieren() {
    this.generatePdfInProgress .set(true);

    this.reportingController.generateReport().subscribe({
      next: (blob: Blob) => {
        this.generatePdfInProgress.set(false);
        const newWindow = window.open('', '_blank');

        if (blob.type !== 'application/pdf') {
          this.showError('Die Antwort war kein PDF.');
          if (newWindow) {
            newWindow.close();
          }
          return;
        }

        const fileURL = URL.createObjectURL(blob);

        if (newWindow) {
          newWindow.location.href = fileURL;
        }

        this.showSuccess('PDF erfolgreich generiert.');
        setTimeout(() => URL.revokeObjectURL(fileURL), 10000);
      },
      error: (err) => {
        console.error('API error:', err);
        this.showError("Fehler beim generieren des PDF.")
        this.generatePdfInProgress.set(false);
      },
    });
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'OK', {
      duration: 3000,
      panelClass: ['snackbar-success'],
      horizontalPosition: 'right',
      verticalPosition: 'top'
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Schließen', {
      duration: 5000,
      panelClass: ['snackbar-error'],
      horizontalPosition: 'right',
      verticalPosition: 'top'
    });
  }
}
