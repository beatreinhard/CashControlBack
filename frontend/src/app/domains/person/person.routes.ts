import {Routes} from '@angular/router';

export const PERSON_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./person/person.component').then(m => m.PersonComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('./person-edit/person-edit.component').then(m => m.PersonEditComponent)
  },
  {
    path: 'edit/:personId',
    loadComponent: () => import('./person-edit/person-edit.component').then(m => m.PersonEditComponent)
  }
];
