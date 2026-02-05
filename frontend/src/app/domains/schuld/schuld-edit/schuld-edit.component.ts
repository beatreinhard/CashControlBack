import {Component, computed, effect, inject, input, signal} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from '@angular/material/input';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {Observable} from 'rxjs';
import {SchuldArtDto, SchuldControllerApi, SchuldDto} from '../../../generated';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Router, RouterLink} from '@angular/router';

type SchuldForm = FormGroup<{
  jahr: FormControl<number>;
  glaeubiger: FormControl<string>;
  art: FormControl<SchuldArtDto | null>;
  text: FormControl<string | null>;
  betrag: FormControl<number | null>;
  zinsen: FormControl<number | null>;
}>;

@Component({
  selector: 'app-schuld-edit',
  imports: [
    FormsModule,
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
    MatSelect,
    MatSuffix,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './schuld-edit.component.html',
  styleUrl: './schuld-edit.component.css'
})
export class SchuldEditComponent {
  /** undefined/null => Create, string => Edit */
  private inputSchuldId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly schuldController = inject(SchuldControllerApi);
  private readonly router = inject(Router);

  protected readonly artValues = Object.values(SchuldArtDto);

  protected readonly isLoading = signal(false);
  protected readonly loadError = signal<string | null>(null);

  protected readonly isEditMode = computed(() => !!this.inputSchuldId());

  protected readonly title = computed(() => (this.isEditMode() ? 'Schuld bearbeiten' : 'Neue Schuld erfassen'));

  private readonly snackBar = inject(MatSnackBar);
  protected readonly form: SchuldForm = this.buildForm();

  constructor() {
    effect(() => this.handleIdChange(this.inputSchuldId()));
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
        console.log('Schuld gespeichert');
        this.showSuccess('Schuld wurde erfolgreich gespeichert.');
        // optional: Navigation / Reset / Emit
      },
      error: (err) => {
        console.error('Fehler beim Speichern der Schuld:', err);
        this.showError('Fehler beim Speichern der Schuld.');
      }
    });
  }

  delete(): void {
  //  if (this.schuldId() ) {
      this.schuldController.deleteSchuldById(this.inputSchuldId()!).subscribe();
  //  }
    this.router.navigate(['/schuld']);
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): SchuldForm {
    return this.fb.group({
      jahr: this.fb.control<number>(this.defaultYear(), {
        nonNullable: true,
        validators: [Validators.required, Validators.min(2000)]
      }),
      glaeubiger: this.fb.control<string>('', {
        nonNullable: true,
        validators: [Validators.required]
      }),
      text: this.fb.control<string | null>(null),
      art: this.fb.control<SchuldArtDto | null>(null, {
        validators: [Validators.required]
      }),
      betrag: this.fb.control<number | null>(null, {
        validators: [Validators.required, Validators.min(0.01)]
      }),
      zinsen: this.fb.control<number | null>(null, {
        validators: [Validators.min(0)]
      })
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
      jahr: this.defaultYear(),
      glaeubiger: '',
      art: null,
      text: null,
      betrag: null,
      zinsen: null
    });
  }

  private loadForEdit(id: string): void {
    this.setLoading(true);

    this.schuldController.getSchuldById(id).subscribe({
      next: (schuld) => {
        this.patchFormFromDto(schuld);
        this.setLoading(false);
      },
      error: (err) => {
        console.error('Fehler beim Laden der Schuld', err);
        this.loadError.set('Die Schuld konnte nicht geladen werden.');
        this.setLoading(false);
      }
    });
  }

  private patchFormFromDto(schuld: SchuldDto): void {
    this.form.patchValue({
      jahr: schuld.jahr ?? this.defaultYear(),
      glaeubiger: schuld.glaeubiger ?? '',
      art: schuld.art ?? null,
      text: schuld.text ?? null,
      betrag: schuld.betrag ?? null,
      zinsen: schuld.zinsen ?? null
    });
  }

  private buildDtoFromForm(): SchuldDto {
    const raw = this.form.getRawValue();

    return {
      jahr: raw.jahr,
      glaeubiger: raw.glaeubiger,
      art: raw.art!,
      betrag: Number(raw.betrag),
      text: raw.text ?? undefined,
      // falls null/undefined => nicht mitschicken (oder auf null setzen, je nach Backend)
      zinsen: raw.zinsen == null ? undefined : Number(raw.zinsen),
      id: this.inputSchuldId() ?? undefined
    };
  }

  private saveRequest$(dto: SchuldDto): Observable<unknown> {
    const id = this.inputSchuldId();
    return id ? this.schuldController.updateSchuld(id, dto) : this.schuldController.createSchuld(dto);
  }

  private markFormTouched(): void {
    this.form.markAllAsTouched();
  }

  private setLoading(isLoading: boolean): void {
    this.isLoading.set(isLoading);
    if (isLoading) this.loadError.set(null);
  }

  private defaultYear(): number {
    return new Date().getFullYear();
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
