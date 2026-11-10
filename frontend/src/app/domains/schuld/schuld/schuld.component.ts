import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {SchuldSearchComponent} from '../schuld-search/schuld-search.component';
import {PageHeaderComponent} from '../../../shared/page-header/page-header.component';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-schuld',
  imports: [
    SchuldSearchComponent,
    MatAnchor,
    RouterLink,
    PageHeaderComponent,
    MatIcon
  ],
  templateUrl: './schuld.component.html',
  styleUrl: './schuld.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'cc-page' }
})
export class SchuldComponent {

}
