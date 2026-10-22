import {ChangeDetectionStrategy, Component, computed, DestroyRef, effect, inject, input, signal} from '@angular/core';
import {rxResource, takeUntilDestroyed, toSignal} from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import {DecimalPipe} from '@angular/common';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatDialog} from '@angular/material/dialog';
import {MatError, MatFormField, MatHint, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressBar} from '@angular/material/progress-bar';
import {Router, RouterLink} from '@angular/router';
import {filter, finalize, map, Observable, switchMap} from 'rxjs';
import {GrundstueckunterhaltControllerApi, GrundstueckunterhaltDto} from '../../../generated';
import {ConfirmDialogComponent, ConfirmDialogData} from '../../../shared/confirm-dialog/confirm-dialog.component';
import {parseBackendDate, toBackendDate} from '../../../shared/date-utils';
import {NotificationService} from '../../../shared/notification.service';
import {notInFutureValidator} from '../../../shared/validators';
import {MIN_YEAR} from '../../../shared/year-filter/year-filter.component';

const MAX_TEXT_LENGTH = 255;

type GrundstueckunterhaltForm = FormGroup<{
  jahr: FormControl<number | null>;
  rgDatum: FormControl<Date | null>;
  ausfuehrendeFirma: FormControl<string>;
  arbeitsArt: FormControl<string>;
  betragNetto: FormControl<number | null>;
  anteilAndereKosten: FormControl<number | null>;
}>;

/** Fehler 'groesserAlsNetto', wenn der Anteil andere Kosten den Nettobetrag übersteigt. */
function anteilNichtGroesserAlsNetto(control: AbstractControl<number | null>): ValidationErrors | null {
  const netto = control.parent?.get('betragNetto')?.value;
  if (control.value == null || netto == null) return null;
  return Number(control.value) > Number(netto) ? { groesserAlsNetto: true } : null;
}

@Component({
  selector: 'app-grundstueckunterhalt-edit',
  imports: [
    DecimalPipe,
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
  templateUrl: './grundstueckunterhalt-edit.component.html',
  styleUrl: './grundstueckunterhalt-edit.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GrundstueckunterhaltEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly grundstueckunterhaltId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly grundstueckunterhaltController = inject(GrundstueckunterhaltControllerApi);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroyRef = inject(DestroyRef);

  /** Lädt den Eintrag im Edit-Modus; bei grundstueckunterhaltId == null bleibt die Resource idle. */
  private readonly grundstueckunterhaltResource = rxResource({
    request: () => this.grundstueckunterhaltId() ?? undefined,
    loader: ({ request: id }) => this.grundstueckunterhaltController.getGrundstueckunterhaltById(id)
  });

  protected readonly isLoading = this.grundstueckunterhaltResource.isLoading;
  protected readonly isSaving = signal(false);
  protected readonly loadError = computed(() =>
    this.grundstueckunterhaltResource.error() ? 'Der Grundstückunterhalt konnte nicht geladen werden.' : null
  );

  protected readonly isEditMode = computed(() => !!this.grundstueckunterhaltId());
  protected readonly title = computed(() =>
    this.isEditMode() ? 'Grundstückunterhalt bearbeiten' : 'Neuen Grundstückunterhalt erfassen'
  );

  protected readonly minYear = MIN_YEAR;
  protected readonly maxTextLength = MAX_TEXT_LENGTH;
  protected readonly form: GrundstueckunterhaltForm = this.buildForm();

  /** Steuerlich relevanter Anteil: Betrag netto minus Anteil andere Kosten (wird nicht erfasst, sondern berechnet). */
  protected readonly anteilUnterhalt = toSignal(
    this.form.valueChanges.pipe(map(() => this.calculateAnteilUnterhalt())),
    { initialValue: this.calculateAnteilUnterhalt() }
  );

  constructor() {
    // Bridge: Resource-Signal -> imperatives Reactive-Forms-API
    effect(() => {
      if (!this.grundstueckunterhaltId()) {
        this.resetFormForCreate();
        return;
      }
      const eintrag = this.grundstueckunterhaltResource.value();
      if (eintrag) this.resetFormFromDto(eintrag);
    });

    // Anteil andere Kosten hängt vom Nettobetrag ab -> bei Änderung neu validieren
    this.form.controls.betragNetto.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.form.controls.anteilAndereKosten.updateValueAndValidity({ emitEvent: false }));
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
          this.notification.success('Grundstückunterhalt wurde erfolgreich gespeichert.');
          this.router.navigate(['/grundstueckunterhalt']);
        },
        error: () => this.notification.error('Fehler beim Speichern des Grundstückunterhalts.')
      });
  }

  delete(): void {
    const id = this.grundstueckunterhaltId();
    if (!id || this.isSaving()) return;

    this.dialog
      .open<ConfirmDialogComponent, ConfirmDialogData, boolean>(ConfirmDialogComponent, {
        data: {
          title: 'Grundstückunterhalt löschen',
          message: 'Soll dieser Grundstückunterhalt wirklich gelöscht werden?',
          confirmLabel: 'Löschen'
        }
      })
      .afterClosed()
      .pipe(
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.isSaving.set(true);
          return this.grundstueckunterhaltController
            .deleteGrundstueckunterhaltById(id)
            .pipe(finalize(() => this.isSaving.set(false)));
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.notification.success('Grundstückunterhalt wurde gelöscht.');
          this.router.navigate(['/grundstueckunterhalt']);
        },
        error: () => this.notification.error('Fehler beim Löschen des Grundstückunterhalts.')
      });
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): GrundstueckunterhaltForm {
    return this.fb.group({
      jahr: this.fb.control<number | null>(this.defaultYear(), [Validators.required, Validators.min(MIN_YEAR)]),
      rgDatum: this.fb.control<Date | null>(null, [Validators.required, notInFutureValidator()]),
      ausfuehrendeFirma: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      arbeitsArt: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      betragNetto: this.fb.control<number | null>(null, [Validators.required, Validators.min(0.01)]),
      anteilAndereKosten: this.fb.control<number | null>(0, [
        Validators.required, Validators.min(0), anteilNichtGroesserAlsNetto
      ])
    });
  }

  private resetFormForCreate(): void {
    this.form.reset({
      jahr: this.defaultYear(),
      rgDatum: null,
      ausfuehrendeFirma: '',
      arbeitsArt: '',
      betragNetto: null,
      anteilAndereKosten: 0
    });
  }

  private resetFormFromDto(eintrag: GrundstueckunterhaltDto): void {
    this.form.reset({
      jahr: eintrag.jahr ?? this.defaultYear(),
      rgDatum: parseBackendDate(eintrag.rgDatum),
      ausfuehrendeFirma: eintrag.ausfuehrendeFirma ?? '',
      arbeitsArt: eintrag.arbeitsArt ?? '',
      betragNetto: eintrag.betragNetto ?? null,
      anteilAndereKosten: eintrag.anteilAndereKosten ?? 0
    });
  }

  private calculateAnteilUnterhalt(): number {
    const { betragNetto, anteilAndereKosten } = this.form.getRawValue();
    // Auf Rappen runden, um Fliesskomma-Artefakte (z.B. 0.30000000000000004) zu vermeiden
    return Math.round((Number(betragNetto ?? 0) - Number(anteilAndereKosten ?? 0)) * 100) / 100;
  }

  private buildDtoFromForm(): GrundstueckunterhaltDto {
    const raw = this.form.getRawValue();

    return {
      id: this.grundstueckunterhaltId() ?? undefined,
      // required-Validatoren garantieren Jahr, Rechnungsdatum und Beträge
      jahr: raw.jahr!,
      rgDatum: toBackendDate(raw.rgDatum)!,
      ausfuehrendeFirma: raw.ausfuehrendeFirma.trim(),
      arbeitsArt: raw.arbeitsArt.trim(),
      betragNetto: Number(raw.betragNetto),
      anteilAndereKosten: Number(raw.anteilAndereKosten),
      anteilUnterhalt: this.calculateAnteilUnterhalt()
    };
  }

  private saveRequest$(dto: GrundstueckunterhaltDto): Observable<unknown> {
    const id = this.grundstueckunterhaltId();
    return id
      ? this.grundstueckunterhaltController.updateGrundstueckunterhalt(id, dto)
      : this.grundstueckunterhaltController.createGrundstueckunterhalt(dto);
  }

  private defaultYear(): number {
    return new Date().getFullYear();
  }
}
