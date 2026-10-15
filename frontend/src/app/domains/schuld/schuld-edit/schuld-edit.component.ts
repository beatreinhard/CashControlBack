import {ChangeDetectionStrategy, Component, computed, DestroyRef, effect, inject, input, signal} from '@angular/core';
import {rxResource, takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatOption} from '@angular/material/core';
import {MatDialog} from '@angular/material/dialog';
import {MatError, MatFormField, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatProgressBar} from '@angular/material/progress-bar';
import {MatSelect} from '@angular/material/select';
import {Router, RouterLink} from '@angular/router';
import {filter, finalize, Observable, switchMap} from 'rxjs';
import {SchuldArtDto, SchuldControllerApi, SchuldDto} from '../../../generated';
import {ConfirmDialogComponent, ConfirmDialogData} from '../../../shared/confirm-dialog/confirm-dialog.component';
import {NotificationService} from '../../../shared/notification.service';
import {MIN_YEAR} from '../../../shared/year-filter/year-filter.component';

const MAX_TEXT_LENGTH = 255;

type SchuldForm = FormGroup<{
  jahr: FormControl<number | null>;
  art: FormControl<SchuldArtDto | null>;
  glaeubiger: FormControl<string>;
  text: FormControl<string>;
  betrag: FormControl<number | null>;
  zinsen: FormControl<number | null>;
}>;

@Component({
  selector: 'app-schuld-edit',
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
    MatOption,
    MatProgressBar,
    MatSelect,
    MatSuffix,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './schuld-edit.component.html',
  styleUrl: './schuld-edit.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SchuldEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly schuldId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly schuldController = inject(SchuldControllerApi);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroyRef = inject(DestroyRef);

  /** Lädt die Schuld im Edit-Modus; bei schuldId == null bleibt die Resource idle. */
  private readonly schuldResource = rxResource({
    request: () => this.schuldId() ?? undefined,
    loader: ({ request: id }) => this.schuldController.getSchuldById(id)
  });

  protected readonly artValues = Object.values(SchuldArtDto);

  protected readonly isLoading = this.schuldResource.isLoading;
  protected readonly isSaving = signal(false);
  protected readonly loadError = computed(() =>
    this.schuldResource.error() ? 'Die Schuld konnte nicht geladen werden.' : null
  );

  protected readonly isEditMode = computed(() => !!this.schuldId());
  protected readonly title = computed(() => (this.isEditMode() ? 'Schuld bearbeiten' : 'Neue Schuld erfassen'));

  protected readonly minYear = MIN_YEAR;
  protected readonly maxTextLength = MAX_TEXT_LENGTH;
  protected readonly form: SchuldForm = this.buildForm();

  constructor() {
    // Bridge: Resource-Signal -> imperatives Reactive-Forms-API
    effect(() => {
      if (!this.schuldId()) {
        this.resetFormForCreate();
        return;
      }
      const schuld = this.schuldResource.value();
      if (schuld) this.resetFormFromDto(schuld);
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
          this.notification.success('Schuld wurde erfolgreich gespeichert.');
          this.router.navigate(['/schuld']);
        },
        error: () => this.notification.error('Fehler beim Speichern der Schuld.')
      });
  }

  delete(): void {
    const id = this.schuldId();
    if (!id || this.isSaving()) return;

    this.dialog
      .open<ConfirmDialogComponent, ConfirmDialogData, boolean>(ConfirmDialogComponent, {
        data: {
          title: 'Schuld löschen',
          message: 'Soll diese Schuld wirklich gelöscht werden?',
          confirmLabel: 'Löschen'
        }
      })
      .afterClosed()
      .pipe(
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.isSaving.set(true);
          return this.schuldController.deleteSchuldById(id).pipe(finalize(() => this.isSaving.set(false)));
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.notification.success('Schuld wurde gelöscht.');
          this.router.navigate(['/schuld']);
        },
        error: () => this.notification.error('Fehler beim Löschen der Schuld.')
      });
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): SchuldForm {
    return this.fb.group({
      jahr: this.fb.control<number | null>(this.defaultYear(), [Validators.required, Validators.min(MIN_YEAR)]),
      art: this.fb.control<SchuldArtDto | null>(null, [Validators.required]),
      glaeubiger: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(MAX_TEXT_LENGTH)]),
      text: this.fb.nonNullable.control('', [Validators.maxLength(MAX_TEXT_LENGTH)]),
      betrag: this.fb.control<number | null>(null, [Validators.required, Validators.min(0.01)]),
      zinsen: this.fb.control<number | null>(null, [Validators.min(0)])
    });
  }

  private resetFormForCreate(): void {
    this.form.reset({
      jahr: this.defaultYear(),
      art: null,
      glaeubiger: '',
      text: '',
      betrag: null,
      zinsen: null
    });
  }

  private resetFormFromDto(schuld: SchuldDto): void {
    this.form.reset({
      jahr: schuld.jahr ?? this.defaultYear(),
      art: schuld.art ?? null,
      glaeubiger: schuld.glaeubiger ?? '',
      text: schuld.text ?? '',
      betrag: schuld.betrag ?? null,
      zinsen: schuld.zinsen ?? null
    });
  }

  private buildDtoFromForm(): SchuldDto {
    const raw = this.form.getRawValue();

    return {
      id: this.schuldId() ?? undefined,
      // required-Validatoren garantieren Jahr, Art und Betrag
      jahr: raw.jahr!,
      art: raw.art!,
      glaeubiger: raw.glaeubiger.trim(),
      text: raw.text.trim() || undefined,
      betrag: Number(raw.betrag),
      zinsen: raw.zinsen == null ? undefined : Number(raw.zinsen)
    };
  }

  private saveRequest$(dto: SchuldDto): Observable<unknown> {
    const id = this.schuldId();
    return id ? this.schuldController.updateSchuld(id, dto) : this.schuldController.createSchuld(dto);
  }

  private defaultYear(): number {
    return new Date().getFullYear();
  }
}
