import {ChangeDetectionStrategy, Component, computed, DestroyRef, effect, inject, input, signal} from '@angular/core';
import {rxResource, takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatError, MatFormField, MatHint, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressBar} from '@angular/material/progress-bar';
import {MatDialog} from '@angular/material/dialog';
import {Router, RouterLink} from '@angular/router';
import {filter, finalize, Observable, switchMap} from 'rxjs';
import {PersonControllerApi, PersonDto} from '../../../generated';
import {ConfirmDialogComponent, ConfirmDialogData} from '../../../shared/confirm-dialog/confirm-dialog.component';
import {NotificationService} from '../../../shared/notification.service';
import {parseBackendDate, toBackendDate} from '../../../shared/date-utils';
import {AHV_PATTERN, notInFutureValidator} from '../../../shared/validators';

const MAX_TEXT_LENGTH = 255;

type PersonForm = FormGroup<{
  name: FormControl<string>;
  vorname: FormControl<string>;
  geburtsdatum: FormControl<Date | null>;
  ahvnummer: FormControl<string>;
}>;

@Component({
  selector: 'app-person-edit',
  imports: [
    MatAnchor,
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
    MatHint,
    MatInput,
    MatLabel,
    MatProgressBar,
    MatSuffix,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './person-edit.component.html',
  styleUrl: './person-edit.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PersonEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly personId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly personController = inject(PersonControllerApi);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);
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

  protected readonly maxTextLength = MAX_TEXT_LENGTH;
  protected readonly form: PersonForm = this.buildForm();

  constructor() {
    // Bridge: Resource-Signal -> imperatives Reactive-Forms-API
    effect(() => {
      if (!this.personId()) {
        this.resetFormForCreate();
        return;
      }
      const person = this.personResource.value();
      if (person) this.resetFormFromDto(person);
    });
  }

  save(): void {
    if (this.isSaving()) return;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
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
          this.notification.success('Person wurde erfolgreich gespeichert.');
          this.router.navigate(['/person']);
        },
        error: () => this.notification.error('Fehler beim Speichern der Person.')
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
          this.notification.success('Person wurde gelöscht.');
          this.router.navigate(['/person']);
        },
        error: () => this.notification.error('Fehler beim Löschen der Person.')
      });
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): PersonForm {
    return this.fb.group({
      name: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      vorname: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      geburtsdatum: this.fb.control<Date | null>(null, [Validators.required, notInFutureValidator()]),
      ahvnummer: this.fb.nonNullable.control('', [Validators.pattern(AHV_PATTERN)])
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

  private resetFormFromDto(person: PersonDto): void {
    this.form.reset({
      name: person.name,
      vorname: person.vorname,
      geburtsdatum: parseBackendDate(person.geburtsdatum),
      ahvnummer: person.ahvnummer ?? ''
    });
  }

  private buildDtoFromForm(): PersonDto {
    const raw = this.form.getRawValue();

    return {
      id: this.personId() ?? undefined,
      name: raw.name.trim(),
      vorname: raw.vorname.trim(),
      // required-Validator garantiert ein Datum
      geburtsdatum: toBackendDate(raw.geburtsdatum)!,
      ahvnummer: raw.ahvnummer.trim() || undefined
    };
  }

  private saveRequest$(dto: PersonDto): Observable<unknown> {
    const id = this.personId();
    return id ? this.personController.updatePerson(id, dto) : this.personController.createPerson(dto);
  }
}
