import { Product, Shop, Listing } from '../types';

export const CHENNAI_SHOPS: Shop[] = [
  {
    id: 'delta-peripherals',
    name: 'Delta Peripherals',
    area: 'Ritchie Street',
    landmark: 'Meeran Sahib St, Ritchie St',
    googleRating: 4.8,
    whatsappNumber: '+919840223344',
    verified: true,
    assemblyFee: 750,
    phone: '+91 44 2841 3345',
  },
  {
    id: 'challenger-computers',
    name: 'Challenger Computers',
    area: 'Ritchie Street',
    landmark: 'Wallers Road, Mount Road Junction',
    googleRating: 4.6,
    whatsappNumber: '+919840334455',
    verified: true,
    assemblyFee: 600,
    phone: '+91 44 2841 8912',
  },
  {
    id: 'supreme-computers',
    name: 'Supreme Computers',
    area: 'Ritchie Street',
    landmark: 'Meeran Sahib Street',
    googleRating: 4.5,
    whatsappNumber: '+919840445566',
    verified: true,
    assemblyFee: 500,
    phone: '+91 44 2841 4422',
  },
  {
    id: 'marina-micro',
    name: 'Marina Micro Systems',
    area: 'Mount Road',
    landmark: 'Opp. LIC Building, Anna Salai',
    googleRating: 4.7,
    whatsappNumber: '+919840667788',
    verified: true,
    assemblyFee: 800,
    phone: '+91 44 2852 1100',
  },
  {
    id: 'oasis-it',
    name: 'Oasis IT Hub',
    area: 'T. Nagar',
    landmark: 'Near Panagal Park, Usman Road',
    googleRating: 4.6,
    whatsappNumber: '+919840112233',
    verified: true,
    assemblyFee: 900,
    phone: '+91 44 2434 5566',
  },
  {
    id: 'velachery-pc-point',
    name: 'Velachery PC Point',
    area: 'Velachery',
    landmark: '100 Feet Bypass Road, Near Grand Square',
    googleRating: 4.4,
    whatsappNumber: '+919840778899',
    verified: false,
    assemblyFee: 500,
    phone: '+91 44 2259 8899',
  },
];

export const SEED_PRODUCTS: Product[] = [
  {
    id: 'cpu-9950x3d',
    name: 'AMD Ryzen 9 9950X3D',
    brand: 'AMD',
    category: 'cpu',
    specs: '16 Cores / 32 Threads • 5.7GHz Boost • 144MB 3D V-Cache • AM5',
    modelNumber: '100-100001234WOF',
    tdpWatts: 120,
    socketType: 'AM5',
  },
  {
    id: 'gpu-rtx-5080',
    name: 'NVIDIA GeForce RTX 5080 16GB',
    brand: 'NVIDIA / Zotac',
    category: 'gpu',
    specs: '16GB GDDR7 • Blackwell Arch • DLSS 4 • 304mm Length',
    modelNumber: 'ZT-B50800J-10P',
    tdpWatts: 360,
    dimensions: { lengthMm: 304, heightMm: 137, widthMm: 62 },
  },
  {
    id: 'mobo-x870e-tomahawk',
    name: 'MSI MAG X870E Tomahawk WiFi',
    brand: 'MSI',
    category: 'motherboard',
    specs: 'ATX • AM5 • DDR5 8400+ MT/s • PCIe 5.0 • 4x M.2 • Wi-Fi 7',
    modelNumber: 'MAG-X870E-TOMAHAWK',
    tdpWatts: 45,
    socketType: 'AM5',
    formFactor: 'ATX',
  },
  {
    id: 'cooler-kraken-360',
    name: 'NZXT Kraken 360 RGB AIO Cooler',
    brand: 'NZXT',
    category: 'cooler',
    specs: '360mm Radiator • 1.54" LCD Display • 3x F120 RGB Core Fans',
    modelNumber: 'RL-KR360-B1',
    tdpWatts: 15,
    dimensions: { lengthMm: 394, heightMm: 52, widthMm: 121 },
  },
  {
    id: 'ram-trident-z5-32gb',
    name: 'G.Skill Trident Z5 Neo RGB 32GB DDR5',
    brand: 'G.Skill',
    category: 'ram',
    specs: '32GB (2x16GB) • DDR5-6000MHz CL30-36-36-96 • AMD EXPO',
    modelNumber: 'F5-6000J3036F16GX2-TZ5NR',
    tdpWatts: 12,
  },
  {
    id: 'ssd-990-pro-2tb',
    name: 'Samsung 990 Pro 2TB NVMe PCIe 4.0 SSD',
    brand: 'Samsung',
    category: 'ssd',
    specs: '2TB • Up to 7,450 MB/s Read • 6,900 MB/s Write • 2GB LPDDR4 Cache',
    modelNumber: 'MZ-V9P2T0B/AM',
    tdpWatts: 8,
  },
  {
    id: 'psu-rm850x',
    name: 'Corsair RM850x 850W 80+ Gold Fully Modular',
    brand: 'Corsair',
    category: 'psu',
    specs: '850W • 80 Plus Gold • ATX 3.1 & PCIe 5.1 Native • Low-Noise Fan',
    modelNumber: 'CP-9020252-IN',
    tdpWatts: 850, // Wattage rating
  },
  {
    id: 'cabinet-o11-dynamic-evo',
    name: 'Lian Li O11 Dynamic EVO Mid Tower',
    brand: 'Lian Li',
    category: 'cabinet',
    specs: 'Dual-Chamber Tempered Glass • Max GPU 422mm • Max Cooler 167mm',
    modelNumber: 'O11DEW',
    maxGpuLengthMm: 422,
    maxCoolerHeightMm: 167,
  },
  {
    id: 'cpu-i7-14700k',
    name: 'Intel Core i7-14700K Processor',
    brand: 'Intel',
    category: 'cpu',
    specs: '20 Cores (8P+12E) / 28 Threads • 5.6GHz Turbo • 33MB Cache • LGA1700',
    modelNumber: 'BX8071514700K',
    tdpWatts: 125,
    socketType: 'LGA1700',
  },
  {
    id: 'gpu-rtx-4070-super',
    name: 'ASUS Dual GeForce RTX 4070 Super 12GB',
    brand: 'ASUS',
    category: 'gpu',
    specs: '12GB GDDR6X • Axial-tech Fans • 267mm Length • 220W TDP',
    modelNumber: 'DUAL-RTX4070S-O12G',
    tdpWatts: 220,
    dimensions: { lengthMm: 267, heightMm: 134, widthMm: 51 },
  },
  {
    id: 'mobo-b760-plus',
    name: 'ASUS TUF Gaming B760-Plus WiFi',
    brand: 'ASUS',
    category: 'motherboard',
    specs: 'ATX • LGA1700 • DDR5 7200+ • 3x M.2 • PCIe 5.0 • Wi-Fi 6',
    modelNumber: 'TUF-GAMING-B760-PLUS-WIFI',
    tdpWatts: 35,
    socketType: 'LGA1700',
    formFactor: 'ATX',
  },
  {
    id: 'cooler-ak620',
    name: 'DeepCool AK620 Dual-Tower CPU Cooler',
    brand: 'DeepCool',
    category: 'cooler',
    specs: 'Dual 120mm PWM Fans • 6 Copper Heatpipes • 160mm Height • 260W TDP',
    modelNumber: 'R-AK620-BKNNMT-G',
    tdpWatts: 5,
    dimensions: { lengthMm: 138, heightMm: 160, widthMm: 129 },
  },
  {
    id: 'ram-vengeance-32gb',
    name: 'Corsair Vengeance 32GB (2x16GB) DDR5 6000',
    brand: 'Corsair',
    category: 'ram',
    specs: '32GB Kit • DDR5-6000MHz CL36 • Intel XMP 3.0 & AMD EXPO',
    modelNumber: 'CMK32GX5M2B6000C36',
    tdpWatts: 10,
  },
  {
    id: 'ssd-sn580-1tb',
    name: 'WD Blue SN580 1TB NVMe PCIe 4.0 SSD',
    brand: 'Western Digital',
    category: 'ssd',
    specs: '1TB • Up to 4,150 MB/s Read • nCache 4.0 • M.2 2280',
    modelNumber: 'WDS100T3B0E',
    tdpWatts: 5,
  },
  {
    id: 'psu-msi-a650bn',
    name: 'MSI MAG A650BN 650W 80+ Bronze',
    brand: 'MSI',
    category: 'psu',
    specs: '650W • 80 Plus Bronze • DC-to-DC Design • 120mm Silent Fan',
    modelNumber: 'MAG-A650BN',
    tdpWatts: 650,
  },
  {
    id: 'cabinet-air-903',
    name: 'Montech AIR 903 MAX E-ATX Mid Tower',
    brand: 'Montech',
    category: 'cabinet',
    specs: 'High Airflow Mesh • 4x 140mm PWM Fans • Max GPU 400mm • Cooler 180mm',
    modelNumber: 'AIR903MAX-B',
    maxGpuLengthMm: 400,
    maxCoolerHeightMm: 180,
  },
];

export const MOCK_LISTINGS: Listing[] = [
  // --- AMD Ryzen 9 9950X3D (CPU) ---
  {
    productId: 'cpu-9950x3d',
    shopId: 'delta-peripherals',
    priceInr: 66500,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'cpu-9950x3d',
    shopId: 'challenger-computers',
    priceInr: 67200,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'cpu-9950x3d',
    shopId: 'supreme-computers',
    priceInr: 66900,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'cpu-9950x3d',
    shopId: 'marina-micro',
    priceInr: 68500,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'cpu-9950x3d',
    shopId: 'oasis-it',
    priceInr: 67900,
    stock: false,
    gstIncluded: true,
    warrantyYears: 3,
  },

  // --- NVIDIA GeForce RTX 5080 16GB (GPU) ---
  {
    productId: 'gpu-rtx-5080',
    shopId: 'delta-peripherals',
    priceInr: 124500,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'gpu-rtx-5080',
    shopId: 'challenger-computers',
    priceInr: 123900, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'gpu-rtx-5080',
    shopId: 'supreme-computers',
    priceInr: 125800,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'gpu-rtx-5080',
    shopId: 'marina-micro',
    priceInr: 127000,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },

  // --- MSI MAG X870E Tomahawk WiFi (Motherboard) ---
  {
    productId: 'mobo-x870e-tomahawk',
    shopId: 'delta-peripherals',
    priceInr: 31900,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'mobo-x870e-tomahawk',
    shopId: 'supreme-computers',
    priceInr: 31450, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'mobo-x870e-tomahawk',
    shopId: 'challenger-computers',
    priceInr: 32200,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'mobo-x870e-tomahawk',
    shopId: 'oasis-it',
    priceInr: 32800,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },

  // --- NZXT Kraken 360 RGB AIO Cooler ---
  {
    productId: 'cooler-kraken-360',
    shopId: 'delta-peripherals',
    priceInr: 17400,
    stock: true,
    gstIncluded: true,
    warrantyYears: 6,
  },
  {
    productId: 'cooler-kraken-360',
    shopId: 'marina-micro',
    priceInr: 16990, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 6,
  },
  {
    productId: 'cooler-kraken-360',
    shopId: 'challenger-computers',
    priceInr: 17650,
    stock: true,
    gstIncluded: true,
    warrantyYears: 6,
  },

  // --- G.Skill Trident Z5 Neo RGB 32GB DDR5 ---
  {
    productId: 'ram-trident-z5-32gb',
    shopId: 'supreme-computers',
    priceInr: 11400, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 10,
  },
  {
    productId: 'ram-trident-z5-32gb',
    shopId: 'delta-peripherals',
    priceInr: 11650,
    stock: true,
    gstIncluded: true,
    warrantyYears: 10,
  },
  {
    productId: 'ram-trident-z5-32gb',
    shopId: 'oasis-it',
    priceInr: 11900,
    stock: true,
    gstIncluded: true,
    warrantyYears: 10,
  },
  {
    productId: 'ram-trident-z5-32gb',
    shopId: 'velachery-pc-point',
    priceInr: 12200,
    stock: true,
    gstIncluded: true,
    warrantyYears: 10,
  },

  // --- Samsung 990 Pro 2TB NVMe SSD ---
  {
    productId: 'ssd-990-pro-2tb',
    shopId: 'challenger-computers',
    priceInr: 16950, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 5,
  },
  {
    productId: 'ssd-990-pro-2tb',
    shopId: 'delta-peripherals',
    priceInr: 17200,
    stock: true,
    gstIncluded: true,
    warrantyYears: 5,
  },
  {
    productId: 'ssd-990-pro-2tb',
    shopId: 'marina-micro',
    priceInr: 17450,
    stock: true,
    gstIncluded: true,
    warrantyYears: 5,
  },

  // --- Corsair RM850x 850W Gold PSU ---
  {
    productId: 'psu-rm850x',
    shopId: 'delta-peripherals',
    priceInr: 12100, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 10,
  },
  {
    productId: 'psu-rm850x',
    shopId: 'supreme-computers',
    priceInr: 12350,
    stock: true,
    gstIncluded: true,
    warrantyYears: 10,
  },
  {
    productId: 'psu-rm850x',
    shopId: 'challenger-computers',
    priceInr: 12250,
    stock: true,
    gstIncluded: true,
    warrantyYears: 10,
  },

  // --- Lian Li O11 Dynamic EVO Cabinet ---
  {
    productId: 'cabinet-o11-dynamic-evo',
    shopId: 'delta-peripherals',
    priceInr: 14800,
    stock: true,
    gstIncluded: true,
    warrantyYears: 1,
  },
  {
    productId: 'cabinet-o11-dynamic-evo',
    shopId: 'challenger-computers',
    priceInr: 14500, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 1,
  },
  {
    productId: 'cabinet-o11-dynamic-evo',
    shopId: 'oasis-it',
    priceInr: 15200,
    stock: true,
    gstIncluded: true,
    warrantyYears: 1,
  },

  // --- Intel Core i7-14700K ---
  {
    productId: 'cpu-i7-14700k',
    shopId: 'delta-peripherals',
    priceInr: 36900,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'cpu-i7-14700k',
    shopId: 'supreme-computers',
    priceInr: 36450, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'cpu-i7-14700k',
    shopId: 'marina-micro',
    priceInr: 37200,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },

  // --- ASUS Dual RTX 4070 Super ---
  {
    productId: 'gpu-rtx-4070-super',
    shopId: 'delta-peripherals',
    priceInr: 58900,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'gpu-rtx-4070-super',
    shopId: 'challenger-computers',
    priceInr: 58200, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'gpu-rtx-4070-super',
    shopId: 'oasis-it',
    priceInr: 59400,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },

  // --- ASUS TUF B760-Plus WiFi ---
  {
    productId: 'mobo-b760-plus',
    shopId: 'supreme-computers',
    priceInr: 18450, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'mobo-b760-plus',
    shopId: 'delta-peripherals',
    priceInr: 18900,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },

  // --- DeepCool AK620 Cooler ---
  {
    productId: 'cooler-ak620',
    shopId: 'delta-peripherals',
    priceInr: 5250, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },
  {
    productId: 'cooler-ak620',
    shopId: 'challenger-computers',
    priceInr: 5400,
    stock: true,
    gstIncluded: true,
    warrantyYears: 3,
  },

  // --- Corsair Vengeance 32GB DDR5 ---
  {
    productId: 'ram-vengeance-32gb',
    shopId: 'supreme-computers',
    priceInr: 9600, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 10,
  },
  {
    productId: 'ram-vengeance-32gb',
    shopId: 'delta-peripherals',
    priceInr: 9850,
    stock: true,
    gstIncluded: true,
    warrantyYears: 10,
  },

  // --- WD Blue SN580 1TB SSD ---
  {
    productId: 'ssd-sn580-1tb',
    shopId: 'delta-peripherals',
    priceInr: 5700, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 5,
  },
  {
    productId: 'ssd-sn580-1tb',
    shopId: 'challenger-computers',
    priceInr: 5850,
    stock: true,
    gstIncluded: true,
    warrantyYears: 5,
  },

  // --- MSI MAG A650BN 650W PSU ---
  {
    productId: 'psu-msi-a650bn',
    shopId: 'challenger-computers',
    priceInr: 4350, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 5,
  },
  {
    productId: 'psu-msi-a650bn',
    shopId: 'delta-peripherals',
    priceInr: 4450,
    stock: true,
    gstIncluded: true,
    warrantyYears: 5,
  },

  // --- Montech AIR 903 MAX Cabinet ---
  {
    productId: 'cabinet-air-903',
    shopId: 'delta-peripherals',
    priceInr: 5950, // Cheapest
    stock: true,
    gstIncluded: true,
    warrantyYears: 1,
  },
  {
    productId: 'cabinet-air-903',
    shopId: 'supreme-computers',
    priceInr: 6100,
    stock: true,
    gstIncluded: true,
    warrantyYears: 1,
  },
];

export function getShopById(shopId: string): Shop | undefined {
  return CHENNAI_SHOPS.find((s) => s.id === shopId);
}

export function getProductById(productId: string): Product | undefined {
  return SEED_PRODUCTS.find((p) => p.id === productId);
}

export function getListingsForProduct(productId: string): Listing[] {
  return MOCK_LISTINGS.filter((l) => l.productId === productId);
}

export function getCheapestListing(productId: string): Listing | undefined {
  const listings = getListingsForProduct(productId);
  if (listings.length === 0) return undefined;
  return listings.reduce((min, cur) => (cur.priceInr < min.priceInr ? cur : min));
}
