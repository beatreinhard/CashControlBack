import {Component, inject, OnInit, signal} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {VergabungListComponent} from '../../vergabung-list/vergabung-list.component';
import {VergabungControllerApi, VergabungDto} from '../../../../generated';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-vergabung-search',
  imports: [
    MatProgressSpinner,
    VergabungListComponent,
    MatCard,
    MatCardTitle,
    MatCardContent,
    MatLabel,
    MatCardActions,
    MatInput,
    MatButton,
    ReactiveFormsModule,
    MatFormField,
    MatError
  ],
  templateUrl: './vergabung-search.component.html',
  styleUrl: './vergabung-search.component.css'
})
export class VergabungSearchComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly loading = signal(true);
  readonly error = signal<string | undefined>("");
  readonly result = signal<VergabungDto[]>([]);

  protected vergabungController = inject(VergabungControllerApi);

  // typisiertes FormGroup
  protected readonly form: FormGroup<{
    jahr: FormControl<number | null>;
  }>;

  constructor() {
    this.form = this.fb.group({
      jahr: this.fb.control<number | null>(null, {
        validators: [Validators.min(2000)]
      }),
    });
  }

  ngOnInit(): void {
    this.fetchData();
  }

  filtern(): void {
    const form = this.form.getRawValue();
    if (form.jahr === null) {
      this.fetchData();
    } else {
      this.fetchData(form.jahr);
    }
  }

  private fetchData(jahr?: number): void {
    this.vergabungController.getVergabungen(jahr).subscribe({
      next: (res) => {
        this.result.set((res as VergabungDto[]) ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('API error:', err);
        this.error.set('Konnte Vergabungen nicht laden.');
        this.loading.set(false);
      },
    });
  }
}
