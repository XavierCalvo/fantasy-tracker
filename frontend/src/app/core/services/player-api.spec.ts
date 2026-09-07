import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { PlayerApi } from './player-api';
import { Player } from '../models/player';

describe('PlayerApi', () => {
  let service: PlayerApi;
  let httpMock: HttpTestingController;

  const player: Player = {
    id: 1,
    name: 'Test Player',
    teamId: 3,
    teamName: 'Test FC',
    position: 'DELANTERO',
    externalId: null,
    createdAt: '2024-01-01T00:00:00Z',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(PlayerApi);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should list players', () => {
    service.list().subscribe((players) => {
      expect(players).toEqual([player]);
    });

    const req = httpMock.expectOne('/api/players');
    expect(req.request.method).toBe('GET');
    req.flush([player]);
  });

  it('should get a single player', () => {
    service.get(1).subscribe((result) => {
      expect(result).toEqual(player);
    });

    const req = httpMock.expectOne('/api/players/1');
    expect(req.request.method).toBe('GET');
    req.flush(player);
  });

  it('should create a player', () => {
    service.create({ name: 'Test Player', teamId: 3, position: 'DELANTERO' }).subscribe((result) => {
      expect(result).toEqual(player);
    });

    const req = httpMock.expectOne('/api/players');
    expect(req.request.method).toBe('POST');
    req.flush(player);
  });

  it('should delete a player', () => {
    service.delete(1).subscribe();

    const req = httpMock.expectOne('/api/players/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
