import {Component} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {RouterLink} from '@angular/router';
import {SchuldSearchComponent} from '../schuld-search/schuld-search.component';

@Component({
  selector: 'app-schuld',
  imports: [
    MatButton,
    RouterLink,
    SchuldSearchComponent
  ],
  templateUrl: './schuld.component.html',
  styleUrl: './schuld.component.css'
})
export class SchuldComponent {

}
