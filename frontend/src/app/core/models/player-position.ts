/** Fixed set of playing positions (no free text). Order matches the dropdown display order. */
export type PlayerPosition = 'PORTERO' | 'DEFENSA' | 'MEDIO' | 'DELANTERO' | 'ENTRENADOR';

export const PLAYER_POSITIONS: PlayerPosition[] = ['PORTERO', 'DEFENSA', 'MEDIO', 'DELANTERO', 'ENTRENADOR'];

export const PLAYER_POSITION_LABELS: Record<PlayerPosition, string> = {
  PORTERO: 'Portero',
  DEFENSA: 'Defensa',
  MEDIO: 'Medio',
  DELANTERO: 'Delantero',
  ENTRENADOR: 'Entrenador',
};

export const DEFAULT_PLAYER_POSITION: PlayerPosition = 'DEFENSA';
