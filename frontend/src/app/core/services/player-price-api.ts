import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlayerPrice, PlayerPriceRequest } from '../models/player-price';

@Service()
export class PlayerPriceApi {
  private readonly http = inject(HttpClient);

  private baseUrl(playerId: number): string {
    return `/api/players/${playerId}/prices`;
  }

  list(playerId: number): Observable<PlayerPrice[]> {
    return this.http.get<PlayerPrice[]>(this.baseUrl(playerId));
  }

  create(playerId: number, request: PlayerPriceRequest): Observable<PlayerPrice> {
    return this.http.post<PlayerPrice>(this.baseUrl(playerId), request);
  }
}
