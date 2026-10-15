import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {AusgabeSearchComponent} from '../ausgabe-search/ausgabe-search.component';

@Component({
  selector: 'app-ausgabe',
  imports: [
    AusgabeSearchComponent,
    MatAnchor,
    RouterLink
  ],
  templateUrl: './ausgabe.component.html',
  styleUrl: './ausgabe.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AusgabeComponent {

}
