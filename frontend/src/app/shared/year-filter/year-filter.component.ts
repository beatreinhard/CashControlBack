import {ChangeDetectionStrategy, Component, inject, output} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';

export const MIN_YEAR = 2000;

/** Filterformular für ein (optionales) Jahr. Leer => alle Jahre. */
@Component({
  selector: 'app-year-filter',
  imports: [
    MatButton,
    MatCard,
    MatCardActions,
    MatCardContent,
    MatCardTitle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  template: `
    <form [formGroup]="form" (ngSubmit)="submit()">
      <mat-card>
        <mat-card-title>Filter</mat-card-title>
        <mat-card-content>
          <mat-form-field class="full-width">
            <mat-label>Jahr</mat-label>
            <input matInput type="number" formControlName="jahr" />
            <mat-error>Jahr muss {{ minYear }} oder grösser sein</mat-error>
          </mat-form-field>
        </mat-card-content>
        <mat-card-actions align="end">
          <button mat-flat-button color="primary" type="submit">Filtern</button>
        </mat-card-actions>
      </mat-card>
    </form>
  `,
  styles: `
    .full-width { width: 100%; }
    form { margin-bottom: 16px; }
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class YearFilterComponent {
  readonly jahrChange = output<number | undefined>();

  protected readonly minYear = MIN_YEAR;
  protected readonly form = inject(FormBuilder).group({
    jahr: [null as number | null, [Validators.min(MIN_YEAR)]]
  });

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.jahrChange.emit(this.form.controls.jahr.value ?? undefined);
  }
}
