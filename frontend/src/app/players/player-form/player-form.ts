import { ChangeDetectionStrategy, Component, computed, effect, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { map } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { PlayerApi } from '../../core/services/player-api';
import { PlayerRequest } from '../../core/models/player';
import { StatusMessage } from '../../shared/status-message/status-message';

@Component({
  imports: [
    RouterLink,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    StatusMessage,
  ],
  selector: 'app-player-form',
  styleUrl: './player-form.scss',
  templateUrl: './player-form.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlayerForm {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly playerApi = inject(PlayerApi);
  private readonly snackBar = inject(MatSnackBar);

  // Present on the "/players/:id/edit" route only; absent on "/players/new".
  private readonly playerId = toSignal(
    this.route.paramMap.pipe(map((params) => (params.has('id') ? Number(params.get('id')) : null))),
    { initialValue: null },
  );

  readonly isEditMode = computed(() => this.playerId() !== null);

  readonly loading = signal(false);
  readonly error = signal(false);
  readonly saving = signal(false);

  readonly name = signal('');
  readonly team = signal('');
  readonly position = signal('');
  readonly externalId = signal('');

  readonly nameValid = computed(() => {
    const value = this.name().trim();
    return value.length > 0 && value.length <= 255;
  });
  readonly canSave = computed(() => this.nameValid() && !this.saving());

  constructor() {
    effect(() => {
      const id = this.playerId();
      if (id === null) {
        this.name.set('');
        this.team.set('');
        this.position.set('');
        this.externalId.set('');
        this.error.set(false);
        return;
      }

      this.loading.set(true);
      this.error.set(false);
      this.playerApi.get(id).subscribe({
        next: (player) => {
          this.name.set(player.name);
          this.team.set(player.team ?? '');
          this.position.set(player.position ?? '');
          this.externalId.set(player.externalId ?? '');
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
          this.error.set(true);
        },
      });
    });
  }

  save(): void {
    if (!this.canSave()) {
      return;
    }

    const request: PlayerRequest = {
      name: this.name().trim(),
      team: this.team().trim(),
      position: this.position().trim(),
      externalId: this.externalId().trim() || null,
    };

    this.saving.set(true);
    const id = this.playerId();
    const request$ = id !== null ? this.playerApi.update(id, request) : this.playerApi.create(request);

    request$.subscribe({
      next: (player) => {
        this.saving.set(false);
        this.snackBar.open(id !== null ? 'Jugador actualizado' : 'Jugador creado', 'Cerrar', {
          duration: 3000,
        });
        this.router.navigate(['/players', player.id]);
      },
      error: () => {
        this.saving.set(false);
        this.snackBar.open('No se pudo guardar el jugador', 'Cerrar', { duration: 3000 });
      },
    });
  }
}
