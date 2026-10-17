export type BuildSlotCategory =
  | 'cpu'
  | 'motherboard'
  | 'gpu'
  | 'cooler'
  | 'ram'
  | 'ssd'
  | 'psu'
  | 'cabinet';

export interface ProductDimensions {
  lengthMm: number;
  heightMm: number;
  widthMm: number;
}

export interface Product {
  id: string;
  name: string;
  brand: string;
  category: BuildSlotCategory;
  specs: string;
  modelNumber: string;
  tdpWatts?: number;
  socketType?: string;
  formFactor?: string;
  dimensions?: ProductDimensions;
  maxGpuLengthMm?: number;
  maxCoolerHeightMm?: number;
}

export interface Shop {
  id: string;
  name: string;
  area: string; // e.g. "Ritchie Street", "Mount Road", "Sowcarpet"
  landmark: string; // e.g. "Meeran Sahib Street", "Wallers Road", "Narayana Mudali Street"
  googleRating: number;
  whatsappNumber: string;
  verified: boolean;
  assemblyFee: number;
  phone?: string;
}

export interface Listing {
  productId: string;
  shopId: string;
  priceInr: number;
  stock: boolean;
  gstIncluded: boolean;
  warrantyYears?: number;
}

export type BuildSlotItem = {
  productId?: string;
  shopId?: string;
  price?: number;
};

export type BuildState = Record<BuildSlotCategory, BuildSlotItem>;

export interface ClearanceMetrics {
  isGpuClear: boolean;
  gpuMarginMm: number;
  isCoolerClear: boolean;
  coolerMarginMm: number;
  isSocketMatched: boolean;
  isFormFactorMatched: boolean;
  totalTdpWatts: number;
  headroomWatts: number;
  fitPercentage: number;
}

export type RootTabParamList = {
  'Rig Hub': undefined;
  'CAD Studio': undefined;
  'Blueprint': undefined;
  'Hunter': undefined;
  'Price Pulse': undefined;
};
