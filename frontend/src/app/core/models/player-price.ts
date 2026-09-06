export type PlayerPriceTrendType =
  | 'INFLECTION_POSITIVE'
  | 'ACCELERATING_STRONGLY_UP'
  | 'ACCELERATING_UP'
  | 'STABLE_UP'
  | 'DECELERATING_UP'
  | 'DECELERATING_STRONGLY_UP'
  | 'INFLECTION_NEGATIVE'
  | 'DECELERATING_STRONGLY_DOWN'
  | 'DECELERATING_DOWN'
  | 'STABLE_DOWN'
  | 'ACCELERATING_DOWN'
  | 'ACCELERATING_STRONGLY_DOWN';

export const PLAYER_PRICE_TREND_LABELS: Record<PlayerPriceTrendType, string> = {
  INFLECTION_POSITIVE: 'Inflexión positiva',
  ACCELERATING_STRONGLY_UP: 'Acelera mucho (subida)',
  ACCELERATING_UP: 'Acelera (subida)',
  STABLE_UP: 'Estable (subida)',
  DECELERATING_UP: 'Desacelera (subida)',
  DECELERATING_STRONGLY_UP: 'Desacelera mucho (subida)',
  INFLECTION_NEGATIVE: 'Inflexión negativa',
  DECELERATING_STRONGLY_DOWN: 'Desacelera mucho (bajada)',
  DECELERATING_DOWN: 'Desacelera (bajada)',
  STABLE_DOWN: 'Estable (bajada)',
  ACCELERATING_DOWN: 'Acelera (bajada)',
  ACCELERATING_STRONGLY_DOWN: 'Acelera mucho (bajada)',
};

export interface PlayerPrice {
  id: number;
  playerId: number;
  price: number;
  trendAmount: number | null;
  trendType: PlayerPriceTrendType | null;
  capturedAt: string;
}

export interface PlayerPriceRequest {
  price: number;
  trendAmount?: number | null;
  trendType?: PlayerPriceTrendType | null;
}
