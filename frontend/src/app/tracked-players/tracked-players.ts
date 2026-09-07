import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { catchError, map, of, startWith } from 'rxjs';
import { TrackedPlayerApi } from '../core/services/tracked-player-api';
import {
  TRACKED_PLAYER_STATUS_LABELS,
  TrackedPlayerListItem,
  TrackedPlayerStatus,
} from '../core/models/tracked-player';
import { StatusMessage } from '../shared/status-message/status-message';

interface TrackedPlayersState {
  loading: boolean;
  error: boolean;
  items: TrackedPlayerListItem[];
}

type StatusFilter = TrackedPlayerStatus | 'ALL';
type SortField = 'clauseReleaseDate' | 'playerName' | 'status';
type SortDirection = 'asc' | 'desc';

@Component({
  imports: [
    RouterLink,
    DatePipe,
    MatListModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatChipsModule,
    MatIconModule,
    FormsModule,
    StatusMessage,
  ],
  selector: 'app-tracked-players',
  styleUrl: './tracked-players.scss',
  templateUrl: './tracked-players.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TrackedPlayers {
  private readonly trackedPlayerApi = inject(TrackedPlayerApi);

  readonly statusLabels = TRACKED_PLAYER_STATUS_LABELS;
  readonly statusOptions: TrackedPlayerStatus[] = ['WATCHING', 'OWNED', 'DISCARDED'];

  readonly statusFilter = signal<StatusFilter>('ALL');
  readonly sortField = signal<SortField>('clauseReleaseDate');
  readonly sortDirection = signal<SortDirection>('asc');

  private readonly state = toSignal(
    this.trackedPlayerApi.list().pipe(
      map((items): TrackedPlayersState => ({ loading: false, error: false, items })),
      startWith<TrackedPlayersState>({ loading: true, error: false, items: [] }),
      catchError(() => of<TrackedPlayersState>({ loading: false, error: true, items: [] })),
    ),
    { initialValue: { loading: true, error: false, items: [] } as TrackedPlayersState },
  );

  readonly loading = () => this.state().loading;
  readonly error = () => this.state().error;

  readonly filteredItems = computed(() => {
    const filter = this.statusFilter();
    const items = this.state().items;
    const filtered = filter === 'ALL' ? items : items.filter((item) => item.status === filter);
    return this.sortItems(filtered);
  });

  toggleSortDirection(): void {
    this.sortDirection.set(this.sortDirection() === 'asc' ? 'desc' : 'asc');
  }

  private sortItems(items: TrackedPlayerListItem[]): TrackedPlayerListItem[] {
    const field = this.sortField();
    const direction = this.sortDirection() === 'asc' ? 1 : -1;

    return [...items].sort((a, b) => {
      const valueA = this.sortValue(a, field);
      const valueB = this.sortValue(b, field);

      // Players without a clause release date are pushed to the end regardless of direction.
      if (field === 'clauseReleaseDate') {
        if (valueA === null && valueB === null) return 0;
        if (valueA === null) return 1;
        if (valueB === null) return -1;
      }

      if (valueA! < valueB!) return -1 * direction;
      if (valueA! > valueB!) return 1 * direction;
      return 0;
    });
  }

  private sortValue(item: TrackedPlayerListItem, field: SortField): string | null {
    switch (field) {
      case 'clauseReleaseDate':
        return item.clauseReleaseDate;
      case 'playerName':
        return item.playerName;
      case 'status':
        return item.status;
    }
  }
}
