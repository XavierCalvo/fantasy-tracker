import { PlayerPosition } from './player-position';

export interface Player {
  id: number;
  name: string;
  teamId: number | null;
  teamName: string | null;
  position: PlayerPosition | null;
  externalId: string | null;
  createdAt: string;
}

export interface PlayerRequest {
  name: string;
  teamId: number | null;
  position: PlayerPosition | null;
  externalId?: string | null;
}
