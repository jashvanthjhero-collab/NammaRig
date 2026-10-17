# NammaRig — Chennai's PC & Laptop Hub (Android Jetpack Compose)

NammaRig is a high-performance native Android application engineered specifically for Chennai's vibrant PC enthusiast, gamer, and custom builder community. It brings the bustling hardware hub of Ritchie Street, Mount Road, and Chennai's tech alleys directly to the builder's fingertips with real-time multi-dealer price comparisons, an interactive 8-component 2D CAD visualizer, automated hardware clearance & TDP validation, and an optimized street-lane shopping slip with 1-tap WhatsApp dealer quote generation.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural and feature decisions were confirmed in Phase 1:
> - **Native Android Target**: Built 100% with native Kotlin, Jetpack Compose, and Material 3, incorporating the exact specified Frost Lavender palette (`#ECEDF5` canvas, `#6C4BF4` electric violet accent, `#090D16` ink text, `#10B981` emerald deal badges).
> - **Rig Studio Presets**: Pre-configured with 4 ready-to-load custom builds: Esports Champion, 4K Titan Gaming, AI Workstation, and Budget Office.
> - **Lane-Clustered Shopping Slip**: Components selected in the builder automatically aggregate into an optimized Ritchie Street walking slip grouped by physical street lanes (Meeran Sahib St, Wallers Rd, Narayana Mudali St) with direct 1-tap WhatsApp quote dispatch.

---

## 1. Overview & Core Concept

Chennai's Ritchie Street is Asia's second-largest electronics market, home to hundreds of competitive hardware retailers. However, navigating dealer alleys, verifying component physical clearance, checking PSU power margins, and haggling for the best prices can be overwhelming.

**NammaRig** solves this by providing:
1. **Ritchie Street Price Matrix**: Direct component pricing across renowned Chennai dealers (Oasis IT, Delta Peripherals, Challenger Computers, Supreme Computers, Nakoda IT, IT Depôt, Computer Waves, Ashirwad), highlighting the absolute lowest quote in Chennai in vibrant emerald green.
2. **Rig Studio CAD Visualizer**: An 8-component builder (CPU, Motherboard, GPU, Cooler, RAM, SSD, PSU, Case) with an interactive 2D blueprint schematic showing physical layout, socket alignments, and clearance boundaries.
3. **Hardware Spec & Clearance Engine**: Real-time validation of TDP headroom (calculating total system draw vs. PSU capacity with headroom gauges) alongside dimension checks (GPU card length vs. case max GPU clearance, CPU cooler tower height vs. case width, motherboard form factor ATX/mATX/ITX vs. case brackets).
4. **Component Hunter**: Swipeable category pills, interactive price & budget range sliders, dealer filters, and fast search.
5. **Ritchie Street Shopping Slip**: Groups components by shortest walking route across Ritchie Street's lanes, shows estimated walking time, calculates total build cost at best dealer rates, and crafts pre-formatted WhatsApp quote inquiries with 1 tap.

---

## 2. User Experience & Visual Design

### Color Palette & Visual Identity (Frost Lavender Aesthetic)
- **Canvas / Background**: Frost Lavender (`#ECEDF5`), subtle slate-lavender surfaces (`#F4F5FA`, dark mode support `#12111A`)
- **Primary / Brand Accent**: Electric Violet (`#6C4BF4`) for key interactive actions, active states, and CAD blueprint highlights
- **Text & Contrast**: High-contrast Ink Black (`#090D16`), Muted Slate (`#525866`) for secondary specs and dealer tags
- **Deal / Lowest Price Accent**: Emerald Green (`#10B981`) for lowest price flags, clearance OK badges, and in-stock statuses
- **Warning / Alert**: Vivid Amber (`#F59E0B`) and Crimson (`#EF4444`) for clearance mismatches or insufficient PSU wattage

### Screen Architecture & Navigation
The app features an ergonomic bottom navigation bar with 4 primary destinations:
1. **Studio (Rig Studio CAD)**: The 8-component interactive workbench, blueprint schematic view, real-time TDP power gauge, physical clearance status pill, and build preset quick-loader.
2. **Dealers (Price Hub)**: Component price comparisons across Ritchie Street and Mount Road shops, with filterable store directory and live price differences.
3. **Hunter (Component Catalog)**: Filterable catalog with swipeable component categories (CPU, GPU, RAM, Motherboard, SSD, PSU, Cooler, Case), budget sliders, and "Add to Rig" action.
4. **Slip (Ritchie Street Route & Quotes)**: The organized shopping itinerary clustered by street lanes (Meeran Sahib St, Wallers Rd, Narayana Mudali St), shortest route visualizer, and 1-tap WhatsApp quote dispatch.

---

## 3. Key Product Decisions & Trade-Offs

- **2D CAD Blueprint Visualization**: Rather than heavyweight 3D meshes that increase APK size and degrade low-end device performance, we implement a crisp, high-framerate 2D Jetpack Compose Canvas blueprint schematic. It renders true-to-scale dimensional outlines (case boundaries, motherboard footprint, GPU length bracket, cooler radiator/tower position) with interactive hot-spot component inspect overlays.
- **Local Persistence via Room & Preferences**: Custom builds, saved slips, and user preferences are persisted locally on-device using Room Database and DataStore. No mandatory login or network dependency is required to calculate clearances, plan builds, or assemble shopping slips.
- **Realistic Ritchie Street Dealer Dataset**: Pre-seeded with authentic Chennai retail pricing, store addresses, phone numbers, and WhatsApp links across legendary Ritchie Street and Mount Road electronics distributors.
- **WhatsApp Direct Deep-Linking**: Uses Android's native intent `Intent.ACTION_VIEW` (`https://api.whatsapp.com/send?phone=...&text=...`) to dispatch fully itemized quotation requests with model numbers, targeted price, and stock inquiries directly into the dealer's WhatsApp chat.

---

## 4. Technical Architecture & Data Strategy

### System Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                          NammaRig Android App                          │
├────────────────────────────────────────────────────────────────────────┤
│                           UI Layer (Compose)                           │
│  ┌──────────────────┬─────────────────┬─────────────────┬───────────┐  │
│  │ Rig Studio (CAD) │ Dealer Price Hub│ Component Hunter│ Shop Slip │  │
│  │ Blueprint Canvas │ Ritchie Matrix  │ Budget Sliders  │ WhatsApp  │  │
│  └────────▲─────────┴────────▲────────┴────────▲────────┴─────▲─────┘  │
│           │                  │                 │              │        │
│           └──────────────────┼─────────────────┴──────────────┘        │
│                              │                                         │
│                      State & ViewModels                                │
│       ┌───────────────────────────────────────────────┐                │
│       │ RigBuilderViewModel & SpecCheckerEngine       │                │
│       │ • 8-Component Active Slots                    │                │
│       │ • TDP Headroom & Power Curve Calculator       │                │
│       │ • Physical Clearance Matrix (GPU/Cooler/MB)   │                │
│       │ • Lane Grouping & Route Optimizer             │                │
│       └──────────────────────▲────────────────────────┘                │
│                              │                                         │
│                       Repository Layer                                 │
│       ┌───────────────────────────────────────────────┐                │
│       │ HardwareRepository & DealerRepository         │                │
│       │ • Component specs (dimensions, TDP, sockets)  │                │
│       │ • Multi-dealer price quotes & stock info      │                │
│       │ • Room DAO for Saved Custom Builds            │                │
│       └───────────────────────────────────────────────┘                │
└────────────────────────────────────────────────────────────────────────┘
```

### Key Data Entities
1. **ComponentItem**: `id`, `name`, `category` (CPU, GPU, etc.), `brand`, `specDetails`, `tdpWatts`, `dimensionsMm` (length, height, width), `socketType`, `formFactor`, `basePriceInr`, `dealerPrices: List<DealerPrice>`
2. **DealerPrice**: `dealerId`, `dealerName`, `streetLane`, `priceInr`, `inStock`, `warrantyYears`, `whatsappNumber`
3. **RigBuild**: `id`, `name`, `timestamp`, 8 component slots (`cpu`, `motherboard`, `gpu`, `cooler`, `ram`, `ssd`, `psu`, `case`), `totalTdp`, `totalCostInr`
4. **ClearanceReport**: `isGpuFitting`, `gpuMarginMm`, `isCoolerFitting`, `coolerMarginMm`, `isSocketMatched`, `isFormFactorSupported`, `isPsuAdequate`, `tdpHeadroomWatts`, `recommendedPsuWatts`
5. **ShopLaneCluster**: `laneName` (e.g. Wallers Road, Meeran Sahib St), `dealers: List<DealerStop>`, `totalLaneItems: List<ComponentItem>`, `laneCostInr`

---

## Verification Plan

### Automated Build & Unit Verification
- Run `compile_applet` to confirm clean Gradle compilation with zero errors.
- Run Robolectric unit test verifying `SpecCheckerEngine` calculations (TDP headroom math, GPU dimension clearance checks, and lane-clustering grouping).

### User Journey Tests
- **CAD Studio & Presets**: Load "Esports Champion" or "4K Titan", inspect 2D blueprint layout, swap GPU, verify clearance alert triggers when GPU exceeds case max length.
- **Spec Checker**: Observe live power gauge and headroom calculations when upgrading CPU/GPU.
- **Price Matrix**: Browse components, verify green badge marks cheapest Chennai dealer, compare Ritchie Street vs Mount Road.
- **Shopping Slip & WhatsApp**: Add custom rig to Slip, verify lane grouping (Meeran Sahib, Wallers Rd), tap WhatsApp action to test intent string creation.
