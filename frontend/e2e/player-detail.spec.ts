import { expect, test } from '@playwright/test';
import { mockApi } from './mocks';

test.describe('Player detail', () => {
  test.beforeEach(async ({ page }) => {
    await mockApi(page);
  });

  test('shows current price, trend and price history', async ({ page }) => {
    await page.goto('/players/1');

    await expect(page.getByText('David Soria')).toBeVisible();
    await expect(page.getByText('Getafe · Portero')).toBeVisible();

    // Latest price (most recent observation) and its trend label.
    await expect(page.getByText('4,200,000 €').first()).toBeVisible();
    await expect(page.getByText('Estable (subida)')).toBeVisible();

    // Full price history list contains both observations.
    await expect(page.getByText('4,185,000 €')).toBeVisible();
  });
});
