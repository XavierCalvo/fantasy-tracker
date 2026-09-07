import { PlayerPosition } from './player-position';
import { PlayerPriceTrendType } from './player-price';

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

/** Enriched row returned by GET /api/tracking, used by the watchlist screen. */
export interface TrackedPlayerListItem {
  id: number;
  playerId: number;
  playerName: string;
  playerTeam: string | null;
  playerPosition: PlayerPosition | null;
  status: TrackedPlayerStatus;
  clause: number | null;
  clauseReleaseDate: string | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
  latestPrice: number | null;
  latestTrendAmount: number | null;
  latestTrendType: PlayerPriceTrendType | null;
  latestPriceCapturedAt: string | null;
}

/** Per-player outcome of a bulk price refresh (POST /api/tracking/prices/refresh). */
export interface BulkPriceRefreshItem {
  playerId: number;
  playerName: string;
  success: boolean;
  price: {
    id: number;
    playerId: number;
    price: number;
    trendAmount: number | null;
    trendType: PlayerPriceTrendType | null;
    capturedAt: string;
  } | null;
  error: string | null;
}

export interface BulkPriceRefreshResponse {
  results: BulkPriceRefreshItem[];
}
