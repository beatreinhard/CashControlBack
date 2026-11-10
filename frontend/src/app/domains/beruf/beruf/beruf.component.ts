import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {BerufSearchComponent} from '../beruf-search/beruf-search.component';
import {PageHeaderComponent} from '../../../shared/page-header/page-header.component';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-beruf',
  imports: [
    BerufSearchComponent,
    MatAnchor,
    RouterLink,
    PageHeaderComponent,
    MatIcon
  ],
  templateUrl: './beruf.component.html',
  styleUrl: './beruf.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'cc-page' }
})
export class BerufComponent {

}
