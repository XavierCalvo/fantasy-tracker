import { Page } from '@playwright/test';

/**
 * Fixture data and route mocking helpers used by the E2E smoke tests.
 *
 * The tests intercept every `/api/**` call so they exercise the real Angular
 * application (routing, forms, Angular Material components) without
 * depending on the Spring Boot backend or a PostgreSQL database.
 */

export const players = [
  {
    id: 1,
    name: 'David Soria',
    team: 'Getafe',
    position: 'Portero',
    externalId: 'david-soria',
    createdAt: '2026-09-06T20:11:57.498572Z',
  },
  {
    id: 2,
    name: 'Djené',
    team: 'Getafe',
    position: 'DEF',
    externalId: 'dakonam-djene',
    createdAt: '2026-09-06T20:17:48.866168Z',
  },
];

export const playerPrices = [
  {
    id: 20,
    playerId: 1,
    price: 4_200_000,
    trendAmount: 15_000,
    trendType: 'STABLE_UP',
    capturedAt: '2026-09-07T08:00:00Z',
  },
  {
    id: 10,
    playerId: 1,
    price: 4_185_000,
    trendAmount: null,
    trendType: null,
    capturedAt: '2026-09-06T08:00:00Z',
  },
];

export const trackedPlayerListItems = [
  {
    id: 100,
    playerId: 1,
    playerName: 'David Soria',
    playerTeam: 'Getafe',
    playerPosition: 'Portero',
    status: 'WATCHING',
    clause: 6_000_000,
    clauseReleaseDate: '2026-12-01',
    notes: 'Portero en forma',
    createdAt: '2026-09-06T20:12:00Z',
    updatedAt: '2026-09-06T20:12:00Z',
  },
];

/**
 * Registers `page.route` handlers for every backend endpoint used by the
 * frontend, backed by the fixtures above. Call this before navigating.
 */
export async function mockApi(page: Page): Promise<void> {
  await page.route('**/api/players', async (route) => {
    if (route.request().method() === 'GET') {
      await route.fulfill({ json: players });
      return;
    }
    await route.continue();
  });

  await page.route('**/api/players/1', async (route) => {
    await route.fulfill({ json: players[0] });
  });

  await page.route('**/api/players/1/prices', async (route) => {
    await route.fulfill({ json: playerPrices });
  });

  await page.route('**/api/players/1/tracking', async (route) => {
    const method = route.request().method();
    if (method === 'GET') {
      await route.fulfill({ status: 404, json: { message: 'Not found' } });
      return;
    }
    if (method === 'POST') {
      const body = route.request().postDataJSON();
      await route.fulfill({
        json: {
          id: 100,
          playerId: 1,
          status: body.status,
          clause: body.clause,
          clauseReleaseDate: body.clauseReleaseDate,
          notes: body.notes,
          createdAt: '2026-09-07T18:00:00Z',
          updatedAt: '2026-09-07T18:00:00Z',
        },
      });
      return;
    }
    await route.continue();
  });

  await page.route('**/api/tracking', async (route) => {
    if (route.request().method() === 'GET') {
      await route.fulfill({ json: trackedPlayerListItems });
      return;
    }
    await route.continue();
  });
}
