import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TrackedPlayerApi } from './tracked-player-api';
import { TrackedPlayer, TrackedPlayerListItem } from '../models/tracked-player';

describe('TrackedPlayerApi', () => {
  let service: TrackedPlayerApi;
  let httpMock: HttpTestingController;

  const tracking: TrackedPlayer = {
    id: 1,
    playerId: 1,
    status: 'WATCHING',
    clause: null,
    clauseReleaseDate: null,
    notes: null,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  };

  const trackingListItem: TrackedPlayerListItem = {
    ...tracking,
    playerName: 'Test Player',
    playerTeam: 'Test Team',
    playerPosition: 'FWD',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(TrackedPlayerApi);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get tracking information for a player', () => {
    service.getByPlayer(1).subscribe((result) => {
      expect(result).toEqual(tracking);
    });

    const req = httpMock.expectOne('/api/players/1/tracking');
    expect(req.request.method).toBe('GET');
    req.flush(tracking);
  });

  it('should list all tracked players', () => {
    service.list().subscribe((result) => {
      expect(result).toEqual([trackingListItem]);
    });

    const req = httpMock.expectOne('/api/tracking');
    expect(req.request.method).toBe('GET');
    req.flush([trackingListItem]);
  });

  it('should create tracking information', () => {
    service.create(1, { status: 'WATCHING' }).subscribe((result) => {
      expect(result).toEqual(tracking);
    });

    const req = httpMock.expectOne('/api/players/1/tracking');
    expect(req.request.method).toBe('POST');
    req.flush(tracking);
  });

  it('should update tracking information', () => {
    service.update(1, { status: 'OWNED' }).subscribe((result) => {
      expect(result).toEqual(tracking);
    });

    const req = httpMock.expectOne('/api/tracking/1');
    expect(req.request.method).toBe('PUT');
    req.flush(tracking);
  });

  it('should delete tracking information', () => {
    service.delete(1).subscribe();

    const req = httpMock.expectOne('/api/tracking/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
