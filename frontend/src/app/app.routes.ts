import {Routes} from '@angular/router';
import {DashboardComponent} from './domains/dashboard/dashboard/dashboard.component';
import {AusgabeComponent} from './domains/ausgabe/ausgabe/ausgabe.component';
import {AusgabeEditComponent} from './domains/ausgabe/ausgabe-edit/ausgabe-edit.component';
import {VergabungComponent} from './domains/vergabung/vergabung/vergabung/vergabung.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'ausgabe', component: AusgabeComponent },
  {
    path: 'ausgabe/edit/:ausgabeId',
    component: AusgabeEditComponent
  },
  { path: 'ausgabe/create', component: AusgabeEditComponent },
  { path: 'vergabung', component: VergabungComponent }
];
