import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, provideRouter, Router } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { PlayerForm } from './player-form';
import { Player } from '../../core/models/player';
import { Team } from '../../core/models/team';

describe('PlayerForm', () => {
  let component: PlayerForm;
  let fixture: ComponentFixture<PlayerForm>;
  let httpMock: HttpTestingController;

  const player: Player = {
    id: 1,
    name: 'Alpha Striker',
    teamId: 1,
    teamName: 'FC Alpha',
    position: 'DELANTERO',
    externalId: 'ff-123',
    createdAt: '2024-01-01T00:00:00Z',
  };

  const teams: Team[] = [
    { id: 1, name: 'FC Alpha' },
    { id: 2, name: 'FC Test' },
  ];

  async function setup(paramMap: Record<string, string>) {
    await TestBed.configureTestingModule({
      imports: [PlayerForm, NoopAnimationsModule],
      providers: [
        provideRouter([{ path: '**', children: [] }]),
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: { paramMap: of(convertToParamMap(paramMap)) },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PlayerForm);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    httpMock.expectOne('/api/teams').flush(teams);
  }

  it('should start empty in create mode', async () => {
    await setup({});
    fixture.detectChanges();

    expect(component.isEditMode()).toBe(false);
    expect(component.name()).toBe('');
    expect(component.position()).toBe('DEFENSA');
    expect(component.canSave()).toBe(false);
  });

  it('should create a new player', async () => {
    await setup({});
    fixture.detectChanges();

    component.name.set('New Player');
    component.teamId.set(2);
    component.position.set('PORTERO');
    expect(component.canSave()).toBe(true);

    component.save();

    const req = httpMock.expectOne('/api/players');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      name: 'New Player',
      teamId: 2,
      position: 'PORTERO',
      externalId: null,
    });
    req.flush({ ...player, id: 2, name: 'New Player' });
  });

  it('should load an existing player in edit mode', async () => {
    await setup({ id: '1' });
    fixture.detectChanges();

    httpMock.expectOne('/api/players/1').flush(player);
    await fixture.whenStable();
    fixture.detectChanges();

    expect(component.isEditMode()).toBe(true);
    expect(component.name()).toBe('Alpha Striker');
    expect(component.teamId()).toBe(1);
    expect(component.externalId()).toBe('ff-123');
  });

  it('should update an existing player', async () => {
    await setup({ id: '1' });
    fixture.detectChanges();

    httpMock.expectOne('/api/players/1').flush(player);
    await fixture.whenStable();

    component.position.set('MEDIO');
    component.save();

    const req = httpMock.expectOne('/api/players/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body.position).toBe('MEDIO');
    req.flush({ ...player, position: 'MEDIO' });
  });

  it('should navigate to the player detail page after saving', async () => {
    await setup({});
    fixture.detectChanges();
    const router = TestBed.inject(Router);
    const navigateSpy = vi.spyOn(router, 'navigate').mockResolvedValue(true);

    component.name.set('New Player');
    component.save();

    const req = httpMock.expectOne('/api/players');
    req.flush({ ...player, id: 2, name: 'New Player' });

    expect(navigateSpy).toHaveBeenCalledWith(['/players', 2]);
  });
});
