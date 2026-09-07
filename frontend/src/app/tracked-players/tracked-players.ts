import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { TrackedPlayerApi } from '../core/services/tracked-player-api';
import {
  PLAYER_PRICE_TREND_LABELS,
} from '../core/models/player-price';
import {
  TRACKED_PLAYER_STATUS_LABELS,
  TrackedPlayerListItem,
  TrackedPlayerStatus,
} from '../core/models/tracked-player';
import { PLAYER_POSITION_LABELS, PLAYER_POSITIONS } from '../core/models/player-position';
import { StatusMessage } from '../shared/status-message/status-message';
import { isPriceStale } from '../shared/price-staleness';

type StatusFilter = TrackedPlayerStatus | 'ALL';
type SortField = 'clauseReleaseDate' | 'playerName' | 'status' | 'playerPosition' | 'latestTrendAmount';
type SortDirection = 'asc' | 'desc';

@Component({
  imports: [
    RouterLink,
    DatePipe,
    DecimalPipe,
    MatListModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatChipsModule,
    MatIconModule,
    MatTooltipModule,
    MatSnackBarModule,
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
  private readonly snackBar = inject(MatSnackBar);

  readonly statusLabels = TRACKED_PLAYER_STATUS_LABELS;
  readonly positionLabels = PLAYER_POSITION_LABELS;
  readonly trendLabels = PLAYER_PRICE_TREND_LABELS;
  readonly statusOptions: TrackedPlayerStatus[] = ['WATCHING', 'OWNED', 'DISCARDED'];
  readonly isPriceStale = isPriceStale;

  readonly statusFilter = signal<StatusFilter>('ALL');
  readonly sortField = signal<SortField>('clauseReleaseDate');
  readonly sortDirection = signal<SortDirection>('asc');

  readonly loading = signal(true);
  readonly error = signal(false);
  readonly items = signal<TrackedPlayerListItem[]>([]);
  readonly refreshingAll = signal(false);

  constructor() {
    this.loadTracked();
  }

  private loadTracked(): void {
    this.loading.set(true);
    this.error.set(false);
    this.trackedPlayerApi.list().subscribe({
      next: (items) => {
        this.items.set(items);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.error.set(true);
      },
    });
  }

  refreshAllPrices(): void {
    if (this.refreshingAll()) {
      return;
    }

    this.refreshingAll.set(true);
    this.trackedPlayerApi.refreshAllPrices().subscribe({
      next: (response) => {
        this.refreshingAll.set(false);
        const failures = response.results.filter((item) => !item.success);
        if (failures.length === 0) {
          this.snackBar.open(
            `Precios actualizados (${response.results.length} jugadores)`,
            'Cerrar',
            { duration: 3000 },
          );
        } else {
          this.snackBar.open(
            `${response.results.length - failures.length} de ${response.results.length} precios actualizados. Fallos: ${failures.map((f) => f.playerName).join(', ')}`,
            'Cerrar',
            { duration: 6000 },
          );
        }
        this.loadTracked();
      },
      error: () => {
        this.refreshingAll.set(false);
        this.snackBar.open('No se pudieron actualizar los precios', 'Cerrar', { duration: 3000 });
      },
    });
  }

  readonly filteredItems = computed(() => {
    const filter = this.statusFilter();
    const items = this.items();
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

      // Players without a value for the chosen field (no clause date, no position, no price
      // recorded yet...) are pushed to the end regardless of direction.
      if (valueA === null && valueB === null) return 0;
      if (valueA === null) return 1;
      if (valueB === null) return -1;

      if (valueA < valueB) return -1 * direction;
      if (valueA > valueB) return 1 * direction;
      return 0;
    });
  }

  private sortValue(item: TrackedPlayerListItem, field: SortField): string | number | null {
    switch (field) {
      case 'clauseReleaseDate':
        return item.clauseReleaseDate;
      case 'playerName':
        return item.playerName;
      case 'status':
        return item.status;
      case 'playerPosition':
        return item.playerPosition ? PLAYER_POSITIONS.indexOf(item.playerPosition) : null;
      case 'latestTrendAmount':
        return item.latestTrendAmount;
    }
  }
}
