import {Routes} from '@angular/router';

export const VERMOEGENSWERT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./vermoegenswert/vermoegenswert.component').then(m => m.VermoegenswertComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('./vermoegenswert-edit/vermoegenswert-edit.component').then(m => m.VermoegenswertEditComponent)
  },
  {
    path: 'edit/:vermoegenswertId',
    loadComponent: () => import('./vermoegenswert-edit/vermoegenswert-edit.component').then(m => m.VermoegenswertEditComponent)
  }
];
