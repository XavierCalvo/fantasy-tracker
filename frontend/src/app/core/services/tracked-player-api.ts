import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TrackedPlayer, TrackedPlayerListItem, TrackedPlayerRequest } from '../models/tracked-player';

@Service()
export class TrackedPlayerApi {
  private readonly http = inject(HttpClient);

  getByPlayer(playerId: number): Observable<TrackedPlayer> {
    return this.http.get<TrackedPlayer>(`/api/players/${playerId}/tracking`);
  }

  list(): Observable<TrackedPlayerListItem[]> {
    return this.http.get<TrackedPlayerListItem[]>('/api/tracking');
  }

  create(playerId: number, request: TrackedPlayerRequest): Observable<TrackedPlayer> {
    return this.http.post<TrackedPlayer>(`/api/players/${playerId}/tracking`, request);
  }

  update(id: number, request: TrackedPlayerRequest): Observable<TrackedPlayer> {
    return this.http.put<TrackedPlayer>(`/api/tracking/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`/api/tracking/${id}`);
  }
}
