import {Component, inject} from '@angular/core';
import {toSignal} from '@angular/core/rxjs-interop';
import {RouterLink, RouterOutlet} from '@angular/router';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatListItem, MatNavList} from '@angular/material/list';
import {catchError, map, of} from 'rxjs';
import {VersionControllerApi} from './generated';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatSidenavModule, MatButtonModule, MatToolbarModule, MatIconModule, MatListItem, MatNavList, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  // Die Version ist nur eine Zusatzinfo: Schlaegt der Aufruf fehl, bleibt die Anzeige leer.
  protected readonly version = toSignal(
    inject(VersionControllerApi).getVersion().pipe(
      map(dto => dto.version),
      catchError(() => of(undefined))
    )
  );
}
