import { expect, test } from '@playwright/test';
import { mockApi, players } from './mocks';

test.describe('Player list', () => {
  test.beforeEach(async ({ page }) => {
    await mockApi(page);
  });

  test('lists players and navigates to a player detail', async ({ page }) => {
    await page.goto('/players');

    await expect(page.getByRole('link', { name: /David Soria/ })).toBeVisible();
    await expect(page.getByRole('link', { name: /Djené/ })).toBeVisible();

    await page.getByRole('link', { name: /David Soria/ }).click();

    await expect(page).toHaveURL(/\/players\/1$/);
    await expect(page.getByText(players[0].name)).toBeVisible();
  });

  test('filters players by search term', async ({ page }) => {
    await page.goto('/players');

    await page.getByLabel('Buscar jugador').fill('Djené');

    await expect(page.getByRole('link', { name: /Djené/ })).toBeVisible();
    await expect(page.getByRole('link', { name: /David Soria/ })).toHaveCount(0);
  });
});
