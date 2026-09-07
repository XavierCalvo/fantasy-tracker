import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TrackedPlayers } from './tracked-players';
import { TrackedPlayerListItem } from '../core/models/tracked-player';

describe('TrackedPlayers', () => {
  let component: TrackedPlayers;
  let fixture: ComponentFixture<TrackedPlayers>;
  let httpMock: HttpTestingController;

  const items: TrackedPlayerListItem[] = [
    {
      id: 1,
      playerId: 1,
      playerName: 'Alpha Striker',
      playerTeam: 'FC Alpha',
      playerPosition: 'DEL',
      status: 'OWNED',
      clause: 50000000,
      clauseReleaseDate: '2026-06-30',
      notes: null,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    {
      id: 2,
      playerId: 2,
      playerName: 'Beta Defender',
      playerTeam: 'FC Beta',
      playerPosition: 'DEF',
      status: 'WATCHING',
      clause: null,
      clauseReleaseDate: null,
      notes: null,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    {
      id: 3,
      playerId: 3,
      playerName: 'Gamma Keeper',
      playerTeam: 'FC Gamma',
      playerPosition: 'POR',
      status: 'OWNED',
      clause: 10000000,
      clauseReleaseDate: '2026-01-15',
      notes: null,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackedPlayers],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(TrackedPlayers);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/tracking').flush(items);
    expect(component).toBeTruthy();
  });

  it('should list tracked players sorted by clause release date ascending by default', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/tracking').flush(items);
    await fixture.whenStable();

    const sorted = component.filteredItems();
    expect(sorted.map((item) => item.id)).toEqual([3, 1, 2]);
  });

  it('should filter by status', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/tracking').flush(items);
    await fixture.whenStable();

    component.statusFilter.set('WATCHING');
    fixture.detectChanges();

    const filtered = component.filteredItems();
    expect(filtered.map((item) => item.id)).toEqual([2]);
  });

  it('should reverse sort direction when toggled', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/tracking').flush(items);
    await fixture.whenStable();

    component.toggleSortDirection();
    fixture.detectChanges();

    const sorted = component.filteredItems();
    expect(sorted.map((item) => item.id)).toEqual([1, 3, 2]);
  });

  it('should show an error state when the API call fails', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/tracking').flush('error', { status: 500, statusText: 'Server Error' });
    await fixture.whenStable();
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('No se ha podido conectar con el backend');
  });
});
