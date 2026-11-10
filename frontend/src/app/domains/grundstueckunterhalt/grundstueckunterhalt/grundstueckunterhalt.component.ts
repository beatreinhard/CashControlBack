import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {
  GrundstueckunterhaltSearchComponent
} from '../grundstueckunterhalt-search/grundstueckunterhalt-search.component';
import {PageHeaderComponent} from '../../../shared/page-header/page-header.component';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-grundstueckunterhalt',
  imports: [
    GrundstueckunterhaltSearchComponent,
    MatAnchor,
    RouterLink,
    PageHeaderComponent,
    MatIcon
  ],
  templateUrl: './grundstueckunterhalt.component.html',
  styleUrl: './grundstueckunterhalt.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'cc-page' }
})
export class GrundstueckunterhaltComponent {

}
