import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'tracked' },
  {
    path: 'tracked',
    loadComponent: () => import('./tracked-players/tracked-players').then((m) => m.TrackedPlayers),
  },
  {
    path: 'players',
    loadComponent: () => import('./players/player-list/player-list').then((m) => m.PlayerList),
  },
  {
    path: 'players/:id',
    loadComponent: () => import('./players/player-detail/player-detail').then((m) => m.PlayerDetail),
  },
  { path: '**', redirectTo: 'tracked' },
];
