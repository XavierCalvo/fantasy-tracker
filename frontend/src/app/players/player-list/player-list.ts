import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { catchError, map, of, startWith } from 'rxjs';
import { PlayerApi } from '../../core/services/player-api';
import { Player } from '../../core/models/player';
import { StatusMessage } from '../../shared/status-message/status-message';

interface PlayerListState {
  loading: boolean;
  error: boolean;
  players: Player[];
}

@Component({
  imports: [
    RouterLink,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    FormsModule,
    StatusMessage,
  ],
  selector: 'app-player-list',
  styleUrl: './player-list.scss',
  templateUrl: './player-list.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlayerList {
  private readonly playerApi = inject(PlayerApi);

  readonly search = signal('');

  private readonly state = toSignal(
    this.playerApi.list().pipe(
      map((players): PlayerListState => ({ loading: false, error: false, players })),
      startWith<PlayerListState>({ loading: true, error: false, players: [] }),
      catchError(() => of<PlayerListState>({ loading: false, error: true, players: [] })),
    ),
    { initialValue: { loading: true, error: false, players: [] } as PlayerListState },
  );

  readonly loading = () => this.state().loading;
  readonly error = () => this.state().error;

  readonly filteredPlayers = () => {
    const term = this.search().trim().toLowerCase();
    const players = this.state().players;
    if (!term) {
      return players;
    }
    return players.filter(
      (player) =>
        player.name.toLowerCase().includes(term) ||
        player.team.toLowerCase().includes(term) ||
        player.position.toLowerCase().includes(term),
    );
  };
}
