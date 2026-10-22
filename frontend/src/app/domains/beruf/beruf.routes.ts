import {Routes} from '@angular/router';

export const BERUF_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./beruf/beruf.component').then(m => m.BerufComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('./beruf-edit/beruf-edit.component').then(m => m.BerufEditComponent)
  },
  {
    path: 'edit/:berufId',
    loadComponent: () => import('./beruf-edit/beruf-edit.component').then(m => m.BerufEditComponent)
  }
];
