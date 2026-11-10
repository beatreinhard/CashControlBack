import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {ErbschaftSearchComponent} from '../erbschaft-search/erbschaft-search.component';
import {PageHeaderComponent} from '../../../shared/page-header/page-header.component';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-erbschaft',
  imports: [
    ErbschaftSearchComponent,
    MatAnchor,
    RouterLink,
    PageHeaderComponent,
    MatIcon
  ],
  templateUrl: './erbschaft.component.html',
  styleUrl: './erbschaft.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'cc-page' }
})
export class ErbschaftComponent {

}
