import {Routes} from '@angular/router';

export const ERBSCHAFT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./erbschaft/erbschaft.component').then(m => m.ErbschaftComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('./erbschaft-edit/erbschaft-edit.component').then(m => m.ErbschaftEditComponent)
  },
  {
    path: 'edit/:erbschaftId',
    loadComponent: () => import('./erbschaft-edit/erbschaft-edit.component').then(m => m.ErbschaftEditComponent)
  }
];
