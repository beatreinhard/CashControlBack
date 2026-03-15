import {Component, computed, effect, inject, input, signal} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {KostenArtDto, KostenControllerApi, KostenDto, PersonControllerApi} from '../../../generated';
import {Router, RouterLink} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {toSignal} from '@angular/core/rxjs-interop';
import {catchError, map, Observable, of} from 'rxjs';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from '@angular/material/input';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';

type KostenForm = FormGroup<{
  jahr: FormControl<number | null>;
  art: FormControl<KostenArtDto | null>;
  zahlender: FormControl<string | null>;
  empfaenger: FormControl<string | null>;
  bemerkung: FormControl<string | null>;
  betrag: FormControl<number | null>;
  personSelect: FormControl<string | null>;
}>;

interface Zahlender {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-kosten-edit',
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
  templateUrl: './kosten-edit.component.html',
  styleUrl: './kosten-edit.component.css'
})
export class KostenEditComponent {
  /** undefined/null => Create, string => Edit */
  readonly kostenId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly kostenController = inject(KostenControllerApi);
  private readonly personController:PersonControllerApi = inject(PersonControllerApi);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly kostenArten = Object.values(KostenArtDto);

  protected readonly isLoading = signal(false);
  protected readonly loadError = signal<string | null>(null);
  protected readonly hasAusgabe = signal(true);

  protected readonly isEditMode = computed(() => !!this.kostenId());
  protected readonly title = computed(() => (this.isEditMode() ? 'Kosten bearbeiten' : 'Neue Kosten erfassen'));

  protected readonly form: KostenForm = this.buildForm();

  protected readonly zahlender = toSignal(
    this.personController.getAllPerson().pipe(
      map((persons) =>
        persons.map((p) => ({
          value: String(p.id),
          viewValue: `${p.name} ${p.vorname}`.trim(),
        }))
      ),
      catchError(() => {
        this.loadError.set('Personen konnten nicht geladen werden.');
        return of([] as Zahlender[]);
      })
    ),
    { initialValue: [] as Zahlender[] }
  );


  constructor() {
    effect(() => this.handleIdChange(this.kostenId()));
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
        console.log('Kosten gespeichert:', result);
        this.showSuccess('Kosten wurde erfolgreich gespeichert.');
        // optional: Navigation / Reset / Emit
      },
      error: (err) => {
        console.error('Fehler beim Speichern der Kosten:', err);
        this.showError('Fehler beim Speichern der Kosten.');
      }
    });
  }

  delete(): void {
    if (this.kostenId() != null) {
      console.log('Kosten gelöscht');
      this.kostenController.deleteKostenById(this.kostenId()!).subscribe();
      this.router.navigate(['/kosten']);
    }
  }

  // -----------------------
  // private Methoden
  // -----------------------

  private buildForm(): KostenForm {
    return this.fb.group({
      jahr: this.fb.control<number | null>(null, { validators: [Validators.required] }),
      art: this.fb.control<KostenArtDto | null>(null, { validators: [Validators.required] }),
      zahlender: this.fb.control<string | null>(null),
      empfaenger: this.fb.control<string | null>(null, { validators: [Validators.required] }),
      bemerkung: this.fb.control<string | null>(null),
      betrag: this.fb.control<number | null>(null, { validators: [Validators.required, Validators.min(0.01)] }),
      personSelect: this.fb.control<string | null>(null),
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
      jahr: null,
      art: null,
      zahlender: null,
      empfaenger: null,
      bemerkung: null,
      betrag: null,
      personSelect: null
    });
  }

  private loadForEdit(id: string): void {
    this.setLoading(true);

    this.kostenController.getKostenById(id).subscribe({
      next: (kosten) => {
        this.patchFormFromDto(kosten);
        this.setLoading(false);
        if (kosten.ausgabeId != undefined) {
          this.hasAusgabe.set(true);
        } else {
          this.hasAusgabe.set(false);
        }
      },
      error: (err) => {
        console.error('Fehler beim Laden der Kosten', err);
        this.loadError.set('Die Kosten konnte nicht geladen werden.');
        this.setLoading(false);
      }
    });
  }

  private patchFormFromDto(kosten: KostenDto): void {
    this.form.patchValue({
      jahr: kosten.jahr,
      art: kosten.art,
      zahlender: kosten.zahlender ?? null,
      empfaenger: kosten.empfaenger ?? null,
      bemerkung: kosten.bemerkung ?? null,
      betrag: kosten.betrag ?? null
    });
  }

  private buildDtoFromForm(): KostenDto {
    const raw = this.form.getRawValue();

    return {
      jahr: raw.jahr!,
      art: raw.art!,
      empfaenger: raw.empfaenger ?? '',
      zahlender: raw.zahlender ?? '',
      bemerkung: raw.bemerkung ?? '',
      betrag: Number(raw.betrag)
    };
  }

  private saveRequest$(dto: KostenDto): Observable<unknown> {
    const id = this.kostenId();
    return id ? this.kostenController.updateKosten(id, dto) : this.kostenController.createKosten(dto);
  }

  private setLoading(isLoading: boolean): void {
    this.isLoading.set(isLoading);
    if (isLoading) this.loadError.set(null);
  }

  private markFormTouched(): void {
    this.form.markAllAsTouched();
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
