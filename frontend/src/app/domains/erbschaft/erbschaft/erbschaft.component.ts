import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {ErbschaftSearchComponent} from '../erbschaft-search/erbschaft-search.component';

@Component({
  selector: 'app-erbschaft',
  imports: [
    ErbschaftSearchComponent,
    MatAnchor,
    RouterLink
  ],
  templateUrl: './erbschaft.component.html',
  styleUrl: './erbschaft.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ErbschaftComponent {

}
