import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {RouterLink} from '@angular/router';
import {PageHeaderComponent} from '../../../shared/page-header/page-header.component';
import {AusgabeSearchComponent} from '../ausgabe-search/ausgabe-search.component';

@Component({
  selector: 'app-ausgabe',
  imports: [
    AusgabeSearchComponent,
    MatAnchor,
    MatIcon,
    PageHeaderComponent,
    RouterLink
  ],
  templateUrl: './ausgabe.component.html',
  styleUrl: './ausgabe.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'cc-page' }
})
export class AusgabeComponent {

}
