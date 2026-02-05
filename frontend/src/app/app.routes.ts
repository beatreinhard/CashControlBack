import {Routes} from '@angular/router';
import {DashboardComponent} from './domains/dashboard/dashboard/dashboard.component';
import {AusgabeComponent} from './domains/ausgabe/ausgabe/ausgabe.component';
import {AusgabeEditComponent} from './domains/ausgabe/ausgabe-edit/ausgabe-edit.component';
import {VergabungComponent} from './domains/vergabung/vergabung/vergabung/vergabung.component';
import {SchuldComponent} from './domains/schuld/schuld/schuld.component';
import {SchuldEditComponent} from './domains/schuld/schuld-edit/schuld-edit.component';
import {KostenComponent} from './domains/kosten/kosten/kosten.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'ausgabe', component: AusgabeComponent },
  {
    path: 'ausgabe/edit/:ausgabeId',
    component: AusgabeEditComponent
  },
  { path: 'ausgabe/create', component: AusgabeEditComponent },
  { path: 'vergabung', component: VergabungComponent },
  { path: 'schuld', component: SchuldComponent },
  { path: 'kosten', component: KostenComponent },
  {
    path: 'schuld/edit/:schuldId',
    component: SchuldEditComponent,
  },
  { path: 'schuld/create', component: SchuldEditComponent }
];
