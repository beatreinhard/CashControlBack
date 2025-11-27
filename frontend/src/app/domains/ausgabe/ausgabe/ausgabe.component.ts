import {Component} from '@angular/core';
import {AusgabeSearchComponent} from '../ausgabe-search/ausgabe-search.component';
import {MatButton} from '@angular/material/button';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-ausgabe',
  imports: [
    AusgabeSearchComponent,
    MatButton,
    RouterLink,
  ],
  templateUrl: './ausgabe.component.html',
  styleUrl: './ausgabe.component.css'
})
export class AusgabeComponent {

}
