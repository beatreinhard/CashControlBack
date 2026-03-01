import {Component} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {PersonSearchComponent} from '../person-search/person-search.component';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-person',
  imports: [
    MatButton,
    PersonSearchComponent,
    RouterLink
  ],
  templateUrl: './person.component.html',
  styleUrl: './person.component.css'
})
export class PersonComponent {

}
