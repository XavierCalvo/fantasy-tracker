import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'players' },
  {
    path: 'players',
    loadComponent: () => import('./players/player-list/player-list').then((m) => m.PlayerList),
  },
  {
    path: 'players/:id',
    loadComponent: () => import('./players/player-detail/player-detail').then((m) => m.PlayerDetail),
  },
  { path: '**', redirectTo: 'players' },
];
