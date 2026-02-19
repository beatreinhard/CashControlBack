import {Component, inject, OnInit, signal} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {SchuldControllerApi, SchuldDto} from '../../../generated';
import {SchuldListComponent} from '../schuld-list/schuld-list.component';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-schuld-search',
  imports: [
    MatProgressSpinner,
    SchuldListComponent,
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
  templateUrl: './schuld-search.component.html',
  styleUrl: './schuld-search.component.css'
})
export class SchuldSearchComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly loading = signal(true);
  readonly error = signal<string | undefined>("");
  readonly result = signal<SchuldDto[]>([]);

  protected schuldController = inject(SchuldControllerApi);

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
    this.schuldController.getSchulden(jahr).subscribe({
      next: (res) => {
        this.result.set((res as SchuldDto[]) ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('API error:', err);
        this.error.set('Konnte Schulden nicht laden.');
        this.loading.set(false);
      },
    });
  }
}
