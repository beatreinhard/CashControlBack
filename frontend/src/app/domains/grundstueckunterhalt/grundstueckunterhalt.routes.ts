import {Routes} from '@angular/router';

export const GRUNDSTUECKUNTERHALT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./grundstueckunterhalt/grundstueckunterhalt.component').then(m => m.GrundstueckunterhaltComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('./grundstueckunterhalt-edit/grundstueckunterhalt-edit.component').then(m => m.GrundstueckunterhaltEditComponent)
  },
  {
    path: 'edit/:grundstueckunterhaltId',
    loadComponent: () => import('./grundstueckunterhalt-edit/grundstueckunterhalt-edit.component').then(m => m.GrundstueckunterhaltEditComponent)
  }
];
