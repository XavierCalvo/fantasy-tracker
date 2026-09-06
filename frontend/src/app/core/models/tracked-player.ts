export type TrackedPlayerStatus = 'WATCHING' | 'OWNED' | 'DISCARDED';

export const TRACKED_PLAYER_STATUS_LABELS: Record<TrackedPlayerStatus, string> = {
  WATCHING: 'En seguimiento',
  OWNED: 'En plantilla',
  DISCARDED: 'Descartado',
};

export interface TrackedPlayer {
  id: number;
  playerId: number;
  status: TrackedPlayerStatus;
  clause: number | null;
  clauseReleaseDate: string | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface TrackedPlayerRequest {
  status?: TrackedPlayerStatus | null;
  clause?: number | null;
  clauseReleaseDate?: string | null;
  notes?: string | null;
}
