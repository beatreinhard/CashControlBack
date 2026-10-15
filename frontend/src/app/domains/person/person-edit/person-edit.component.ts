import {Component, computed, DestroyRef, effect, inject, input, signal} from '@angular/core';
import {rxResource, takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from '@angular/material/input';
import {PersonControllerApi, PersonDto} from '../../../generated';
import {Router, RouterLink} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatProgressBar} from '@angular/material/progress-bar';
import {filter, finalize, Observable, switchMap} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {ConfirmDialogComponent, ConfirmDialogData} from '../../../shared/confirm-dialog/confirm-dialog.component';
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
    MatProgressBar,
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
  private readonly snackBar = inject(MatSnackBar);
  private readonly dialog = inject(MatDialog);
  private readonly destroyRef = inject(DestroyRef);

  /** Lädt die Person im Edit-Modus; bei personId == null bleibt die Resource idle. */
  private readonly personResource = rxResource({
    request: () => this.personId() ?? undefined,
    loader: ({ request: id }) => this.personController.getPersonById(id)
  });

  protected readonly isLoading = this.personResource.isLoading;
  protected readonly isSaving = signal(false);
  protected readonly loadError = computed(() =>
    this.personResource.error() ? 'Die Person konnte nicht geladen werden.' : null
  );

  protected readonly isEditMode = computed(() => !!this.personId());
  protected readonly title = computed(() => (this.isEditMode() ? 'Person bearbeiten' : 'Neue Person erfassen'));

  protected readonly form: PersonForm = this.buildForm();

  constructor() {
    // Bridge: Resource-Signal -> imperatives Reactive-Forms-API
    effect(() => {
      if (!this.personId()) {
        this.resetFormForCreate();
        return;
      }
      const person = this.personResource.value();
      if (person) this.patchFormFromDto(person);
    });
  }

  save(): void {
    if (this.isSaving()) return;
    if (this.form.invalid) {
      this.markFormTouched();
      return;
    }

    const dto = this.buildDtoFromForm();
    this.isSaving.set(true);

    this.saveRequest$(dto)
      .pipe(
        finalize(() => this.isSaving.set(false)),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.showSuccess('Person wurde erfolgreich gespeichert.');
          this.router.navigate(['/person']);
        },
        error: (err) => {
          console.error('Fehler beim Speichern der Person:', err);
          this.showError('Fehler beim Speichern der Person.');
        }
      });
  }

  delete(): void {
    const id = this.personId();
    if (!id || this.isSaving()) return;

    this.dialog
      .open<ConfirmDialogComponent, ConfirmDialogData, boolean>(ConfirmDialogComponent, {
        data: {
          title: 'Person löschen',
          message: 'Soll diese Person wirklich gelöscht werden?',
          confirmLabel: 'Löschen'
        }
      })
      .afterClosed()
      .pipe(
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.isSaving.set(true);
          return this.personController.deletePersonById(id).pipe(finalize(() => this.isSaving.set(false)));
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.showSuccess('Person wurde gelöscht.');
          this.router.navigate(['/person']);
        },
        error: (err) => {
          console.error('Fehler beim Löschen der Person:', err);
          this.showError('Fehler beim Löschen der Person.');
        }
      });
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

  private resetFormForCreate(): void {
    this.form.reset({
      name: '',
      vorname: '',
      geburtsdatum: null,
      ahvnummer: ''
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
