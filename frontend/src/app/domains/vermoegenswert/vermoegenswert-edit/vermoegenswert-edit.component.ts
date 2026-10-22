import {ChangeDetectionStrategy, Component, computed, DestroyRef, effect, inject, input, signal} from '@angular/core';
import {rxResource, takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatDialog} from '@angular/material/dialog';
import {MatError, MatFormField, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressBar} from '@angular/material/progress-bar';
import {Router, RouterLink} from '@angular/router';
import {filter, finalize, Observable, switchMap} from 'rxjs';
import {VermoegenswertControllerApi, VermoegenswertDto} from '../../../generated';
import {ConfirmDialogComponent, ConfirmDialogData} from '../../../shared/confirm-dialog/confirm-dialog.component';
import {NotificationService} from '../../../shared/notification.service';
import {MIN_YEAR} from '../../../shared/year-filter/year-filter.component';

const MAX_TEXT_LENGTH = 255;
const MIN_ANSCHAFFUNGSJAHR = 1900;

type VermoegenswertForm = FormGroup<{
  jahr: FormControl<number | null>;
  bezeichnung: FormControl<string>;
  anschaffungsjahr: FormControl<number | null>;
  anschaffungspreis: FormControl<number | null>;
}>;

/** Fehler 'nachSteuerjahr', wenn das Anschaffungsjahr nach dem Steuerjahr liegt. */
function nichtNachSteuerjahr(control: AbstractControl<number | null>): ValidationErrors | null {
  const jahr = control.parent?.get('jahr')?.value;
  if (control.value == null || jahr == null) return null;
  return Number(control.value) > Number(jahr) ? { nachSteuerjahr: true } : null;
}

@Component({
  selector: 'app-vermoegenswert-edit',
  imports: [
    MatAnchor,
    MatButton,
    MatCard,
    MatCardActions,
    MatCardContent,
    MatCardTitle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    MatProgressBar,
    MatSuffix,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './vermoegenswert-edit.component.html',
  styleUrl: './vermoegenswert-edit.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VermoegenswertEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly vermoegenswertId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly vermoegenswertController = inject(VermoegenswertControllerApi);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroyRef = inject(DestroyRef);

  /** Lädt den Vermögenswert im Edit-Modus; bei vermoegenswertId == null bleibt die Resource idle. */
  private readonly vermoegenswertResource = rxResource({
    request: () => this.vermoegenswertId() ?? undefined,
    loader: ({ request: id }) => this.vermoegenswertController.getVermoegenswertById(id)
  });

  protected readonly isLoading = this.vermoegenswertResource.isLoading;
  protected readonly isSaving = signal(false);
  protected readonly loadError = computed(() =>
    this.vermoegenswertResource.error() ? 'Der Vermögenswert konnte nicht geladen werden.' : null
  );

  protected readonly isEditMode = computed(() => !!this.vermoegenswertId());
  protected readonly title = computed(() =>
    this.isEditMode() ? 'Vermögenswert bearbeiten' : 'Neuen Vermögenswert erfassen'
  );

  protected readonly minYear = MIN_YEAR;
  protected readonly minAnschaffungsjahr = MIN_ANSCHAFFUNGSJAHR;
  protected readonly maxTextLength = MAX_TEXT_LENGTH;
  protected readonly form: VermoegenswertForm = this.buildForm();

  constructor() {
    // Bridge: Resource-Signal -> imperatives Reactive-Forms-API
    effect(() => {
      if (!this.vermoegenswertId()) {
        this.resetFormForCreate();
        return;
      }
      const vermoegenswert = this.vermoegenswertResource.value();
      if (vermoegenswert) this.resetFormFromDto(vermoegenswert);
    });

    // Anschaffungsjahr hängt vom Steuerjahr ab -> bei Änderung neu validieren
    this.form.controls.jahr.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.form.controls.anschaffungsjahr.updateValueAndValidity({ emitEvent: false }));
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
          this.notification.success('Vermögenswert wurde erfolgreich gespeichert.');
          this.router.navigate(['/vermoegenswert']);
        },
        error: () => this.notification.error('Fehler beim Speichern des Vermögenswerts.')
      });
  }

  delete(): void {
    const id = this.vermoegenswertId();
    if (!id || this.isSaving()) return;

    this.dialog
      .open<ConfirmDialogComponent, ConfirmDialogData, boolean>(ConfirmDialogComponent, {
        data: {
          title: 'Vermögenswert löschen',
          message: 'Soll dieser Vermögenswert wirklich gelöscht werden?',
          confirmLabel: 'Löschen'
        }
      })
      .afterClosed()
      .pipe(
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.isSaving.set(true);
          return this.vermoegenswertController
            .deleteVermoegenswertById(id)
            .pipe(finalize(() => this.isSaving.set(false)));
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.notification.success('Vermögenswert wurde gelöscht.');
          this.router.navigate(['/vermoegenswert']);
        },
        error: () => this.notification.error('Fehler beim Löschen des Vermögenswerts.')
      });
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): VermoegenswertForm {
    return this.fb.group({
      jahr: this.fb.control<number | null>(this.defaultYear(), [Validators.required, Validators.min(MIN_YEAR)]),
      bezeichnung: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      anschaffungsjahr: this.fb.control<number | null>(null, [
        Validators.required, Validators.min(MIN_ANSCHAFFUNGSJAHR), nichtNachSteuerjahr
      ]),
      anschaffungspreis: this.fb.control<number | null>(null, [Validators.required, Validators.min(0)])
    });
  }

  private resetFormForCreate(): void {
    this.form.reset({
      jahr: this.defaultYear(),
      bezeichnung: '',
      anschaffungsjahr: null,
      anschaffungspreis: null
    });
  }

  private resetFormFromDto(vermoegenswert: VermoegenswertDto): void {
    this.form.reset({
      jahr: vermoegenswert.jahr ?? this.defaultYear(),
      bezeichnung: vermoegenswert.bezeichnung ?? '',
      anschaffungsjahr: vermoegenswert.anschaffungsjahr ?? null,
      anschaffungspreis: vermoegenswert.anschaffungspreis ?? null
    });
  }

  private buildDtoFromForm(): VermoegenswertDto {
    const raw = this.form.getRawValue();

    return {
      id: this.vermoegenswertId() ?? undefined,
      // required-Validatoren garantieren Jahr, Anschaffungsjahr und Anschaffungspreis
      jahr: raw.jahr!,
      bezeichnung: raw.bezeichnung.trim(),
      anschaffungsjahr: Number(raw.anschaffungsjahr),
      anschaffungspreis: Number(raw.anschaffungspreis)
    };
  }

  private saveRequest$(dto: VermoegenswertDto): Observable<unknown> {
    const id = this.vermoegenswertId();
    return id
      ? this.vermoegenswertController.updateVermoegenswert(id, dto)
      : this.vermoegenswertController.createVermoegenswert(dto);
  }

  private defaultYear(): number {
    return new Date().getFullYear();
  }
}
