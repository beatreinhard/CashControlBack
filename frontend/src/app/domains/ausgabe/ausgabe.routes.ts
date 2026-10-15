import {Routes} from '@angular/router';

export const AUSGABE_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./ausgabe/ausgabe.component').then(m => m.AusgabeComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('./ausgabe-edit/ausgabe-edit.component').then(m => m.AusgabeEditComponent)
  },
  {
    path: 'edit/:ausgabeId',
    loadComponent: () => import('./ausgabe-edit/ausgabe-edit.component').then(m => m.AusgabeEditComponent)
  }
];
