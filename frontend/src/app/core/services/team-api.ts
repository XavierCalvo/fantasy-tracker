import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Team, TeamRequest } from '../models/team';

@Service()
export class TeamApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/teams';

  list(): Observable<Team[]> {
    return this.http.get<Team[]>(this.baseUrl);
  }

  get(id: number): Observable<Team> {
    return this.http.get<Team>(`${this.baseUrl}/${id}`);
  }

  create(request: TeamRequest): Observable<Team> {
    return this.http.post<Team>(this.baseUrl, request);
  }

  update(id: number, request: TeamRequest): Observable<Team> {
    return this.http.put<Team>(`${this.baseUrl}/${id}`, request);
  }
}
