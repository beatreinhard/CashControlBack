import {Component} from '@angular/core';
import {KostenSearchComponent} from '../kosten-search/kosten-search.component';

@Component({
  selector: 'app-kosten',
  imports: [
    KostenSearchComponent
  ],
  templateUrl: './kosten.component.html',
  styleUrl: './kosten.component.css'
})
export class KostenComponent {

}
