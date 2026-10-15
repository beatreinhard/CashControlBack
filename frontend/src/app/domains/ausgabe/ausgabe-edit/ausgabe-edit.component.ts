import {ChangeDetectionStrategy, Component, computed, DestroyRef, effect, inject, input, signal} from '@angular/core';
import {rxResource, takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatOption} from '@angular/material/core';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatDialog} from '@angular/material/dialog';
import {MatError, MatFormField, MatHint, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressBar} from '@angular/material/progress-bar';
import {MatSelect} from '@angular/material/select';
import {Router, RouterLink} from '@angular/router';
import {filter, finalize, Observable, switchMap} from 'rxjs';
import {AusgabeControllerApi, AusgabeDto, AusgabeKategorieDto} from '../../../generated';
import {ConfirmDialogComponent, ConfirmDialogData} from '../../../shared/confirm-dialog/confirm-dialog.component';
import {NotificationService} from '../../../shared/notification.service';
import {parseBackendDate, toBackendDate} from '../../../shared/date-utils';
import {injectPersonOptions} from '../../../shared/person-options';

const MAX_TEXT_LENGTH = 255;

type AusgabeForm = FormGroup<{
  datum: FormControl<Date | null>;
  zahlender: FormControl<string | null>;
  empfaenger: FormControl<string>;
  kategorie: FormControl<AusgabeKategorieDto | null>;
  text: FormControl<string>;
  betrag: FormControl<number | null>;
}>;

@Component({
  selector: 'app-ausgabe-edit',
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
    MatOption,
    MatProgressBar,
    MatSelect,
    MatSuffix,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './ausgabe-edit.component.html',
  styleUrl: './ausgabe-edit.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AusgabeEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly ausgabeId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly ausgabeController = inject(AusgabeControllerApi);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroyRef = inject(DestroyRef);

  /** Lädt die Ausgabe im Edit-Modus; bei ausgabeId == null bleibt die Resource idle. */
  private readonly ausgabeResource = rxResource({
    request: () => this.ausgabeId() ?? undefined,
    loader: ({ request: id }) => this.ausgabeController.getAusgabeById(id)
  });

  private readonly personOptions = injectPersonOptions();

  protected readonly kategorien = Object.values(AusgabeKategorieDto);
  protected readonly zahlender = this.personOptions.options;

  protected readonly isLoading = this.ausgabeResource.isLoading;
  protected readonly isSaving = signal(false);
  protected readonly loadError = computed(() =>
    this.ausgabeResource.error() ? 'Die Ausgabe konnte nicht geladen werden.' : null
  );
  protected readonly zahlenderError = this.personOptions.error;

  protected readonly isEditMode = computed(() => !!this.ausgabeId());
  protected readonly title = computed(() => (this.isEditMode() ? 'Ausgabe bearbeiten' : 'Neue Ausgabe erfassen'));

  protected readonly maxTextLength = MAX_TEXT_LENGTH;
  protected readonly form: AusgabeForm = this.buildForm();

  constructor() {
    // Bridge: Resource-Signal -> imperatives Reactive-Forms-API
    effect(() => {
      if (!this.ausgabeId()) {
        this.resetFormForCreate();
        return;
      }
      const ausgabe = this.ausgabeResource.value();
      if (ausgabe) this.resetFormFromDto(ausgabe);
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
          this.notification.success('Ausgabe wurde erfolgreich gespeichert.');
          this.router.navigate(['/ausgabe']);
        },
        error: () => this.notification.error('Fehler beim Speichern der Ausgabe.')
      });
  }

  delete(): void {
    const id = this.ausgabeId();
    if (!id || this.isSaving()) return;

    this.dialog
      .open<ConfirmDialogComponent, ConfirmDialogData, boolean>(ConfirmDialogComponent, {
        data: {
          title: 'Ausgabe löschen',
          message: 'Soll diese Ausgabe wirklich gelöscht werden?',
          confirmLabel: 'Löschen'
        }
      })
      .afterClosed()
      .pipe(
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.isSaving.set(true);
          return this.ausgabeController.deleteAusgabeById(id).pipe(finalize(() => this.isSaving.set(false)));
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.notification.success('Ausgabe wurde gelöscht.');
          this.router.navigate(['/ausgabe']);
        },
        error: () => this.notification.error('Fehler beim Löschen der Ausgabe.')
      });
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): AusgabeForm {
    return this.fb.group({
      datum: this.fb.control<Date | null>(null, [Validators.required]),
      zahlender: this.fb.control<string | null>(null),
      empfaenger: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      kategorie: this.fb.control<AusgabeKategorieDto | null>(null, [Validators.required]),
      text: this.fb.nonNullable.control('', [Validators.maxLength(MAX_TEXT_LENGTH)]),
      betrag: this.fb.control<number | null>(null, [Validators.required, Validators.min(0.01)])
    });
  }

  private resetFormForCreate(): void {
    this.form.reset({
      datum: null,
      zahlender: null,
      empfaenger: '',
      kategorie: null,
      text: '',
      betrag: null
    });
  }

  private resetFormFromDto(ausgabe: AusgabeDto): void {
    this.form.reset({
      datum: parseBackendDate(ausgabe.datum),
      zahlender: ausgabe.zahlender ?? null,
      empfaenger: ausgabe.empfaenger ?? '',
      kategorie: ausgabe.kategorie ?? null,
      text: ausgabe.text ?? '',
      betrag: ausgabe.betrag ?? null
    });
  }

  private buildDtoFromForm(): AusgabeDto {
    const raw = this.form.getRawValue();

    return {
      id: this.ausgabeId() ?? undefined,
      // required-Validatoren garantieren Datum, Kategorie und Betrag
      datum: toBackendDate(raw.datum)!,
      zahlender: raw.zahlender ?? undefined,
      empfaenger: raw.empfaenger.trim(),
      kategorie: raw.kategorie!,
      text: raw.text.trim(),
      betrag: Number(raw.betrag)
    };
  }

  private saveRequest$(dto: AusgabeDto): Observable<unknown> {
    const id = this.ausgabeId();
    return id ? this.ausgabeController.updateAusgabe(id, dto) : this.ausgabeController.createAusgabe(dto);
  }
}
