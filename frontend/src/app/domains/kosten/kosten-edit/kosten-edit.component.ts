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
import {KostenArtDto, KostenControllerApi, KostenDto} from '../../../generated';
import {ConfirmDialogComponent, ConfirmDialogData} from '../../../shared/confirm-dialog/confirm-dialog.component';
import {NotificationService} from '../../../shared/notification.service';
import {injectPersonOptions} from '../../../shared/person-options';
import {MIN_YEAR} from '../../../shared/year-filter/year-filter.component';

const MAX_TEXT_LENGTH = 255;

type KostenForm = FormGroup<{
  jahr: FormControl<number | null>;
  art: FormControl<KostenArtDto | null>;
  zahlender: FormControl<string | null>;
  empfaenger: FormControl<string>;
  bemerkung: FormControl<string>;
  betrag: FormControl<number | null>;
}>;

@Component({
  selector: 'app-kosten-edit',
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
  templateUrl: './kosten-edit.component.html',
  styleUrl: './kosten-edit.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class KostenEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly kostenId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly kostenController = inject(KostenControllerApi);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroyRef = inject(DestroyRef);

  /** Lädt die Kosten im Edit-Modus; bei kostenId == null bleibt die Resource idle. */
  private readonly kostenResource = rxResource({
    request: () => this.kostenId() ?? undefined,
    loader: ({ request: id }) => this.kostenController.getKostenById(id)
  });

  private readonly personOptions = injectPersonOptions();

  protected readonly kostenArten = Object.values(KostenArtDto);
  protected readonly zahlender = this.personOptions.options;
  protected readonly zahlenderError = this.personOptions.error;

  protected readonly isLoading = this.kostenResource.isLoading;
  protected readonly isSaving = signal(false);
  protected readonly loadError = computed(() =>
    this.kostenResource.error() ? 'Die Kosten konnten nicht geladen werden.' : null
  );

  /** Kosten, die aus einer Ausgabe entstanden sind, werden über die Ausgabe gepflegt. */
  protected readonly hasAusgabe = computed(() => !!this.kostenResource.value()?.ausgabeId);
  /** Im Edit-Modus erst nach dem Laden bearbeitbar. */
  protected readonly isReadOnly = computed(() =>
    this.hasAusgabe() || (this.isEditMode() && !this.kostenResource.value())
  );

  protected readonly isEditMode = computed(() => !!this.kostenId());
  protected readonly title = computed(() => (this.isEditMode() ? 'Kosten bearbeiten' : 'Neue Kosten erfassen'));

  protected readonly minYear = MIN_YEAR;
  protected readonly maxTextLength = MAX_TEXT_LENGTH;
  protected readonly form: KostenForm = this.buildForm();

  constructor() {
    // Bridge: Resource-Signal -> imperatives Reactive-Forms-API
    effect(() => {
      if (!this.kostenId()) {
        this.resetFormForCreate();
        return;
      }
      const kosten = this.kostenResource.value();
      if (kosten) this.resetFormFromDto(kosten);
    });

    effect(() => {
      if (this.hasAusgabe()) {
        this.form.disable();
      } else {
        this.form.enable();
      }
    });
  }

  save(): void {
    if (this.isSaving() || this.isReadOnly()) return;
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
          this.notification.success('Kosten wurden erfolgreich gespeichert.');
          this.router.navigate(['/kosten']);
        },
        error: () => this.notification.error('Fehler beim Speichern der Kosten.')
      });
  }

  delete(): void {
    const id = this.kostenId();
    if (!id || this.isSaving() || this.isReadOnly()) return;

    this.dialog
      .open<ConfirmDialogComponent, ConfirmDialogData, boolean>(ConfirmDialogComponent, {
        data: {
          title: 'Kosten löschen',
          message: 'Sollen diese Kosten wirklich gelöscht werden?',
          confirmLabel: 'Löschen'
        }
      })
      .afterClosed()
      .pipe(
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.isSaving.set(true);
          return this.kostenController.deleteKostenById(id).pipe(finalize(() => this.isSaving.set(false)));
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.notification.success('Kosten wurden gelöscht.');
          this.router.navigate(['/kosten']);
        },
        error: () => this.notification.error('Fehler beim Löschen der Kosten.')
      });
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): KostenForm {
    return this.fb.group({
      jahr: this.fb.control<number | null>(new Date().getFullYear(), [Validators.required, Validators.min(MIN_YEAR)]),
      art: this.fb.control<KostenArtDto | null>(null, [Validators.required]),
      zahlender: this.fb.control<string | null>(null),
      empfaenger: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      bemerkung: this.fb.nonNullable.control('', [Validators.maxLength(MAX_TEXT_LENGTH)]),
      betrag: this.fb.control<number | null>(null, [Validators.required, Validators.min(0.01)])
    });
  }

  private resetFormForCreate(): void {
    this.form.reset({
      jahr: new Date().getFullYear(),
      art: null,
      zahlender: null,
      empfaenger: '',
      bemerkung: '',
      betrag: null
    });
  }

  private resetFormFromDto(kosten: KostenDto): void {
    this.form.reset({
      jahr: kosten.jahr,
      art: kosten.art,
      zahlender: kosten.zahlender || null,
      empfaenger: kosten.empfaenger ?? '',
      bemerkung: kosten.bemerkung ?? '',
      betrag: kosten.betrag ?? null
    });
  }

  private buildDtoFromForm(): KostenDto {
    const raw = this.form.getRawValue();

    return {
      id: this.kostenId() ?? undefined,
      // required-Validatoren garantieren Jahr, Art und Betrag
      jahr: raw.jahr!,
      art: raw.art!,
      empfaenger: raw.empfaenger.trim(),
      // Pflichtfeld im API-Vertrag; leer = kein Zahlender
      zahlender: raw.zahlender ?? '',
      bemerkung: raw.bemerkung.trim() || undefined,
      betrag: Number(raw.betrag)
    };
  }

  private saveRequest$(dto: KostenDto): Observable<unknown> {
    const id = this.kostenId();
    return id ? this.kostenController.updateKosten(id, dto) : this.kostenController.createKosten(dto);
  }
}
