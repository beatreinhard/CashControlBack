import {ChangeDetectionStrategy, Component, input} from '@angular/core';
import {MatIcon} from '@angular/material/icon';
import {RouterLink} from '@angular/router';

export interface Crumb {
  label: string;
  /** Ohne Link => aktueller Ort (letztes Element). */
  link?: string;
}

/**
 * Seitenkopf mit Pfad-Navigation, Titel, Untertitel und Aktionen (per Content-Projection).
 * Ohne Titel wird nur die Pfad-Navigation angezeigt (z.B. auf Bearbeiten-Seiten).
 */
@Component({
  selector: 'app-page-header',
  imports: [MatIcon, RouterLink],
  template: `
    <header class="cc-header" [class.narrow]="narrow()">
      <div>
        @if (!hideCrumbs()) {
        <nav class="cc-crumb" aria-label="Pfad">
          <a routerLink="/">Übersicht</a>
          @for (c of crumbs(); track c.label) {
            <mat-icon>chevron_right</mat-icon>
            @if (c.link) {
              <a [routerLink]="c.link">{{ c.label }}</a>
            } @else {
              <span aria-current="page">{{ c.label }}</span>
            }
          }
        </nav>
        }
        @if (title()) {
          <h1 class="page-title">{{ title() }}</h1>
        }
        @if (subtitle()) {
          <p class="cc-sub">{{ subtitle() }}</p>
        }
      </div>
      <div class="cc-header-actions">
        <ng-content />
      </div>
    </header>
  `,
  styles: `:host { display: block; }`,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PageHeaderComponent {
  readonly title = input<string>();
  readonly subtitle = input<string>();
  readonly crumbs = input<Crumb[]>([]);
  /** Schmale Variante passend zu den Formularen. */
  readonly narrow = input(false);
  /** Pfad-Navigation ausblenden (z.B. auf der Übersicht selbst). */
  readonly hideCrumbs = input(false);
}
