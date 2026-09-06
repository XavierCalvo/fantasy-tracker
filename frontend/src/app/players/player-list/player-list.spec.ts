import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { PlayerList } from './player-list';
import { Player } from '../../core/models/player';

describe('PlayerList', () => {
  let component: PlayerList;
  let fixture: ComponentFixture<PlayerList>;
  let httpMock: HttpTestingController;

  const players: Player[] = [
    { id: 1, name: 'Alpha Striker', team: 'FC Alpha', position: 'DEL', externalId: null, createdAt: '2024-01-01T00:00:00Z' },
    { id: 2, name: 'Beta Defender', team: 'FC Beta', position: 'DEF', externalId: null, createdAt: '2024-01-01T00:00:00Z' },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerList],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(PlayerList);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/players').flush(players);
    expect(component).toBeTruthy();
  });

  it('should list players returned by the API', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/players').flush(players);
    await fixture.whenStable();
    fixture.detectChanges();

    const items = fixture.nativeElement.querySelectorAll('a[mat-list-item]');
    expect(items.length).toBe(2);
  });

  it('should filter players by search term', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/players').flush(players);
    await fixture.whenStable();

    component.search.set('beta');
    fixture.detectChanges();

    const filtered = component.filteredPlayers();
    expect(filtered).toEqual([players[1]]);
  });

  it('should show an error state when the API call fails', async () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/players').flush('error', { status: 500, statusText: 'Server Error' });
    await fixture.whenStable();
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('No se ha podido conectar con el backend');
  });
});
