import { defineConfig, devices } from '@playwright/test';

/**
 * Playwright configuration for the Fantasy Tracker frontend E2E smoke tests.
 *
 * These tests exercise the Angular application through a real browser while
 * mocking the backend HTTP API (see e2e/mocks.ts), so they run standalone
 * without requiring the Spring Boot backend or a PostgreSQL database.
 */
export default defineConfig({
  testDir: './e2e',
  fullyParallel: true,
  forbidOnly: !!process.env['CI'],
  retries: process.env['CI'] ? 2 : 0,
  reporter: [['list']],
  use: {
    baseURL: 'http://localhost:4300',
    trace: 'on-first-retry',
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
  webServer: {
    command: 'npx ng serve --port 4300 --configuration development',
    url: 'http://localhost:4300',
    reuseExistingServer: !process.env['CI'],
    timeout: 120_000,
  },
});
