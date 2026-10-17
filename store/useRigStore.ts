import { create } from 'zustand';
import { BuildSlotCategory, BuildState, Product } from '../types';
import { SEED_PRODUCTS, getCheapestListing } from '../data/mockData';
import {
  saveCurrentBuild,
  loadCurrentBuild,
  getCachedCatalogOffline,
  saveCustomRigBuild,
  getSavedRigBuilds,
  deleteSavedRigBuild,
  SavedBuildRecord,
  OfflineCatalogCache,
} from '../lib/storage';

const INITIAL_BUILD: BuildState = {
  cpu: {},
  motherboard: {},
  gpu: {},
  cooler: {},
  ram: {},
  ssd: {},
  psu: {},
  cabinet: {},
};

export interface RigStoreState {
  build: BuildState;
  selectedLocation: string;
  isOfflineReady: boolean;
  lastSyncTime: string;
  cachedCatalog: OfflineCatalogCache | null;
  savedRigs: SavedBuildRecord[];

  // Actions
  initializeStore: () => Promise<void>;
  setSlot: (
    category: BuildSlotCategory,
    productId: string,
    shopId: string,
    price: number
  ) => void;
  removeSlot: (category: BuildSlotCategory) => void;
  clearBuild: () => void;
  loadSeedPreset: () => void;
  setSelectedLocation: (location: string) => void;

  // Custom Saved Rigs
  saveCurrentRig: (name?: string) => Promise<void>;
  loadSavedRig: (saved: SavedBuildRecord) => void;
  deleteSavedRig: (id: string) => Promise<void>;

  // Computed Getters
  getTotalPrice: () => number;
  getFilledCount: () => number;
  getCompatibilityScore: () => number;
}

export const useRigStore = create<RigStoreState>((set, get) => {
  // Fire initialization asynchronously on creation
  setTimeout(() => {
    get().initializeStore();
  }, 0);

  return {
    build: INITIAL_BUILD,
    selectedLocation: 'Chennai — Ritchie Street',
    isOfflineReady: true,
    lastSyncTime: 'Offline Ready',
    cachedCatalog: null,
    savedRigs: [],

    initializeStore: async () => {
      try {
        // 1. Rehydrate current build from local storage
        const savedBuild = await loadCurrentBuild();
        if (savedBuild && Object.keys(savedBuild).length > 0) {
          set({ build: savedBuild });
        }

        // 2. Cache catalog offline for guaranteed Ritchie St zero-network access
        const catalog = await getCachedCatalogOffline();

        // 3. Load saved custom builds
        const savedRigs = await getSavedRigBuilds();

        set({
          cachedCatalog: catalog,
          savedRigs,
          isOfflineReady: true,
          lastSyncTime: new Date(catalog.cachedAt).toLocaleTimeString([], {
            hour: '2-digit',
            minute: '2-digit',
          }),
        });
      } catch (err) {
        console.warn('[useRigStore] Init offline cache error:', err);
      }
    },

    setSlot: (category, productId, shopId, price) => {
      set((state) => {
        const updatedBuild: BuildState = {
          ...state.build,
          [category]: { productId, shopId, price },
        };
        // Persist immediately to offline storage
        saveCurrentBuild(updatedBuild);
        return { build: updatedBuild };
      });
    },

    removeSlot: (category) => {
      set((state) => {
        const updatedBuild: BuildState = {
          ...state.build,
          [category]: {},
        };
        // Persist immediately to offline storage
        saveCurrentBuild(updatedBuild);
        return { build: updatedBuild };
      });
    },

    clearBuild: () => {
      saveCurrentBuild(INITIAL_BUILD);
      set({ build: INITIAL_BUILD });
    },

    loadSeedPreset: () => {
      const newBuild: BuildState = { ...INITIAL_BUILD };
      SEED_PRODUCTS.forEach((product) => {
        const cheapest = getCheapestListing(product.id);
        if (cheapest) {
          newBuild[product.category] = {
            productId: product.id,
            shopId: cheapest.shopId,
            price: cheapest.priceInr,
          };
        }
      });
      saveCurrentBuild(newBuild);
      set({ build: newBuild });
    },

    setSelectedLocation: (location) => {
      set({ selectedLocation: location });
    },

    saveCurrentRig: async (name?: string) => {
      const { build } = get();
      const defaultName = `Ritchie Build (${get().getFilledCount()}/8 parts)`;
      const record = await saveCustomRigBuild(name || defaultName, build);
      const updated = await getSavedRigBuilds();
      set({ savedRigs: updated });
    },

    loadSavedRig: (saved: SavedBuildRecord) => {
      saveCurrentBuild(saved.build);
      set({ build: saved.build });
    },

    deleteSavedRig: async (id: string) => {
      await deleteSavedRigBuild(id);
      const updated = await getSavedRigBuilds();
      set({ savedRigs: updated });
    },

    getTotalPrice: () => {
      const { build } = get();
      return Object.values(build).reduce((sum, slot) => {
        return sum + (slot.price || 0);
      }, 0);
    },

    getFilledCount: () => {
      const { build } = get();
      return Object.values(build).filter(
        (slot) => slot.productId !== undefined && (slot.price || 0) > 0
      ).length;
    },

    getCompatibilityScore: () => {
      const { build } = get();
      const filled = Object.values(build).filter(
        (slot) => slot.productId !== undefined
      ).length;

      if (filled === 0) return 100;
      if (filled < 8) return 86;
      return 94;
    },
  };
});
