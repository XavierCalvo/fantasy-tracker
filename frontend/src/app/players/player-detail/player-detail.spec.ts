import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { PlayerDetail } from './player-detail';
import { Player } from '../../core/models/player';
import { PlayerPrice } from '../../core/models/player-price';
import { TrackedPlayer } from '../../core/models/tracked-player';

describe('PlayerDetail', () => {
  let component: PlayerDetail;
  let fixture: ComponentFixture<PlayerDetail>;
  let httpMock: HttpTestingController;

  const player: Player = {
    id: 1,
    name: 'Alpha Striker',
    teamId: 1,
    teamName: 'FC Alpha',
    position: 'DELANTERO',
    externalId: null,
    createdAt: '2024-01-01T00:00:00Z',
  };

  const prices: PlayerPrice[] = [
    {
      id: 1,
      playerId: 1,
      price: 1000000,
      trendAmount: 5000,
      trendType: 'STABLE_UP',
      capturedAt: '2024-01-02T00:00:00Z',
    },
  ];

  const tracking: TrackedPlayer = {
    id: 5,
    playerId: 1,
    status: 'WATCHING',
    clause: 2000000,
    clauseReleaseDate: '2024-06-01',
    notes: 'Ojo con este jugador',
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  };

  async function setup() {
    await TestBed.configureTestingModule({
      imports: [PlayerDetail, NoopAnimationsModule],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: { paramMap: of(convertToParamMap({ id: '1' })) },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PlayerDetail);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  }

  it('should load player, prices and tracking information', async () => {
    await setup();
    fixture.detectChanges();

    httpMock.expectOne('/api/players/1').flush(player);
    httpMock.expectOne('/api/players/1/prices').flush(prices);
    httpMock.expectOne('/api/players/1/tracking').flush(tracking);

    await fixture.whenStable();
    fixture.detectChanges();

    expect(component.player()).toEqual(player);
    expect(component.prices()).toEqual(prices);
    expect(component.tracking()).toEqual(tracking);
    expect(fixture.nativeElement.textContent).toContain('Alpha Striker');
  });

  it('should handle a player with no tracking yet (404)', async () => {
    await setup();
    fixture.detectChanges();

    httpMock.expectOne('/api/players/1').flush(player);
    httpMock.expectOne('/api/players/1/prices').flush([]);
    httpMock
      .expectOne('/api/players/1/tracking')
      .flush('not found', { status: 404, statusText: 'Not Found' });

    await fixture.whenStable();
    fixture.detectChanges();

    expect(component.tracking()).toBeNull();
  });

  it('should create tracking information when none exists', async () => {
    await setup();
    fixture.detectChanges();

    httpMock.expectOne('/api/players/1').flush(player);
    httpMock.expectOne('/api/players/1/prices').flush([]);
    httpMock
      .expectOne('/api/players/1/tracking')
      .flush('not found', { status: 404, statusText: 'Not Found' });
    await fixture.whenStable();

    component.saveTracking();
    const createReq = httpMock.expectOne('/api/players/1/tracking');
    expect(createReq.request.method).toBe('POST');
    createReq.flush(tracking);

    expect(component.tracking()).toEqual(tracking);
  });

  it('should refresh the latest price when requested', async () => {
    const playerWithExternalId: Player = { ...player, externalId: 'alvaro-valles' };
    await setup();
    fixture.detectChanges();

    httpMock.expectOne('/api/players/1').flush(playerWithExternalId);
    httpMock.expectOne('/api/players/1/prices').flush(prices);
    httpMock.expectOne('/api/players/1/tracking').flush(tracking);
    await fixture.whenStable();
    fixture.detectChanges();

    component.refreshPrice();
    const refreshReq = httpMock.expectOne('/api/players/1/prices/refresh');
    expect(refreshReq.request.method).toBe('POST');
    const newPrice: PlayerPrice = {
      id: 2,
      playerId: 1,
      price: 1050000,
      trendAmount: 50000,
      trendType: 'ACCELERATING_UP',
      capturedAt: '2024-01-03T00:00:00Z',
    };
    refreshReq.flush(newPrice);

    expect(component.prices()[0]).toEqual(newPrice);
  });

  it('should show the stale icon when the latest price is older than 24h', async () => {
    await setup();
    fixture.detectChanges();

    httpMock.expectOne('/api/players/1').flush(player);
    httpMock.expectOne('/api/players/1/prices').flush(prices);
    httpMock.expectOne('/api/players/1/tracking').flush(tracking);
    await fixture.whenStable();
    fixture.detectChanges();

    expect(component.latestPriceStale()).toBe(true);
    expect(fixture.nativeElement.querySelector('.player-detail__stale-icon')).toBeTruthy();
  });

  it('should surface the backend error message when refreshing the price fails', async () => {
    const playerWithExternalId: Player = { ...player, externalId: 'no-market-player' };
    await setup();
    fixture.detectChanges();

    httpMock.expectOne('/api/players/1').flush(playerWithExternalId);
    httpMock.expectOne('/api/players/1/prices').flush(prices);
    httpMock.expectOne('/api/players/1/tracking').flush(tracking);
    await fixture.whenStable();
    fixture.detectChanges();

    component.refreshPrice();
    const refreshReq = httpMock.expectOne('/api/players/1/prices/refresh');
    refreshReq.flush(
      { message: 'No se encontró un valor de mercado para este jugador' },
      { status: 502, statusText: 'Bad Gateway' },
    );

    expect(component.priceRefreshing()).toBe(false);
  });
});
