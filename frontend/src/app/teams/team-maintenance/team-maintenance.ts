import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { TeamApi } from '../../core/services/team-api';
import { Team } from '../../core/models/team';
import { StatusMessage } from '../../shared/status-message/status-message';

/**
 * Minimal team maintenance screen (id + name only). Expected to be used at
 * most once per season, so it favours a compact inline list/edit form over a
 * dedicated create/edit route.
 */
@Component({
  imports: [
    RouterLink,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatSnackBarModule,
    StatusMessage,
  ],
  selector: 'app-team-maintenance',
  styleUrl: './team-maintenance.scss',
  templateUrl: './team-maintenance.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamMaintenance {
  private readonly teamApi = inject(TeamApi);
  private readonly snackBar = inject(MatSnackBar);

  readonly loading = signal(true);
  readonly error = signal(false);
  readonly teams = signal<Team[]>([]);

  readonly newTeamName = signal('');
  readonly saving = signal(false);
  readonly editingId = signal<number | null>(null);
  readonly editingName = signal('');

  readonly canCreate = () => this.newTeamName().trim().length > 0 && !this.saving();

  constructor() {
    this.loadTeams();
  }

  private loadTeams(): void {
    this.loading.set(true);
    this.error.set(false);
    this.teamApi.list().subscribe({
      next: (teams) => {
        this.teams.set(teams);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.error.set(true);
      },
    });
  }

  createTeam(): void {
    const name = this.newTeamName().trim();
    if (!name) {
      return;
    }

    this.saving.set(true);
    this.teamApi.create({ name }).subscribe({
      next: () => {
        this.saving.set(false);
        this.newTeamName.set('');
        this.snackBar.open('Equipo creado', 'Cerrar', { duration: 3000 });
        this.loadTeams();
      },
      error: () => {
        this.saving.set(false);
        this.snackBar.open('No se pudo crear el equipo', 'Cerrar', { duration: 3000 });
      },
    });
  }

  startEdit(team: Team): void {
    this.editingId.set(team.id);
    this.editingName.set(team.name);
  }

  cancelEdit(): void {
    this.editingId.set(null);
    this.editingName.set('');
  }

  saveEdit(): void {
    const id = this.editingId();
    const name = this.editingName().trim();
    if (id === null || !name) {
      return;
    }

    this.saving.set(true);
    this.teamApi.update(id, { name }).subscribe({
      next: () => {
        this.saving.set(false);
        this.cancelEdit();
        this.snackBar.open('Equipo actualizado', 'Cerrar', { duration: 3000 });
        this.loadTeams();
      },
      error: () => {
        this.saving.set(false);
        this.snackBar.open('No se pudo actualizar el equipo', 'Cerrar', { duration: 3000 });
      },
    });
  }
}
