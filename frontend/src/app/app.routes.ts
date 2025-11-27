import {Routes} from '@angular/router';
import {DashboardComponent} from './domains/dashboard/dashboard/dashboard.component';
import {VergabungListComponent} from './domains/vergabung/vergabung-list/vergabung-list.component';
import {AusgabeComponent} from './domains/ausgabe/ausgabe/ausgabe.component';
import {AusgabeEditComponent} from './domains/ausgabe/ausgabe-edit/ausgabe-edit.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'ausgabe', component: AusgabeComponent },
  { path: 'ausgabe/create', component: AusgabeEditComponent },
  { path: 'vergabung', component: VergabungListComponent }
];
