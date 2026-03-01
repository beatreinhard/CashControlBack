import {Component, computed, effect, inject, input, signal} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from '@angular/material/input';
import {PersonControllerApi, PersonDto} from '../../../generated';
import {Router, RouterLink} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Observable} from 'rxjs';
import {format, parseISO} from 'date-fns';


type PersonForm = FormGroup<{
  name: FormControl<string>;
  vorname: FormControl<string>;
  geburtsdatum: FormControl<Date | null>;
  ahvnummer: FormControl<string | null>;
}>;

@Component({
  selector: 'app-person-edit',
  imports: [
    FormsModule,
    MatButton,
    MatCard,
    MatCardActions,
    MatCardContent,
    MatCardTitle,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    MatSuffix,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './person-edit.component.html',
  styleUrl: './person-edit.component.css'
})
export class PersonEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly personId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly personController = inject(PersonControllerApi);
  private readonly router = inject(Router);

  protected readonly isLoading = signal(false);
  protected readonly loadError = signal<string | null>(null);

  protected readonly isEditMode = computed(() => !!this.personId());
  protected readonly title = computed(() => (this.isEditMode() ? 'Person bearbeiten' : 'Neue Person erfassen'));

  private readonly snackBar = inject(MatSnackBar);
  protected readonly form: PersonForm = this.buildForm();

  constructor() {
    effect(() => this.handleIdChange(this.personId()));
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
        console.log('Person gespeichert:', result);
        this.showSuccess('Person wurde erfolgreich gespeichert.');
        // optional: Navigation / Reset / Emit
      },
      error: (err) => {
        console.error('Fehler beim Speichern der Person:', err);
        this.showError('Fehler beim Speichern der Person.');
      }
    });
  }

  delete(): void {
    if (this.personId() != null) {
      this.personController.deletePersonById(this.personId()!).subscribe();
      this.router.navigate(['/person']);
    }
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): PersonForm {
    return this.fb.group({
      name: this.fb.control<string>('', { nonNullable: true, validators: [Validators.required] }),
      vorname: this.fb.control<string>('', { nonNullable: true, validators: [Validators.required] }),
      geburtsdatum: this.fb.control<Date | null>(null, { nonNullable: true, validators: [Validators.required] }),
      ahvnummer: this.fb.control<string | null>(null)
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
      name: '',
      vorname: '',
      geburtsdatum: null,
      ahvnummer: ''
    });
  }

  private loadForEdit(id: string): void {
    this.setLoading(true);

    this.personController.getPersonById(id).subscribe({
      next: (person) => {
        this.patchFormFromDto(person);
        this.setLoading(false);
      },
      error: (err) => {
        console.error('Fehler beim Laden der Person', err);
        this.loadError.set('Die Person konnte nicht geladen werden.');
        this.setLoading(false);
      }
    });
  }

  private patchFormFromDto(person: PersonDto): void {
    this.form.patchValue({
      name: person.name,
      vorname: person.vorname,
      geburtsdatum: this.parseBackendDate(person.geburtsdatum),
      ahvnummer: person.ahvnummer ?? null
    });
  }

  private buildDtoFromForm(): PersonDto {
    const raw = this.form.getRawValue();

    return {
      name: raw.name,
      vorname: raw.vorname,
      geburtsdatum: this.toBackendDate(raw.geburtsdatum) ?? '',
      ahvnummer: raw.ahvnummer ?? '',
      id: this.personId() ?? undefined
    };
  }

  private saveRequest$(dto: PersonDto): Observable<unknown> {
    const id = this.personId();
    return id ? this.personController.updatePerson(id, dto) : this.personController.createPerson(dto);
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
