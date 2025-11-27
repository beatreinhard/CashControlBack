import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AusgabeControllerApi, AusgabeDto, AusgabeKategorieDto} from '../../../generated';
import {MatCardActions, MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {MatOption, MatSelect} from '@angular/material/select';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-ausgabe-edit',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule, MatInputModule, MatDatepickerModule, MatIconModule, MatCardModule, MatSelect, MatOption, MatCardActions, MatButtonModule
  ],
  templateUrl: './ausgabe-edit.component.html',
  styleUrl: './ausgabe-edit.component.css'
})
export class AusgabeEditComponent {
  protected ausgabeController = inject(AusgabeControllerApi);
  protected form: FormGroup;

  // Das Enum in ein Array umwandeln für das <mat-select>
  protected kategorien = Object.values(AusgabeKategorieDto);

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      datum: [null, Validators.required],
      empfaenger: ['', Validators.required],
      kategorie: [null, Validators.required],
      text: [''],
      betrag: [null, [Validators.required, Validators.min(0.01)]],
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.value;

    const dto: AusgabeDto = {
      datum:
        raw.datum instanceof Date
          ? raw.datum.toISOString().substring(0, 10)
          : raw.datum,
      empfaenger: raw.empfaenger,
      kategorie: raw.kategorie,
      text: raw.text,
      betrag: Number(raw.betrag),
    };

    console.log('Speichere Ausgabe:', dto);

    this.ausgabeController.createAusgabe(dto).subscribe();

  }

  cancel(): void {
    // Navigation oder Dialog schließen
    console.log('Abgebrochen');
  }
}
