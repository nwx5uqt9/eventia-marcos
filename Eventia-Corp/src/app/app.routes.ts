import { Routes } from '@angular/router';
import { AuthGuard } from './shared/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'client/events',
    pathMatch: 'full'
  },
  {
    path: 'index',
    loadComponent: () => import('./shared/pages/index-page/index-page'),
  },
  {
    path: 'register',
    loadComponent: () => import('./shared/pages/register-page/register-page'),
  },
  {
    path: 'admin',
    loadComponent: () => import('./admin/pages/index-page/index-page'),
    canActivate: [AuthGuard],
    data: { roles: [2] }, // Solo rol Administrador (ID 2)
    children: [
      {
        path: 'events',
        loadComponent: () => import('./admin/pages/events-page/events-page'),
      },
      {
        path: 'sales',
        loadComponent: () => import('./admin/pages/sales-page/sales-page'),
      },
      {
        path: 'clients',
        loadComponent: () => import('./admin/pages/clients-page/clients-page'),
      },
      {
        path: 'users',
        loadComponent: () => import('./admin/pages/users-page/users-page'),
      },
      {
        path: 'utilities',
        loadComponent: () => import('./admin/pages/utilities-page/utilities-page'),
      },
      {
        path: '**',
        redirectTo: 'events',
      },
    ],
  },
  {
    path: 'client',
    loadComponent: () => import('./client/pages/index-page/index-page'),
    children: [
      {
        path: 'events',
        loadComponent: () => import('./client/pages/events-page/events-page'),
        // Accesible para todos (sin guard)
      },
      {
        path: 'tickets',
        loadComponent: () => import('./client/pages/tickets-page/tickets-page'),
        canActivate: [AuthGuard],
        // Sin restricción de roles específicos - cualquier usuario autenticado puede acceder
        // Esto permite que roles personalizados creados desde utilities también accedan
      },
      {
        path: '**',
        redirectTo: 'events',
      },
    ],
  },
  {
    path: '**',
    redirectTo: 'client/events',
  },
];
