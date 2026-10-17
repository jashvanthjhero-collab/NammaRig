import { BuildState, BuildSlotCategory, Listing } from '../types';

/**
 * Formats a number to Indian Rupee currency format (e.g. ₹59,322).
 */
export function formatInr(amount: number): string {
  if (isNaN(amount) || amount === null || amount === undefined) {
    return '₹0';
  }
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    maximumFractionDigits: 0,
  }).format(amount);
}

export interface GstBreakup {
  basePrice: number;
  gstRate: number; // 18%
  cgstAmount: number; // 9%
  sgstAmount: number; // 9%
  totalGst: number;
  totalInclusive: number;
}

/**
 * Calculates standard 18% GST (9% CGST + 9% SGST) breakup from an inclusive retail price.
 */
export function calculateGstBreakup(totalWithGst: number): GstBreakup {
  if (totalWithGst <= 0) {
    return {
      basePrice: 0,
      gstRate: 18,
      cgstAmount: 0,
      sgstAmount: 0,
      totalGst: 0,
      totalInclusive: 0,
    };
  }

  // Inclusive GST math: Total = Base * 1.18 => Base = Total / 1.18
  const basePrice = Math.round(totalWithGst / 1.18);
  const totalGst = totalWithGst - basePrice;
  const halfGst = Math.round(totalGst / 2);

  return {
    basePrice,
    gstRate: 18,
    cgstAmount: halfGst,
    sgstAmount: totalGst - halfGst,
    totalGst,
    totalInclusive: totalWithGst,
  };
}

export interface ShopSpread {
  minPrice: number;
  maxPrice: number;
  averagePrice: number;
  spreadSavings: number;
  cheapestShopId: string;
}

/**
 * Computes price differences (spread) for a component across Chennai dealer listings.
 */
export function calculateShopSpread(listings: Listing[]): ShopSpread | null {
  if (!listings || listings.length === 0) return null;

  const validListings = listings.filter((l) => l.priceInr > 0);
  if (validListings.length === 0) return null;

  const prices = validListings.map((l) => l.priceInr);
  const minPrice = Math.min(...prices);
  const maxPrice = Math.max(...prices);
  const averagePrice = Math.round(
    prices.reduce((sum, p) => sum + p, 0) / prices.length
  );
  const spreadSavings = maxPrice - minPrice;
  const cheapestListing = validListings.find((l) => l.priceInr === minPrice);

  return {
    minPrice,
    maxPrice,
    averagePrice,
    spreadSavings,
    cheapestShopId: cheapestListing?.shopId ?? '',
  };
}

export interface BuildTotals {
  totalInr: number;
  basePriceExGst: number;
  totalGst: number;
  marketAverageInr: number;
  totalSavingsInr: number;
  filledSlotsCount: number;
}

/**
 * Calculates total rig cost, inclusive GST split, and estimated Ritchie Street savings.
 */
export function calculateBuildTotals(
  build: BuildState,
  allListings: Listing[] = []
): BuildTotals {
  const slotKeys = Object.keys(build) as BuildSlotCategory[];
  let totalInr = 0;
  let filledSlotsCount = 0;
  let marketAverageInr = 0;

  for (const category of slotKeys) {
    const slot = build[category];
    if (slot && slot.productId && slot.price && slot.price > 0) {
      totalInr += slot.price;
      filledSlotsCount += 1;

      // Find all listings for this product to compute market average
      const productListings = allListings.filter(
        (l) => l.productId === slot.productId
      );
      if (productListings.length > 0) {
        const avg =
          productListings.reduce((acc, curr) => acc + curr.priceInr, 0) /
          productListings.length;
        marketAverageInr += Math.round(avg);
      } else {
        // Fallback default markup comparison (+7%)
        marketAverageInr += Math.round(slot.price * 1.07);
      }
    }
  }

  const gstBreakup = calculateGstBreakup(totalInr);
  const totalSavingsInr =
    marketAverageInr > totalInr ? marketAverageInr - totalInr : Math.round(totalInr * 0.05);

  return {
    totalInr,
    basePriceExGst: gstBreakup.basePrice,
    totalGst: gstBreakup.totalGst,
    marketAverageInr,
    totalSavingsInr,
    filledSlotsCount,
  };
}
