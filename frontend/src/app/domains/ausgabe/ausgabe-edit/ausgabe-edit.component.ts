import {Component, computed, effect, inject, input, signal} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AusgabeControllerApi, AusgabeDto, AusgabeKategorieDto} from '../../../generated';
import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {format, parseISO} from 'date-fns';

@Component({
  selector: 'app-ausgabe-edit',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule, MatInputModule, MatDatepickerModule, MatIconModule, MatCardModule, MatSelectModule, MatButtonModule
  ],
  templateUrl: './ausgabe-edit.component.html',
  styleUrl: './ausgabe-edit.component.css'
})
export class AusgabeEditComponent {

  // -> brauchts nicht mit "provideRouter(routes, withComponentInputBinding())," im app.config.ts (das Mapping von der Route ins ausgabeId wird von withComponentInputBindng übernommen

  // protected readonly route = inject(ActivatedRoute);
  // protected readonly paramMapSig = toSignal(this.route.paramMap);
  //
  // // Abgeleitete Signals (computed)
  // protected readonly ausgabeId = computed(
  //   () => this.paramMapSig()?.get('id') ?? ''
  // );


  /**
   * Optionales Input:
   * - undefined / null  => Create-Modus
   * - string (id)       => Edit-Modus (Daten werden geladen)
   */
  readonly ausgabeId = input<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly ausgabeController = inject(AusgabeControllerApi);

  // Enum-Werte für <mat-select>
  protected readonly kategorien = Object.values(AusgabeKategorieDto);

  // Zustände (optional, aber hilfreich für UX)
  protected readonly isLoading = signal(false);
  protected readonly loadError = signal<string | null>(null);

  // Abgeleitete Infos
  protected readonly isEditMode = computed(() => !!this.ausgabeId());
  protected readonly title = computed(() =>
    this.isEditMode() ? 'Ausgabe bearbeiten' : 'Neue Ausgabe erfassen'
  );

  // typisiertes FormGroup
  protected readonly form: FormGroup<{
    datum: FormControl<Date | null>;
    empfaenger: FormControl<string>;
    kategorie: FormControl<AusgabeKategorieDto | null>;
    text: FormControl<string | null>;
    betrag: FormControl<number | null>;
    version: FormControl<number | null>;
  }>;

  constructor() {

    this.form = this.fb.group({
      datum: this.fb.control<Date | null>(null, {
        validators: [Validators.required]
      }),
      empfaenger: this.fb.control<string>('', {
        nonNullable: true,
        validators: [Validators.required]
      }),
      kategorie: this.fb.control<AusgabeKategorieDto | null>(null, {
        validators: [Validators.required]
      }),
      text: this.fb.control<string | null>(null),
      betrag: this.fb.control<number | null>(null, {
        validators: [Validators.required, Validators.min(0.01)]
      }),
      version: this.fb.control<number>(0, {
        validators: [Validators.required]
      }),
    });

    // Effect zum Laden der Daten, wenn eine ausgabeId gesetzt ist
    effect(() => {
      const id = this.ausgabeId();

      console.log(id);

      // Kein Edit-Modus => nichts laden
      if (!id) {
        // sicherstellen, dass das Formular im Create-Fall leer ist
        this.form.reset({
          datum: null,
          empfaenger: '',
          kategorie: null,
          text: null,
          betrag: null,
          version: 0
        });
        return;
      }

      this.isLoading.set(true);
      this.loadError.set(null);


      // TODO zuerst in ein dto abfüllen und dieses dann auf die form (so haben wir die rohdaten im dto)
      this.ausgabeController.getAusgabeById(id).subscribe({
        next: (ausgabe) => {
          // Mapping Backend-DTO -> Form
          this.form.patchValue({
            datum: ausgabe.datum ? parseISO(ausgabe.datum) : null,
            empfaenger: ausgabe.empfaenger ?? '',
            kategorie: ausgabe.kategorie ?? null,
            text: ausgabe.text ?? null,
            betrag: ausgabe.betrag ?? null
          });
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Fehler beim Laden der Ausgabe', err);
          this.loadError.set('Die Ausgabe konnte nicht geladen werden.');
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

    const dto: AusgabeDto = {
      datum:
        raw.datum instanceof Date
          ? toJsonDate(raw.datum)!
          : raw.datum ?? '',
      empfaenger: raw.empfaenger,
      kategorie: raw.kategorie!,
      text: raw.text ?? '',
      betrag: Number(raw.betrag),
      // falls dein Backend im Edit-Fall eine id im DTO erwartet, kannst du hier noch:
      id: this.ausgabeId() ?? undefined
    };

    console.log(raw.datum);
    console.log(dto.datum);
    const id = this.ausgabeId();

    // Create vs. Update unterscheiden
    const request$ = id
      ? this.ausgabeController.updateAusgabe(id, dto) // Methode nach deinem API-Client anpassen
      : this.ausgabeController.createAusgabe(dto);

    request$.subscribe({
      next: (result) => {
        console.log('Ausgabe gespeichert:', result);
        // TODO: Navigation, Snackbar, Event an Parent emitten etc.
      },
      error: (err) => {
        console.error('Fehler beim Speichern der Ausgabe:', err);
        // TODO: Fehlermeldung im UI anzeigen
      }
    });
  }

  cancel(): void {
    // TODO: Navigation oder Dialog schließen
    console.log('Abgebrochen');
  }
}


/**
 * Formats the given date to a string in the format yyyy-MM-dd.
 */
export function toJsonDate(date?: Date | string): string | undefined {
  if (!date) {
    return undefined;
  }
  let input = date;
  if (typeof input === 'string') {
    input = parseISO(input);
  }
  return format(input, 'yyyy-MM-dd');
}
