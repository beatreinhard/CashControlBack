import {ChangeDetectionStrategy, Component, computed, DestroyRef, effect, inject, input, signal} from '@angular/core';
import {rxResource, takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatOption} from '@angular/material/core';
import {MatDialog} from '@angular/material/dialog';
import {MatError, MatFormField, MatHint, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressBar} from '@angular/material/progress-bar';
import {MatSelect} from '@angular/material/select';
import {Router, RouterLink} from '@angular/router';
import {filter, finalize, Observable, switchMap} from 'rxjs';
import {BerufControllerApi, BerufDto} from '../../../generated';
import {ConfirmDialogComponent, ConfirmDialogData} from '../../../shared/confirm-dialog/confirm-dialog.component';
import {NotificationService} from '../../../shared/notification.service';
import {injectPersonOptions} from '../../../shared/person-options';
import {MIN_YEAR} from '../../../shared/year-filter/year-filter.component';

const MAX_TEXT_LENGTH = 255;
const MAX_ARBEITSTAGE = 366;

type BerufForm = FormGroup<{
  jahr: FormControl<number | null>;
  arbeitnehmer: FormControl<string | null>;
  arbeitgeber: FormControl<string>;
  arbeitsort: FormControl<string>;
  beschaeftigungsgrad: FormControl<number | null>;
  arbeitstage: FormControl<number | null>;
  fahrtkilometerProTag: FormControl<number | null>;
  grundAutobenutzung: FormControl<string>;
  ansatzAuswaertigeVerpflegung: FormControl<number | null>;
  bemerkung: FormControl<string>;
}>;

@Component({
  selector: 'app-beruf-edit',
  imports: [
    MatAnchor,
    MatButton,
    MatCard,
    MatCardActions,
    MatCardContent,
    MatCardTitle,
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
  templateUrl: './beruf-edit.component.html',
  styleUrl: './beruf-edit.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BerufEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly berufId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly berufController = inject(BerufControllerApi);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroyRef = inject(DestroyRef);
  private readonly personOptions = injectPersonOptions();

  /** Lädt den Beruf im Edit-Modus; bei berufId == null bleibt die Resource idle. */
  private readonly berufResource = rxResource({
    request: () => this.berufId() ?? undefined,
    loader: ({ request: id }) => this.berufController.getBerufById(id)
  });

  protected readonly arbeitnehmer = this.personOptions.options;
  protected readonly arbeitnehmerError = this.personOptions.error;

  protected readonly isLoading = this.berufResource.isLoading;
  protected readonly isSaving = signal(false);
  protected readonly loadError = computed(() =>
    this.berufResource.error() ? 'Der Beruf konnte nicht geladen werden.' : null
  );

  protected readonly isEditMode = computed(() => !!this.berufId());
  protected readonly title = computed(() => (this.isEditMode() ? 'Beruf bearbeiten' : 'Neuen Beruf erfassen'));

  protected readonly minYear = MIN_YEAR;
  protected readonly maxTextLength = MAX_TEXT_LENGTH;
  protected readonly maxArbeitstage = MAX_ARBEITSTAGE;
  protected readonly form: BerufForm = this.buildForm();

  constructor() {
    // Bridge: Resource-Signal -> imperatives Reactive-Forms-API
    effect(() => {
      if (!this.berufId()) {
        this.resetFormForCreate();
        return;
      }
      const beruf = this.berufResource.value();
      if (beruf) this.resetFormFromDto(beruf);
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
          this.notification.success('Beruf wurde erfolgreich gespeichert.');
          this.router.navigate(['/beruf']);
        },
        error: () => this.notification.error('Fehler beim Speichern des Berufs.')
      });
  }

  delete(): void {
    const id = this.berufId();
    if (!id || this.isSaving()) return;

    this.dialog
      .open<ConfirmDialogComponent, ConfirmDialogData, boolean>(ConfirmDialogComponent, {
        data: {
          title: 'Beruf löschen',
          message: 'Soll dieser Beruf wirklich gelöscht werden?',
          confirmLabel: 'Löschen'
        }
      })
      .afterClosed()
      .pipe(
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.isSaving.set(true);
          return this.berufController.deleteBerufById(id).pipe(finalize(() => this.isSaving.set(false)));
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.notification.success('Beruf wurde gelöscht.');
          this.router.navigate(['/beruf']);
        },
        error: () => this.notification.error('Fehler beim Löschen des Berufs.')
      });
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): BerufForm {
    return this.fb.group({
      jahr: this.fb.control<number | null>(this.defaultYear(), [Validators.required, Validators.min(MIN_YEAR)]),
      arbeitnehmer: this.fb.control<string | null>(null, [Validators.required]),
      arbeitgeber: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      arbeitsort: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      beschaeftigungsgrad: this.fb.control<number | null>(100, [Validators.required, Validators.min(1), Validators.max(100)]),
      arbeitstage: this.fb.control<number | null>(null, [Validators.required, Validators.min(1), Validators.max(MAX_ARBEITSTAGE)]),
      fahrtkilometerProTag: this.fb.control<number | null>(null, [Validators.min(0)]),
      grundAutobenutzung: this.fb.nonNullable.control('', [Validators.maxLength(MAX_TEXT_LENGTH)]),
      ansatzAuswaertigeVerpflegung: this.fb.control<number | null>(null, [Validators.required, Validators.min(0)]),
      bemerkung: this.fb.nonNullable.control('', [Validators.maxLength(MAX_TEXT_LENGTH)])
    });
  }

  private resetFormForCreate(): void {
    this.form.reset({
      jahr: this.defaultYear(),
      arbeitnehmer: null,
      arbeitgeber: '',
      arbeitsort: '',
      beschaeftigungsgrad: 100,
      arbeitstage: null,
      fahrtkilometerProTag: null,
      grundAutobenutzung: '',
      ansatzAuswaertigeVerpflegung: null,
      bemerkung: ''
    });
  }

  private resetFormFromDto(beruf: BerufDto): void {
    this.form.reset({
      jahr: beruf.jahr ?? this.defaultYear(),
      arbeitnehmer: beruf.arbeitnehmer ?? null,
      arbeitgeber: beruf.arbeitgeber ?? '',
      arbeitsort: beruf.arbeitsort ?? '',
      beschaeftigungsgrad: beruf.beschaeftigungsgrad ?? null,
      arbeitstage: beruf.arbeitstage ?? null,
      fahrtkilometerProTag: beruf.fahrtkilometerProTag ?? null,
      grundAutobenutzung: beruf.grundAutobenutzung ?? '',
      ansatzAuswaertigeVerpflegung: beruf.ansatzAuswaertigeVerpflegung ?? null,
      bemerkung: beruf.bemerkung ?? ''
    });
  }

  private buildDtoFromForm(): BerufDto {
    const raw = this.form.getRawValue();

    return {
      id: this.berufId() ?? undefined,
      // required-Validatoren garantieren Jahr, Arbeitnehmer, Beschäftigungsgrad, Arbeitstage und Verpflegungsansatz
      jahr: raw.jahr!,
      arbeitnehmer: raw.arbeitnehmer!,
      arbeitgeber: raw.arbeitgeber.trim(),
      arbeitsort: raw.arbeitsort.trim(),
      beschaeftigungsgrad: Number(raw.beschaeftigungsgrad),
      arbeitstage: Number(raw.arbeitstage),
      fahrtkilometerProTag: raw.fahrtkilometerProTag == null ? undefined : Number(raw.fahrtkilometerProTag),
      grundAutobenutzung: raw.grundAutobenutzung.trim() || undefined,
      ansatzAuswaertigeVerpflegung: Number(raw.ansatzAuswaertigeVerpflegung),
      bemerkung: raw.bemerkung.trim() || undefined
    };
  }

  private saveRequest$(dto: BerufDto): Observable<unknown> {
    const id = this.berufId();
    return id ? this.berufController.updateBeruf(id, dto) : this.berufController.createBeruf(dto);
  }

  private defaultYear(): number {
    return new Date().getFullYear();
  }
}
