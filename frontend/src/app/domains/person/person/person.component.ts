import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from "@angular/material/button";
import {PersonSearchComponent} from '../person-search/person-search.component';
import {RouterLink} from '@angular/router';
import {PageHeaderComponent} from '../../../shared/page-header/page-header.component';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-person',
  imports: [
    MatAnchor,
    PersonSearchComponent,
    RouterLink,
    PageHeaderComponent,
    MatIcon
  ],
  templateUrl: './person.component.html',
  styleUrl: './person.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'cc-page' }
})
export class PersonComponent {

}
