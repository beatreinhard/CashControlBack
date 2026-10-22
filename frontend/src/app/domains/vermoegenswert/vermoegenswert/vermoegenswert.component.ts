import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {VermoegenswertSearchComponent} from '../vermoegenswert-search/vermoegenswert-search.component';

@Component({
  selector: 'app-vermoegenswert',
  imports: [
    VermoegenswertSearchComponent,
    MatAnchor,
    RouterLink
  ],
  templateUrl: './vermoegenswert.component.html',
  styleUrl: './vermoegenswert.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VermoegenswertComponent {

}
