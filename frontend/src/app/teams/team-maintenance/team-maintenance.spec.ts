import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TeamMaintenance } from './team-maintenance';
import { Team } from '../../core/models/team';

describe('TeamMaintenance', () => {
  let component: TeamMaintenance;
  let fixture: ComponentFixture<TeamMaintenance>;
  let httpMock: HttpTestingController;

  const teams: Team[] = [
    { id: 1, name: 'FC Alpha' },
    { id: 2, name: 'FC Beta' },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeamMaintenance, NoopAnimationsModule],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamMaintenance);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should list teams returned by the API', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/teams').flush(teams);
    await fixture.whenStable();
    fixture.detectChanges();

    expect(component.teams()).toEqual(teams);
  });

  it('should create a new team and reload the list', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/teams').flush(teams);
    await fixture.whenStable();

    component.newTeamName.set('FC Gamma');
    component.createTeam();

    const createReq = httpMock.expectOne('/api/teams');
    expect(createReq.request.method).toBe('POST');
    expect(createReq.request.body).toEqual({ name: 'FC Gamma' });
    createReq.flush({ id: 3, name: 'FC Gamma' });

    httpMock.expectOne('/api/teams').flush([...teams, { id: 3, name: 'FC Gamma' }]);
    expect(component.newTeamName()).toBe('');
  });

  it('should update an existing team', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/teams').flush(teams);
    await fixture.whenStable();

    component.startEdit(teams[0]);
    component.editingName.set('FC Alpha Renamed');
    component.saveEdit();

    const updateReq = httpMock.expectOne('/api/teams/1');
    expect(updateReq.request.method).toBe('PUT');
    expect(updateReq.request.body).toEqual({ name: 'FC Alpha Renamed' });
    updateReq.flush({ id: 1, name: 'FC Alpha Renamed' });

    httpMock.expectOne('/api/teams').flush([{ id: 1, name: 'FC Alpha Renamed' }, teams[1]]);
    expect(component.editingId()).toBeNull();
  });

  it('should show an error state when the API call fails', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/teams').flush('error', { status: 500, statusText: 'Server Error' });
    await fixture.whenStable();
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('No se ha podido conectar con el backend');
  });
});
