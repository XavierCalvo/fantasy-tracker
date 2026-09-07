import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Player, PlayerRequest } from '../models/player';

@Service()
export class PlayerApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/players';

  list(): Observable<Player[]> {
    return this.http.get<Player[]>(this.baseUrl);
  }

  get(id: number): Observable<Player> {
    return this.http.get<Player>(`${this.baseUrl}/${id}`);
  }

  create(request: PlayerRequest): Observable<Player> {
    return this.http.post<Player>(this.baseUrl, request);
  }

  update(id: number, request: PlayerRequest): Observable<Player> {
    return this.http.put<Player>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
