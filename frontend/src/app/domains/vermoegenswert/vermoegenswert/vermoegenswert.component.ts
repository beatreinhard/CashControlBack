import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {VermoegenswertSearchComponent} from '../vermoegenswert-search/vermoegenswert-search.component';
import {PageHeaderComponent} from '../../../shared/page-header/page-header.component';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-vermoegenswert',
  imports: [
    VermoegenswertSearchComponent,
    MatAnchor,
    RouterLink,
    PageHeaderComponent,
    MatIcon
  ],
  templateUrl: './vermoegenswert.component.html',
  styleUrl: './vermoegenswert.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'cc-page' }
})
export class VermoegenswertComponent {

}
