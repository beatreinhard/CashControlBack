import {Component, computed, effect, inject, input, signal} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AusgabeControllerApi, AusgabeDto, AusgabeKategorieDto} from '../../../generated';
import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {format, parseISO} from 'date-fns';
import {Observable} from 'rxjs';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Router, RouterLink} from '@angular/router';

type AusgabeForm = FormGroup<{
  datum: FormControl<Date | null>;
  empfaenger: FormControl<string>;
  kategorie: FormControl<AusgabeKategorieDto | null>;
  text: FormControl<string | null>;
  betrag: FormControl<number | null>;
}>;

@Component({
  selector: 'app-ausgabe-edit',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatIconModule,
    MatCardModule,
    MatSelectModule,
    MatButtonModule,
    RouterLink
  ],
  templateUrl: './ausgabe-edit.component.html',
  styleUrl: './ausgabe-edit.component.css'
})
export class AusgabeEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly ausgabeId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly ausgabeController = inject(AusgabeControllerApi);
  private readonly router = inject(Router);

  protected readonly kategorien = Object.values(AusgabeKategorieDto);

  protected readonly isLoading = signal(false);
  protected readonly loadError = signal<string | null>(null);

  protected readonly isEditMode = computed(() => !!this.ausgabeId());
  protected readonly title = computed(() => (this.isEditMode() ? 'Ausgabe bearbeiten' : 'Neue Ausgabe erfassen'));

  private readonly snackBar = inject(MatSnackBar);
  protected readonly form: AusgabeForm = this.buildForm();

  constructor() {
    effect(() => this.handleIdChange(this.ausgabeId()));
  }

  save(): void {
    if (this.form.invalid) {
      this.markFormTouched();
      return;
    }

    const dto = this.buildDtoFromForm();
    const request$ = this.saveRequest$(dto);

    request$.subscribe({
      next: (result) => {
        console.log('Ausgabe gespeichert:', result);
        this.showSuccess('Ausgabe wurde erfolgreich gespeichert.');
        // optional: Navigation / Reset / Emit
      },
      error: (err) => {
        console.error('Fehler beim Speichern der Ausgabe:', err);
        this.showError('Fehler beim Speichern der Ausgabe.');
      }
    });
  }

  delete(): void {
    if (this.ausgabeId() != null) {
      this.ausgabeController.deleteAusgabeById(this.ausgabeId()!).subscribe();
      this.router.navigate(['/ausgabe']);
    }
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): AusgabeForm {
    return this.fb.group({
      datum: this.fb.control<Date | null>(null, { validators: [Validators.required] }),
      empfaenger: this.fb.control<string>('', { nonNullable: true, validators: [Validators.required] }),
      kategorie: this.fb.control<AusgabeKategorieDto | null>(null, { validators: [Validators.required] }),
      text: this.fb.control<string | null>(null),
      betrag: this.fb.control<number | null>(null, { validators: [Validators.required, Validators.min(0.01)] })
    });
  }

  private handleIdChange(id: string | null): void {
    if (!id) {
      this.resetFormForCreate();
      return;
    }
    this.loadForEdit(id);
  }

  private resetFormForCreate(): void {
    this.loadError.set(null);
    this.form.reset({
      datum: null,
      empfaenger: '',
      kategorie: null,
      text: null,
      betrag: null
    });
  }

  private loadForEdit(id: string): void {
    this.setLoading(true);

    this.ausgabeController.getAusgabeById(id).subscribe({
      next: (ausgabe) => {
        this.patchFormFromDto(ausgabe);
        this.setLoading(false);
      },
      error: (err) => {
        console.error('Fehler beim Laden der Ausgabe', err);
        this.loadError.set('Die Ausgabe konnte nicht geladen werden.');
        this.setLoading(false);
      }
    });
  }

  private patchFormFromDto(ausgabe: AusgabeDto): void {
    this.form.patchValue({
      datum: this.parseBackendDate(ausgabe.datum),
      empfaenger: ausgabe.empfaenger ?? '',
      kategorie: ausgabe.kategorie ?? null,
      text: ausgabe.text ?? null,
      betrag: ausgabe.betrag ?? null
    });
  }

  private buildDtoFromForm(): AusgabeDto {
    const raw = this.form.getRawValue();

    return {
      datum: this.toBackendDate(raw.datum) ?? '',
      empfaenger: raw.empfaenger,
      kategorie: raw.kategorie!,
      text: raw.text ?? '',
      betrag: Number(raw.betrag),
      id: this.ausgabeId() ?? undefined
    };
  }

  private saveRequest$(dto: AusgabeDto): Observable<unknown> {
    const id = this.ausgabeId();
    return id ? this.ausgabeController.updateAusgabe(id, dto) : this.ausgabeController.createAusgabe(dto);
  }

  private markFormTouched(): void {
    this.form.markAllAsTouched();
  }

  private setLoading(isLoading: boolean): void {
    this.isLoading.set(isLoading);
    if (isLoading) this.loadError.set(null);
  }

  private parseBackendDate(value?: string | null): Date | null {
    return value ? parseISO(value) : null;
  }

  private toBackendDate(date: Date | null): string | undefined {
    if (!date) return undefined;
    return format(date, 'yyyy-MM-dd');
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
