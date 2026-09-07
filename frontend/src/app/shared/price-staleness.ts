/** Threshold (in milliseconds) after which a price observation is considered outdated. */
export const PRICE_STALE_THRESHOLD_MS = 24 * 60 * 60 * 1000;

/**
 * Returns true when `capturedAt` is more than 24h old (or missing), meaning the price shown to
 * the user may no longer reflect the current market value and should be flagged in the UI.
 */
export function isPriceStale(capturedAt: string | null | undefined): boolean {
  if (!capturedAt) {
    return false;
  }
  const capturedAtMs = new Date(capturedAt).getTime();
  if (Number.isNaN(capturedAtMs)) {
    return false;
  }
  return Date.now() - capturedAtMs > PRICE_STALE_THRESHOLD_MS;
}
