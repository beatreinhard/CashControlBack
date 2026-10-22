import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {BerufSearchComponent} from '../beruf-search/beruf-search.component';

@Component({
  selector: 'app-beruf',
  imports: [
    BerufSearchComponent,
    MatAnchor,
    RouterLink
  ],
  templateUrl: './beruf.component.html',
  styleUrl: './beruf.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BerufComponent {

}
