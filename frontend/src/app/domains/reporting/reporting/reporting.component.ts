import {Component, inject, signal} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {ReportingControllerApi} from '../../../generated';
import {MatSnackBar} from '@angular/material/snack-bar';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';

type ReportingForm  = FormGroup<{
  kontrollJahr: FormControl<number>;
}>;

@Component({
  selector: 'app-reporting',
  imports: [
    MatButton,
    MatCardActions,
    MatProgressSpinner,
    FormsModule,
    MatCard,
    MatCardContent,
    MatCardTitle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './reporting.component.html',
  styleUrl: './reporting.component.css'
})
export class ReportingComponent {

  readonly generatePdfInProgress = signal(false);

  private readonly fb = inject(FormBuilder);
  protected readonly form: ReportingForm = this.buildForm();
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

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): ReportingForm {
    return this.fb.group({
      kontrollJahr: this.fb.control<number>(2026, { nonNullable: true, validators: [Validators.required] })
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
