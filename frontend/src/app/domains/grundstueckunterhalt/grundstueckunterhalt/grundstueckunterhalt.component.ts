import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {
  GrundstueckunterhaltSearchComponent
} from '../grundstueckunterhalt-search/grundstueckunterhalt-search.component';

@Component({
  selector: 'app-grundstueckunterhalt',
  imports: [
    GrundstueckunterhaltSearchComponent,
    MatAnchor,
    RouterLink
  ],
  templateUrl: './grundstueckunterhalt.component.html',
  styleUrl: './grundstueckunterhalt.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GrundstueckunterhaltComponent {

}
