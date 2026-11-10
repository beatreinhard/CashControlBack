import {ChangeDetectionStrategy, Component, inject, output} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatIcon} from '@angular/material/icon';
import {MatInput} from '@angular/material/input';

export const MIN_YEAR = 2000;

/** Filterformular für ein (optionales) Jahr. Leer => alle Jahre. */
@Component({
  selector: 'app-year-filter',
  imports: [
    MatButton,
    MatError,
    MatFormField,
    MatIcon,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  template: `
    <form class="cc-year-filter" [formGroup]="form" (ngSubmit)="submit()">
      <mat-form-field subscriptSizing="dynamic">
        <mat-label>Jahr</mat-label>
        <input matInput type="number" formControlName="jahr" placeholder="Alle" />
        <mat-error>Jahr muss {{ minYear }} oder grösser sein</mat-error>
      </mat-form-field>
      <button mat-flat-button type="submit"><mat-icon>filter_alt</mat-icon>Filtern</button>
    </form>
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
