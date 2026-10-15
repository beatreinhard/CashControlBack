import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./domains/dashboard/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  { path: 'person', loadChildren: () => import('./domains/person/person.routes').then(m => m.PERSON_ROUTES) },
  { path: 'ausgabe', loadChildren: () => import('./domains/ausgabe/ausgabe.routes').then(m => m.AUSGABE_ROUTES) },
  { path: 'vergabung', loadChildren: () => import('./domains/vergabung/vergabung.routes').then(m => m.VERGABUNG_ROUTES) },
  { path: 'schuld', loadChildren: () => import('./domains/schuld/schuld.routes').then(m => m.SCHULD_ROUTES) },
  { path: 'kosten', loadChildren: () => import('./domains/kosten/kosten.routes').then(m => m.KOSTEN_ROUTES) },
  {
    path: 'reporting',
    loadComponent: () => import('./domains/reporting/reporting/reporting.component').then(m => m.ReportingComponent)
  },
];
