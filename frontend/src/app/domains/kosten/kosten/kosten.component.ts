import {Component} from '@angular/core';
import {KostenSearchComponent} from '../kosten-search/kosten-search.component';
import {MatButton} from '@angular/material/button';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-kosten',
  imports: [
    KostenSearchComponent,
    MatButton,
    RouterLink
  ],
  templateUrl: './kosten.component.html',
  styleUrl: './kosten.component.css'
})
export class KostenComponent {

}
