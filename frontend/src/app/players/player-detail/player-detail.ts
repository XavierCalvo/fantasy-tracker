import { ChangeDetectionStrategy, Component, computed, effect, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatListModule } from '@angular/material/list';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { map } from 'rxjs';
import { PlayerApi } from '../../core/services/player-api';
import { PlayerPriceApi } from '../../core/services/player-price-api';
import { TrackedPlayerApi } from '../../core/services/tracked-player-api';
import { Player } from '../../core/models/player';
import { PLAYER_PRICE_TREND_LABELS, PlayerPrice } from '../../core/models/player-price';
import {
  TRACKED_PLAYER_STATUS_LABELS,
  TrackedPlayer,
  TrackedPlayerRequest,
  TrackedPlayerStatus,
} from '../../core/models/tracked-player';
import { PLAYER_POSITION_LABELS } from '../../core/models/player-position';
import { StatusMessage } from '../../shared/status-message/status-message';

@Component({
  imports: [
    RouterLink,
    FormsModule,
    DatePipe,
    DecimalPipe,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatListModule,
    MatSnackBarModule,
    StatusMessage,
  ],
  selector: 'app-player-detail',
  styleUrl: './player-detail.scss',
  templateUrl: './player-detail.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlayerDetail {
  private readonly route = inject(ActivatedRoute);
  private readonly playerApi = inject(PlayerApi);
  private readonly playerPriceApi = inject(PlayerPriceApi);
  private readonly trackedPlayerApi = inject(TrackedPlayerApi);
  private readonly snackBar = inject(MatSnackBar);

  readonly statusOptions: TrackedPlayerStatus[] = ['WATCHING', 'OWNED', 'DISCARDED'];
  readonly statusLabels = TRACKED_PLAYER_STATUS_LABELS;
  readonly trendLabels = PLAYER_PRICE_TREND_LABELS;
  readonly positionLabels = PLAYER_POSITION_LABELS;

  private readonly playerId = toSignal(
    this.route.paramMap.pipe(map((params) => Number(params.get('id')))),
    { initialValue: NaN },
  );

  readonly loading = signal(true);
  readonly error = signal(false);
  readonly player = signal<Player | null>(null);
  readonly prices = signal<PlayerPrice[]>([]);
  readonly latestPrice = computed(() => this.prices()[0] ?? null);

  readonly tracking = signal<TrackedPlayer | null>(null);
  readonly trackingSaving = signal(false);
  readonly priceRefreshing = signal(false);

  readonly editStatus = signal<TrackedPlayerStatus>('WATCHING');
  readonly editClause = signal<number | null>(null);
  readonly editClauseReleaseDate = signal<Date | null>(null);
  readonly editNotes = signal<string>('');

  constructor() {
    effect(() => {
      const id = this.playerId();
      if (Number.isNaN(id)) {
        return;
      }
      this.loadPlayer(id);
      this.loadTracking(id);
    });
  }

  private loadPlayer(id: number): void {
    this.loading.set(true);
    this.error.set(false);

    this.playerApi.get(id).subscribe({
      next: (player) => {
        this.player.set(player);
        this.playerPriceApi.list(id).subscribe({
          next: (prices) => {
            this.prices.set(prices);
            this.loading.set(false);
          },
          error: () => {
            this.prices.set([]);
            this.loading.set(false);
          },
        });
      },
      error: () => {
        this.loading.set(false);
        this.error.set(true);
      },
    });
  }

  private loadTracking(id: number): void {
    this.trackedPlayerApi.getByPlayer(id).subscribe({
      next: (tracking) => {
        this.tracking.set(tracking);
        this.editStatus.set(tracking.status);
        this.editClause.set(tracking.clause);
        this.editClauseReleaseDate.set(
          tracking.clauseReleaseDate ? new Date(tracking.clauseReleaseDate) : null,
        );
        this.editNotes.set(tracking.notes ?? '');
      },
      error: (err: HttpErrorResponse) => {
        // No tracking created yet for this player: keep defaults so the user can create one.
        if (err.status === 404) {
          this.tracking.set(null);
        }
      },
    });
  }

  refreshPrice(): void {
    const id = this.playerId();
    if (Number.isNaN(id) || this.priceRefreshing()) {
      return;
    }

    this.priceRefreshing.set(true);
    this.playerPriceApi.refresh(id).subscribe({
      next: (price) => {
        this.prices.set([price, ...this.prices()]);
        this.priceRefreshing.set(false);
        this.snackBar.open('Precio actualizado', 'Cerrar', { duration: 3000 });
      },
      error: (err: HttpErrorResponse) => {
        this.priceRefreshing.set(false);
        const message =
          err.status === 400
            ? 'Este jugador no tiene identificador externo configurado.'
            : 'No se pudo obtener el precio actual.';
        this.snackBar.open(message, 'Cerrar', { duration: 3000 });
      },
    });
  }

  saveTracking(): void {
    const id = this.playerId();
    if (Number.isNaN(id)) {
      return;
    }

    const request: TrackedPlayerRequest = {
      status: this.editStatus(),
      clause: this.editClause(),
      clauseReleaseDate: this.editClauseReleaseDate()
        ? this.editClauseReleaseDate()!.toISOString().slice(0, 10)
        : null,
      notes: this.editNotes() || null,
    };

    this.trackingSaving.set(true);
    const existing = this.tracking();
    const request$ = existing
      ? this.trackedPlayerApi.update(existing.id, request)
      : this.trackedPlayerApi.create(id, request);

    request$.subscribe({
      next: (tracking) => {
        this.tracking.set(tracking);
        this.trackingSaving.set(false);
        this.snackBar.open('Seguimiento guardado', 'Cerrar', { duration: 3000 });
      },
      error: () => {
        this.trackingSaving.set(false);
        this.snackBar.open('No se pudo guardar el seguimiento', 'Cerrar', { duration: 3000 });
      },
    });
  }
}
