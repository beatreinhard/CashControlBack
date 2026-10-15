import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {SchuldSearchComponent} from '../schuld-search/schuld-search.component';

@Component({
  selector: 'app-schuld',
  imports: [
    SchuldSearchComponent,
    MatAnchor,
    RouterLink
  ],
  templateUrl: './schuld.component.html',
  styleUrl: './schuld.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SchuldComponent {

}
