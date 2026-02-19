import {Component, inject, OnInit, signal} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {KostenControllerApi, KostenDto} from '../../../generated';
import {KostenListComponent} from '../kosten-list/kosten-list.component';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-kosten-search',
  imports: [
    MatProgressSpinner,
    KostenListComponent,
    MatButton,
    MatCard,
    MatCardActions,
    MatCardContent,
    MatCardTitle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './kosten-search.component.html',
  styleUrl: './kosten-search.component.css'
})
export class KostenSearchComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly loading = signal(true);
  readonly error = signal<string | undefined>("");
  readonly result = signal<KostenDto[]>([]);

  protected kostenController = inject(KostenControllerApi);

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
    this.kostenController.getKosten(jahr).subscribe({
      next: (res) => {
        this.result.set((res as KostenDto[]) ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('API error:', err);
        this.error.set('Konnte Kosten nicht laden.');
        this.loading.set(false);
      },
    });
  }
}
