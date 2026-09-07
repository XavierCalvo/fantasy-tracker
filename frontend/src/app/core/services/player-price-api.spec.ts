import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { PlayerPriceApi } from './player-price-api';
import { PlayerPrice } from '../models/player-price';

describe('PlayerPriceApi', () => {
  let service: PlayerPriceApi;
  let httpMock: HttpTestingController;

  const price: PlayerPrice = {
    id: 1,
    playerId: 1,
    price: 1000000,
    trendAmount: 5000,
    trendType: 'STABLE_UP',
    capturedAt: '2024-01-01T00:00:00Z',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(PlayerPriceApi);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should list prices for a player', () => {
    service.list(1).subscribe((prices) => {
      expect(prices).toEqual([price]);
    });

    const req = httpMock.expectOne('/api/players/1/prices');
    expect(req.request.method).toBe('GET');
    req.flush([price]);
  });

  it('should record a new price observation', () => {
    service.create(1, { price: 1000000 }).subscribe((result) => {
      expect(result).toEqual(price);
    });

    const req = httpMock.expectOne('/api/players/1/prices');
    expect(req.request.method).toBe('POST');
    req.flush(price);
  });

  it('should refresh the price from the external source', () => {
    service.refresh(1).subscribe((result) => {
      expect(result).toEqual(price);
    });

    const req = httpMock.expectOne('/api/players/1/prices/refresh');
    expect(req.request.method).toBe('POST');
    req.flush(price);
  });
});
