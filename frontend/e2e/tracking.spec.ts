import { expect, test } from '@playwright/test';
import { mockApi } from './mocks';

test.describe('Tracking management', () => {
  test.beforeEach(async ({ page }) => {
    await mockApi(page);
  });

  test('creates tracking information for a player from the detail page', async ({ page }) => {
    await page.goto('/players/1');

    await page.getByLabel('Cláusula (€)').fill('6000000');
    await page.getByLabel('Notas').fill('Portero en forma');

    await page.getByRole('button', { name: 'Guardar seguimiento' }).click();

    await expect(page.getByText('Seguimiento guardado')).toBeVisible();
  });

  test('lists tracked players in the watchlist screen', async ({ page }) => {
    await page.goto('/tracked');

    await expect(page.getByRole('link', { name: /David Soria/ })).toBeVisible();
    await expect(page.getByText('En seguimiento')).toBeVisible();
  });
});
