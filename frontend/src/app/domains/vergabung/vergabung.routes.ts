import {Routes} from '@angular/router';

export const VERGABUNG_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./vergabung/vergabung.component').then(m => m.VergabungComponent)
  }
];
