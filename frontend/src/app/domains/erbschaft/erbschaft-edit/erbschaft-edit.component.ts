import {ChangeDetectionStrategy, Component, computed, DestroyRef, effect, inject, input, signal} from '@angular/core';
import {rxResource, takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatOption} from '@angular/material/core';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatDialog} from '@angular/material/dialog';
import {MatError, MatFormField, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressBar} from '@angular/material/progress-bar';
import {MatSelect} from '@angular/material/select';
import {Router, RouterLink} from '@angular/router';
import {filter, finalize, Observable, switchMap} from 'rxjs';
import {ErbschaftArtDto, ErbschaftControllerApi, ErbschaftDto} from '../../../generated';
import {ConfirmDialogComponent, ConfirmDialogData} from '../../../shared/confirm-dialog/confirm-dialog.component';
import {parseBackendDate, toBackendDate} from '../../../shared/date-utils';
import {NotificationService} from '../../../shared/notification.service';
import {notInFutureValidator} from '../../../shared/validators';
import {MIN_YEAR} from '../../../shared/year-filter/year-filter.component';

const MAX_TEXT_LENGTH = 255;

type ErbschaftForm = FormGroup<{
  jahr: FormControl<number | null>;
  art: FormControl<ErbschaftArtDto | null>;
  datum: FormControl<Date | null>;
  betrag: FormControl<number | null>;
  geber: FormControl<string>;
  verwandtschaftsverhaeltnis: FormControl<string>;
  gegenstand: FormControl<string>;
  bemerkung: FormControl<string>;
}>;

@Component({
  selector: 'app-erbschaft-edit',
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
    MatInput,
    MatLabel,
    MatOption,
    MatProgressBar,
    MatSelect,
    MatSuffix,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './erbschaft-edit.component.html',
  styleUrl: './erbschaft-edit.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ErbschaftEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly erbschaftId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly erbschaftController = inject(ErbschaftControllerApi);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroyRef = inject(DestroyRef);

  /** Lädt die Erbschaft im Edit-Modus; bei erbschaftId == null bleibt die Resource idle. */
  private readonly erbschaftResource = rxResource({
    request: () => this.erbschaftId() ?? undefined,
    loader: ({ request: id }) => this.erbschaftController.getErbschaftById(id)
  });

  protected readonly artValues = Object.values(ErbschaftArtDto);

  protected readonly isLoading = this.erbschaftResource.isLoading;
  protected readonly isSaving = signal(false);
  protected readonly loadError = computed(() =>
    this.erbschaftResource.error() ? 'Die Erbschaft konnte nicht geladen werden.' : null
  );

  protected readonly isEditMode = computed(() => !!this.erbschaftId());
  protected readonly title = computed(() => (this.isEditMode() ? 'Erbschaft bearbeiten' : 'Neue Erbschaft erfassen'));

  protected readonly minYear = MIN_YEAR;
  protected readonly maxTextLength = MAX_TEXT_LENGTH;
  protected readonly form: ErbschaftForm = this.buildForm();

  constructor() {
    // Bridge: Resource-Signal -> imperatives Reactive-Forms-API
    effect(() => {
      if (!this.erbschaftId()) {
        this.resetFormForCreate();
        return;
      }
      const erbschaft = this.erbschaftResource.value();
      if (erbschaft) this.resetFormFromDto(erbschaft);
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
          this.notification.success('Erbschaft wurde erfolgreich gespeichert.');
          this.router.navigate(['/erbschaft']);
        },
        error: () => this.notification.error('Fehler beim Speichern der Erbschaft.')
      });
  }

  delete(): void {
    const id = this.erbschaftId();
    if (!id || this.isSaving()) return;

    this.dialog
      .open<ConfirmDialogComponent, ConfirmDialogData, boolean>(ConfirmDialogComponent, {
        data: {
          title: 'Erbschaft löschen',
          message: 'Soll diese Erbschaft wirklich gelöscht werden?',
          confirmLabel: 'Löschen'
        }
      })
      .afterClosed()
      .pipe(
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.isSaving.set(true);
          return this.erbschaftController.deleteErbschaftById(id).pipe(finalize(() => this.isSaving.set(false)));
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.notification.success('Erbschaft wurde gelöscht.');
          this.router.navigate(['/erbschaft']);
        },
        error: () => this.notification.error('Fehler beim Löschen der Erbschaft.')
      });
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): ErbschaftForm {
    return this.fb.group({
      jahr: this.fb.control<number | null>(this.defaultYear(), [Validators.required, Validators.min(MIN_YEAR)]),
      art: this.fb.control<ErbschaftArtDto | null>(null, [Validators.required]),
      datum: this.fb.control<Date | null>(null, [Validators.required, notInFutureValidator()]),
      betrag: this.fb.control<number | null>(null, [Validators.required, Validators.min(0.01)]),
      geber: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      verwandtschaftsverhaeltnis: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      gegenstand: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      bemerkung: this.fb.nonNullable.control('', [Validators.maxLength(MAX_TEXT_LENGTH)])
    });
  }

  private resetFormForCreate(): void {
    this.form.reset({
      jahr: this.defaultYear(),
      art: null,
      datum: null,
      betrag: null,
      geber: '',
      verwandtschaftsverhaeltnis: '',
      gegenstand: '',
      bemerkung: ''
    });
  }

  private resetFormFromDto(erbschaft: ErbschaftDto): void {
    this.form.reset({
      jahr: erbschaft.jahr ?? this.defaultYear(),
      art: erbschaft.art ?? null,
      datum: parseBackendDate(erbschaft.datum),
      betrag: erbschaft.betrag ?? null,
      geber: erbschaft.geber ?? '',
      verwandtschaftsverhaeltnis: erbschaft.verwandtschaftsverhaeltnis ?? '',
      gegenstand: erbschaft.gegenstand ?? '',
      bemerkung: erbschaft.bemerkung ?? ''
    });
  }

  private buildDtoFromForm(): ErbschaftDto {
    const raw = this.form.getRawValue();

    return {
      id: this.erbschaftId() ?? undefined,
      // required-Validatoren garantieren Jahr, Art, Datum und Betrag
      jahr: raw.jahr!,
      art: raw.art!,
      datum: toBackendDate(raw.datum)!,
      betrag: Number(raw.betrag),
      geber: raw.geber.trim(),
      verwandtschaftsverhaeltnis: raw.verwandtschaftsverhaeltnis.trim(),
      gegenstand: raw.gegenstand.trim(),
      bemerkung: raw.bemerkung.trim() || undefined
    };
  }

  private saveRequest$(dto: ErbschaftDto): Observable<unknown> {
    const id = this.erbschaftId();
    return id ? this.erbschaftController.updateErbschaft(id, dto) : this.erbschaftController.createErbschaft(dto);
  }

  private defaultYear(): number {
    return new Date().getFullYear();
  }
}
