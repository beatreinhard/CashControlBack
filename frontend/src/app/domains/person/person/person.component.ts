import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatAnchor} from "@angular/material/button";
import {PersonSearchComponent} from '../person-search/person-search.component';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-person',
  imports: [
    MatAnchor,
    PersonSearchComponent,
    RouterLink
  ],
  templateUrl: './person.component.html',
  styleUrl: './person.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PersonComponent {

}
