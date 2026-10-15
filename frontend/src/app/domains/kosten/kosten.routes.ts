import {Routes} from '@angular/router';

export const KOSTEN_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./kosten/kosten.component').then(m => m.KostenComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('./kosten-edit/kosten-edit.component').then(m => m.KostenEditComponent)
  },
  {
    path: 'edit/:kostenId',
    loadComponent: () => import('./kosten-edit/kosten-edit.component').then(m => m.KostenEditComponent)
  }
];
