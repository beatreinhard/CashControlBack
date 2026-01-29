import {Component, computed, effect, inject, input, signal} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from "@angular/material/card";
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from "@angular/material/input";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {SchuldArtDto, SchuldControllerApi, SchuldDto} from '../../../generated';

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
        ReactiveFormsModule
    ],
  templateUrl: './schuld-edit.component.html',
  styleUrl: './schuld-edit.component.css'
})
export class SchuldEditComponent {
  /**
   * Optionales Input:
   * - undefined / null  => Create-Modus
   * - string (id)       => Edit-Modus (Daten werden geladen)
   */
  readonly schuldId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly schuldController = inject(SchuldControllerApi);

  // Enum-Werte für <mat-select>
  protected readonly artValues = Object.values(SchuldArtDto);

  // Zustände (optional, aber hilfreich für UX)
  protected readonly isLoading = signal(false);
  protected readonly loadError = signal<string | null>(null);

  // Abgeleitete Infos
  protected readonly isEditMode = computed(() => !!this.schuldId());
  protected readonly title = computed(() =>
    this.isEditMode() ? 'Schuld bearbeiten' : 'Neue Schuld erfassen'
  );

  // typisiertes FormGroup
  protected readonly form: FormGroup<{
    jahr: FormControl<number>;
    glaeubiger: FormControl<string>;
    art: FormControl<SchuldArtDto | null>;
    betrag: FormControl<number | null>;
    zinsen: FormControl<number | null>;
  }>;

  constructor() {

    // todo: initialisieren mit aktuellen Systemjahr
    this.form = this.fb.group({
      jahr: this.fb.control<number>(2026, {
        nonNullable: true,
        validators: [Validators.required, Validators.min(2000)]
      }),
      glaeubiger: this.fb.control<string>('', {
        nonNullable: true,
        validators: [Validators.required]
      }),
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

    // Effect zum Laden der Daten, wenn eine ausgabeId gesetzt ist
    effect(() => {
      const id = this.schuldId();

      // Kein Edit-Modus => nichts laden
      if (!id) {
        // sicherstellen, dass das Formular im Create-Fall leer ist
        this.form.reset({
          jahr: 2026,
          glaeubiger: '',
          art: null,
          betrag: null,
          zinsen: null
        });
        return;
      }

      this.isLoading.set(true);
      this.loadError.set(null);


      // TODO zuerst in ein dto abfüllen und dieses dann auf die form (so haben wir die rohdaten im dto)
      this.schuldController.getSchuldById(id).subscribe({
        next: (schuld) => {
          // Mapping Backend-DTO -> Form
          this.form.patchValue({
            jahr: schuld.jahr,
            glaeubiger: schuld.glaeubiger ?? '',
            art: schuld.art ?? null,
            betrag: schuld.betrag ?? null,
            zinsen: schuld.zinsen ?? null
          });
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Fehler beim Laden der Schuld', err);
          this.loadError.set('Die Schuld konnte nicht geladen werden.');
          this.isLoading.set(false);
        }
      });
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();

    const dto: SchuldDto = {
      jahr: raw.jahr,
      glaeubiger: raw.glaeubiger,
      art: raw.art!,
      betrag: Number(raw.betrag),
      zinsen: raw.zinsen == null ? undefined : Number(raw.zinsen),
      // falls dein Backend im Edit-Fall eine id im DTO erwartet, kannst du hier noch:
      id: this.schuldId() ?? undefined
    };

    const id = this.schuldId();

    // Create vs. Update unterscheiden
    const request$ = id
      ? this.schuldController.updateSchuld(id, dto) // Methode nach deinem API-Client anpassen
      : this.schuldController.createSchuld(dto);

    request$.subscribe({
      next: (result) => {
        console.log('Schuld gespeichert:', result);
        // TODO: Navigation, Snackbar, Event an Parent emitten etc.
      },
      error: (err) => {
        console.error('Fehler beim Speichern der Schuld:', err);
        // TODO: Fehlermeldung im UI anzeigen
      }
    });
  }

  cancel(): void {
    // TODO: Navigation oder Dialog schließen
    console.log('Abgebrochen');
  }
}
