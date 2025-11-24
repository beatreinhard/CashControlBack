import {Routes} from '@angular/router';
import {DashboardComponent} from './domains/dashboard/dashboard/dashboard.component';
import {VergabungListComponent} from './domains/vergabung/vergabung-list/vergabung-list.component';
import {AusgabeComponent} from './domains/ausgabe/ausgabe/ausgabe.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'ausgabe', component: AusgabeComponent },
  { path: 'vergabung', component: VergabungListComponent }
];
