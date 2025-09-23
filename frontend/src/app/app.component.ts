import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatListItem, MatNavList} from '@angular/material/list';
import {AusgabeControllerApi} from './generated';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatSidenavModule, MatButtonModule, MatToolbarModule, MatIconModule, MatListItem, MatNavList],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  data: any;

  constructor(private ausgabeController: AusgabeControllerApi ) {
  }


  ngOnInit() {
    // Beispiel-Call – Methode bitte durch deine ersetzen
    this.ausgabeController.getAllAusgabe().subscribe({
      next: (res) => {
        console.log('API response:', res);
        this.data = res;
      },
      error: (err) => console.error('API error:', err),
    });
  }
}
