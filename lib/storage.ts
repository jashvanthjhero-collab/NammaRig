import { BuildState, Product, Shop, Listing } from '../types';
import { SEED_PRODUCTS, CHENNAI_SHOPS, MOCK_LISTINGS } from '../data/mockData';

// Storage keys
const STORAGE_KEYS = {
  CURRENT_BUILD: '@nammarig:current_build_v1',
  CACHED_CATALOG: '@nammarig:cached_catalog_v1',
  SAVED_BUILDS: '@nammarig:saved_builds_v1',
  LOCATION: '@nammarig:selected_location_v1',
  OFFLINE_META: '@nammarig:offline_meta_v1',
};

// In-Memory Fallback Map (guarantees zero crash in any JS/Native runtime environment)
const memoryStorage = new Map<string, string>();

/**
 * Universal Local Storage Adapter
 * Safely wraps AsyncStorage or localStorage or in-memory fallback.
 */
class LocalStorageAdapter {
  private async getStorageEngine() {
    try {
      // Try react-native async storage if available
      const AsyncStorageModule = require('@react-native-async-storage/async-storage');
      return AsyncStorageModule.default || AsyncStorageModule;
    } catch {
      // Fallback to browser/web localStorage if present
      if (typeof window !== 'undefined' && window.localStorage) {
        return {
          getItem: async (key: string) => window.localStorage.getItem(key),
          setItem: async (key: string, value: string) =>
            window.localStorage.setItem(key, value),
          removeItem: async (key: string) => window.localStorage.removeItem(key),
        };
      }
      // Otherwise use in-memory fallback
      return {
        getItem: async (key: string) => memoryStorage.get(key) ?? null,
        setItem: async (key: string, value: string) => {
          memoryStorage.set(key, value);
        },
        removeItem: async (key: string) => {
          memoryStorage.delete(key);
        },
      };
    }
  }

  async getItem(key: string): Promise<string | null> {
    try {
      const engine = await this.getStorageEngine();
      return await engine.getItem(key);
    } catch (err) {
      console.warn(`[LocalStorage] Error getting item ${key}:`, err);
      return memoryStorage.get(key) ?? null;
    }
  }

  async setItem(key: string, value: string): Promise<void> {
    try {
      const engine = await this.getStorageEngine();
      await engine.setItem(key, value);
      memoryStorage.set(key, value);
    } catch (err) {
      console.warn(`[LocalStorage] Error setting item ${key}:`, err);
      memoryStorage.set(key, value);
    }
  }

  async removeItem(key: string): Promise<void> {
    try {
      const engine = await this.getStorageEngine();
      await engine.removeItem(key);
      memoryStorage.delete(key);
    } catch (err) {
      console.warn(`[LocalStorage] Error removing item ${key}:`, err);
      memoryStorage.delete(key);
    }
  }
}

export const localStorage = new LocalStorageAdapter();

export interface OfflineCatalogCache {
  products: Product[];
  shops: Shop[];
  listings: Listing[];
  cachedAt: number;
  version: string;
}

export interface SavedBuildRecord {
  id: string;
  name: string;
  timestamp: number;
  build: BuildState;
}

/**
 * Persists the user's current 8-slot rig build to local storage.
 */
export async function saveCurrentBuild(build: BuildState): Promise<void> {
  try {
    const json = JSON.stringify(build);
    await localStorage.setItem(STORAGE_KEYS.CURRENT_BUILD, json);
  } catch (err) {
    console.error('Failed to save current build:', err);
  }
}

/**
 * Loads the user's active rig build from local storage.
 */
export async function loadCurrentBuild(): Promise<BuildState | null> {
  try {
    const json = await localStorage.getItem(STORAGE_KEYS.CURRENT_BUILD);
    if (!json) return null;
    return JSON.parse(json) as BuildState;
  } catch (err) {
    console.error('Failed to load current build:', err);
    return null;
  }
}

/**
 * Caches the entire catalog (products, shops, listings) into local storage
 * for zero-latency, full offline access in Ritchie Street's crowded alleys.
 */
export async function cacheCatalogOffline(
  products: Product[] = SEED_PRODUCTS,
  shops: Shop[] = CHENNAI_SHOPS,
  listings: Listing[] = MOCK_LISTINGS
): Promise<OfflineCatalogCache> {
  const cacheData: OfflineCatalogCache = {
    products,
    shops,
    listings,
    cachedAt: Date.now(),
    version: '1.0.0-chennai',
  };

  try {
    await localStorage.setItem(
      STORAGE_KEYS.CACHED_CATALOG,
      JSON.stringify(cacheData)
    );
    await localStorage.setItem(
      STORAGE_KEYS.OFFLINE_META,
      JSON.stringify({
        isOfflineReady: true,
        lastCachedAt: new Date().toISOString(),
      })
    );
  } catch (err) {
    console.error('Failed to cache catalog offline:', err);
  }

  return cacheData;
}

/**
 * Retrieves the cached offline catalog. If not cached yet, initializes
 * it with SEED_PRODUCTS, CHENNAI_SHOPS, and MOCK_LISTINGS.
 */
export async function getCachedCatalogOffline(): Promise<OfflineCatalogCache> {
  try {
    const json = await localStorage.getItem(STORAGE_KEYS.CACHED_CATALOG);
    if (json) {
      return JSON.parse(json) as OfflineCatalogCache;
    }
  } catch (err) {
    console.warn('Failed to parse cached catalog, regenerating...', err);
  }

  // Pre-cache on first offline startup
  return await cacheCatalogOffline();
}

/**
 * Saves a named custom rig build into the offline saved rigs collection.
 */
export async function saveCustomRigBuild(
  name: string,
  build: BuildState
): Promise<SavedBuildRecord> {
  const records = await getSavedRigBuilds();
  const newRecord: SavedBuildRecord = {
    id: `rig-${Date.now()}-${Math.random().toString(36).substr(2, 4)}`,
    name: name.trim() || `Ritchie Rig #${records.length + 1}`,
    timestamp: Date.now(),
    build,
  };

  const updatedRecords = [newRecord, ...records];
  await localStorage.setItem(
    STORAGE_KEYS.SAVED_BUILDS,
    JSON.stringify(updatedRecords)
  );
  return newRecord;
}

/**
 * Retrieves all saved custom builds.
 */
export async function getSavedRigBuilds(): Promise<SavedBuildRecord[]> {
  try {
    const json = await localStorage.getItem(STORAGE_KEYS.SAVED_BUILDS);
    if (!json) return [];
    return JSON.parse(json) as SavedBuildRecord[];
  } catch (err) {
    console.error('Failed to get saved rig builds:', err);
    return [];
  }
}

/**
 * Removes a saved build by id.
 */
export async function deleteSavedRigBuild(id: string): Promise<void> {
  try {
    const records = await getSavedRigBuilds();
    const filtered = records.filter((r) => r.id !== id);
    await localStorage.setItem(
      STORAGE_KEYS.SAVED_BUILDS,
      JSON.stringify(filtered)
    );
  } catch (err) {
    console.error('Failed to delete saved rig build:', err);
  }
}
