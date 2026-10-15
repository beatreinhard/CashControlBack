import {Routes} from '@angular/router';

export const SCHULD_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./schuld/schuld.component').then(m => m.SchuldComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('./schuld-edit/schuld-edit.component').then(m => m.SchuldEditComponent)
  },
  {
    path: 'edit/:schuldId',
    loadComponent: () => import('./schuld-edit/schuld-edit.component').then(m => m.SchuldEditComponent)
  }
];
