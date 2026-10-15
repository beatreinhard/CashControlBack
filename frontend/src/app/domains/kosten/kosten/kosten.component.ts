import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {KostenSearchComponent} from '../kosten-search/kosten-search.component';

@Component({
  selector: 'app-kosten',
  imports: [
    KostenSearchComponent,
    MatAnchor,
    RouterLink
  ],
  templateUrl: './kosten.component.html',
  styleUrl: './kosten.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class KostenComponent {

}
