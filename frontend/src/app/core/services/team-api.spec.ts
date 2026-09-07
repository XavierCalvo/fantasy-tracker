import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TeamApi } from './team-api';
import { Team } from '../models/team';

describe('TeamApi', () => {
  let service: TeamApi;
  let httpMock: HttpTestingController;

  const team: Team = { id: 1, name: 'Test FC' };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(TeamApi);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should list teams', () => {
    service.list().subscribe((teams) => {
      expect(teams).toEqual([team]);
    });

    const req = httpMock.expectOne('/api/teams');
    expect(req.request.method).toBe('GET');
    req.flush([team]);
  });

  it('should get a single team', () => {
    service.get(1).subscribe((result) => {
      expect(result).toEqual(team);
    });

    const req = httpMock.expectOne('/api/teams/1');
    expect(req.request.method).toBe('GET');
    req.flush(team);
  });

  it('should create a team', () => {
    service.create({ name: 'Test FC' }).subscribe((result) => {
      expect(result).toEqual(team);
    });

    const req = httpMock.expectOne('/api/teams');
    expect(req.request.method).toBe('POST');
    req.flush(team);
  });

  it('should update a team', () => {
    service.update(1, { name: 'Renamed FC' }).subscribe((result) => {
      expect(result).toEqual(team);
    });

    const req = httpMock.expectOne('/api/teams/1');
    expect(req.request.method).toBe('PUT');
    req.flush(team);
  });
});
