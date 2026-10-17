package com.example.data

import com.example.model.ComponentCategory
import com.example.model.ComponentDimensions
import com.example.model.ComponentItem
import com.example.model.DealerPrice
import com.example.model.PresetBuild

object DealerDirectory {
    val OASIS = DealerPrice(
        dealerId = "oasis_it",
        dealerName = "Oasis IT Store",
        streetLane = "Meeran Sahib Street",
        priceInr = 0,
        phone = "+91 44 2841 1290",
        whatsappNumber = "919840112233"
    )
    val DELTA = DealerPrice(
        dealerId = "delta_peripherals",
        dealerName = "Delta Peripherals",
        streetLane = "Wallers Road",
        priceInr = 0,
        phone = "+91 44 2841 3345",
        whatsappNumber = "919840223344"
    )
    val CHALLENGER = DealerPrice(
        dealerId = "challenger_comp",
        dealerName = "Challenger Computers",
        streetLane = "Wallers Road",
        priceInr = 0,
        phone = "+91 44 2841 8912",
        whatsappNumber = "919840334455"
    )
    val SUPREME = DealerPrice(
        dealerId = "supreme_comp",
        dealerName = "Supreme Computers",
        streetLane = "Meeran Sahib Street",
        priceInr = 0,
        phone = "+91 44 2841 4422",
        whatsappNumber = "919840445566"
    )
    val NAKODA = DealerPrice(
        dealerId = "nakoda_it",
        dealerName = "Nakoda IT Hub",
        streetLane = "Narayana Mudali Street",
        priceInr = 0,
        phone = "+91 44 2538 1199",
        whatsappNumber = "919840556677"
    )
    val IT_DEPOT = DealerPrice(
        dealerId = "it_depot",
        dealerName = "The IT Depôt",
        streetLane = "Mount Road Tech Arcade",
        priceInr = 0,
        phone = "+91 44 2841 9900",
        whatsappNumber = "919840667788"
    )
    val WAVES = DealerPrice(
        dealerId = "comp_waves",
        dealerName = "Computer Waves",
        streetLane = "Wallers Road",
        priceInr = 0,
        phone = "+91 44 2841 7766",
        whatsappNumber = "919840778899"
    )
    val ASHIRWAD = DealerPrice(
        dealerId = "ashirwad_tech",
        dealerName = "Ashirwad Tech",
        streetLane = "Narayana Mudali Street",
        priceInr = 0,
        phone = "+91 44 2538 5544",
        whatsappNumber = "919840889900"
    )

    val MARINA_MICRO = DealerPrice(
        dealerId = "marina_micro",
        dealerName = "Marina Micro",
        streetLane = "Mount Road Tech Arcade",
        priceInr = 0,
        phone = "+91 44 2852 4411",
        whatsappNumber = "919840991122"
    )
    val VELACHERY_PC = DealerPrice(
        dealerId = "velachery_pc",
        dealerName = "Velachery PC Point",
        streetLane = "Velachery Hub",
        priceInr = 0,
        phone = "+91 44 2244 5566",
        whatsappNumber = "919840882244"
    )

    val ALL_LANES = listOf(
        "Meeran Sahib Street",
        "Wallers Road",
        "Narayana Mudali Street",
        "Mount Road Tech Arcade",
        "Velachery Hub"
    )
}

data class PricePulseDrop(
    val id: String,
    val componentName: String,
    val dropAmountInr: Int,
    val currentPriceInr: Int,
    val previousPriceInr: Int,
    val shopName: String,
    val streetLane: String,
    val category: ComponentCategory
)

object HardwareCatalog {
    val livePriceDrops: List<PricePulseDrop> = listOf(
        PricePulseDrop(
            id = "drop_1",
            componentName = "Samsung 990 Pro 2TB NVMe SSD",
            dropAmountInr = 1200,
            currentPriceInr = 16499,
            previousPriceInr = 17699,
            shopName = "Delta Peripherals",
            streetLane = "Meeran Sahib St, Ritchie St",
            category = ComponentCategory.SSD
        ),
        PricePulseDrop(
            id = "drop_2",
            componentName = "NVIDIA GeForce RTX 5080 16GB",
            dropAmountInr = 4500,
            currentPriceInr = 112000,
            previousPriceInr = 116500,
            shopName = "Marina Micro",
            streetLane = "Mount Road Tech Arcade",
            category = ComponentCategory.GPU
        ),
        PricePulseDrop(
            id = "drop_3",
            componentName = "MSI MAG X870E Tomahawk WiFi",
            dropAmountInr = 2100,
            currentPriceInr = 31400,
            previousPriceInr = 33500,
            shopName = "Supreme Computers",
            streetLane = "Meeran Sahib St, Ritchie St",
            category = ComponentCategory.MOTHERBOARD
        ),
        PricePulseDrop(
            id = "drop_4",
            componentName = "G.Skill Trident Z5 Neo 32GB DDR5",
            dropAmountInr = 850,
            currentPriceInr = 11950,
            previousPriceInr = 12800,
            shopName = "Challenger Computers",
            streetLane = "Wallers Road, Ritchie St",
            category = ComponentCategory.RAM
        ),
        PricePulseDrop(
            id = "drop_5",
            componentName = "NZXT Kraken 360 RGB Liquid Cooler",
            dropAmountInr = 1400,
            currentPriceInr = 17800,
            previousPriceInr = 19200,
            shopName = "Oasis IT Store",
            streetLane = "Meeran Sahib St, Ritchie St",
            category = ComponentCategory.COOLER
        ),
        PricePulseDrop(
            id = "drop_6",
            componentName = "Corsair RM850x 850W Gold Modular",
            dropAmountInr = 900,
            currentPriceInr = 11400,
            previousPriceInr = 12300,
            shopName = "Challenger Computers",
            streetLane = "Wallers Road, Ritchie St",
            category = ComponentCategory.PSU
        )
    )
    val allComponents: List<ComponentItem> = listOf(
        // ================= CPUs =================
        ComponentItem(
            id = "cpu_r9_9950x3d",
            name = "AMD Ryzen 9 9950X3D Processor",
            category = ComponentCategory.CPU,
            brand = "AMD",
            specs = "16 Cores / 32 Threads, 5.7GHz Boost, 144MB 3D V-Cache, AM5 Socket, PCIe 5.0",
            tdpWatts = 120,
            socketType = "AM5",
            dealerPrices = listOf(
                DealerDirectory.DELTA.copy(priceInr = 59322, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 59990, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 61200, inStock = true),
                DealerDirectory.MARINA_MICRO.copy(priceInr = 60500, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 60800, inStock = true),
                DealerDirectory.NAKODA.copy(priceInr = 61400, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 62500, inStock = true),
                DealerDirectory.VELACHERY_PC.copy(priceInr = 61900, inStock = true)
            )
        ),
        ComponentItem(
            id = "cpu_r5_7600",
            name = "AMD Ryzen 5 7600",
            category = ComponentCategory.CPU,
            brand = "AMD",
            specs = "6 Cores / 12 Threads, 5.1GHz Turbo, 38MB Cache, AM5",
            tdpWatts = 65,
            socketType = "AM5",
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 17400, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 17150, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 17600, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 17300, inStock = true)
            )
        ),
        ComponentItem(
            id = "cpu_r7_7800x3d",
            name = "AMD Ryzen 7 7800X3D",
            category = ComponentCategory.CPU,
            brand = "AMD",
            specs = "8 Cores / 16 Threads, 5.0GHz, 96MB 3D V-Cache, AM5",
            tdpWatts = 120,
            socketType = "AM5",
            dealerPrices = listOf(
                DealerDirectory.DELTA.copy(priceInr = 38900, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 39200, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 39800, inStock = true),
                DealerDirectory.NAKODA.copy(priceInr = 38450, inStock = true)
            )
        ),
        ComponentItem(
            id = "cpu_i5_13400f",
            name = "Intel Core i5-13400F",
            category = ComponentCategory.CPU,
            brand = "Intel",
            specs = "10 Cores (6P + 4E) / 16 Threads, 4.6GHz Turbo, LGA1700",
            tdpWatts = 65,
            socketType = "LGA1700",
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 16200, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 15990, inStock = true),
                DealerDirectory.ASHIRWAD.copy(priceInr = 16400, inStock = true)
            )
        ),
        ComponentItem(
            id = "cpu_i7_14700k",
            name = "Intel Core i7-14700K",
            category = ComponentCategory.CPU,
            brand = "Intel",
            specs = "20 Cores (8P + 12E) / 28 Threads, 5.6GHz Turbo, LGA1700",
            tdpWatts = 125,
            socketType = "LGA1700",
            dealerPrices = listOf(
                DealerDirectory.DELTA.copy(priceInr = 36900, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 37200, inStock = true),
                DealerDirectory.WAVES.copy(priceInr = 36490, inStock = true)
            )
        ),
        ComponentItem(
            id = "cpu_r5_5600g",
            name = "AMD Ryzen 5 5600G with Vega Graphics",
            category = ComponentCategory.CPU,
            brand = "AMD",
            specs = "6 Cores / 12 Threads, 4.4GHz, Radeon Vega 7 GPU, AM4",
            tdpWatts = 65,
            socketType = "AM4",
            dealerPrices = listOf(
                DealerDirectory.NAKODA.copy(priceInr = 10450, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 10600, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 10750, inStock = true)
            )
        ),

        // ================= Motherboards =================
        ComponentItem(
            id = "mobo_msi_x870e",
            name = "MSI MAG X870E Tomahawk WiFi",
            category = ComponentCategory.MOTHERBOARD,
            brand = "MSI",
            specs = "ATX, AM5 Socket, 4x DDR5 EXPO up to 8400+ MT/s, PCIe 5.0 x16, 4x M.2, Wi-Fi 7, 5Gbps LAN",
            tdpWatts = 35,
            socketType = "AM5",
            formFactor = "ATX",
            dealerPrices = listOf(
                DealerDirectory.SUPREME.copy(priceInr = 31400, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 32000, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 32500, inStock = true),
                DealerDirectory.MARINA_MICRO.copy(priceInr = 31900, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 32200, inStock = true)
            )
        ),
        ComponentItem(
            id = "mobo_b650_gaming_plus",
            name = "MSI B650 Gaming Plus WiFi",
            category = ComponentCategory.MOTHERBOARD,
            brand = "MSI",
            specs = "ATX, AM5 Socket, DDR5, PCIe 4.0, 2x M.2, Wi-Fi 6E",
            tdpWatts = 30,
            socketType = "AM5",
            formFactor = "ATX",
            dealerPrices = listOf(
                DealerDirectory.DELTA.copy(priceInr = 16800, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 16450, inStock = true),
                DealerDirectory.WAVES.copy(priceInr = 16900, inStock = true)
            )
        ),
        ComponentItem(
            id = "mobo_b650m_ds3h",
            name = "Gigabyte B650M DS3H",
            category = ComponentCategory.MOTHERBOARD,
            brand = "Gigabyte",
            specs = "Micro-ATX, AM5 Socket, DDR5, PCIe 4.0, Dual M.2",
            tdpWatts = 25,
            socketType = "AM5",
            formFactor = "Micro-ATX",
            dealerPrices = listOf(
                DealerDirectory.CHALLENGER.copy(priceInr = 12400, inStock = true),
                DealerDirectory.NAKODA.copy(priceInr = 12150, inStock = true),
                DealerDirectory.ASHIRWAD.copy(priceInr = 12500, inStock = true)
            )
        ),
        ComponentItem(
            id = "mobo_b760m_pro",
            name = "ASRock B760M Pro RS WiFi",
            category = ComponentCategory.MOTHERBOARD,
            brand = "ASRock",
            specs = "Micro-ATX, LGA1700, DDR5, PCIe 5.0 x16, Wi-Fi 6E",
            tdpWatts = 25,
            socketType = "LGA1700",
            formFactor = "Micro-ATX",
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 13800, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 13950, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 13600, inStock = true)
            )
        ),
        ComponentItem(
            id = "mobo_x670e_aorus",
            name = "Gigabyte X670E Aorus Master",
            category = ComponentCategory.MOTHERBOARD,
            brand = "Gigabyte",
            specs = "E-ATX/ATX, AM5 Socket, PCIe 5.0 GPU & M.2, 16+2+2 VRM",
            tdpWatts = 45,
            socketType = "AM5",
            formFactor = "ATX",
            dealerPrices = listOf(
                DealerDirectory.IT_DEPOT.copy(priceInr = 44500, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 43800, inStock = true)
            )
        ),
        ComponentItem(
            id = "mobo_b450m_k",
            name = "Asus Prime B450M-K II",
            category = ComponentCategory.MOTHERBOARD,
            brand = "Asus",
            specs = "Micro-ATX, AM4 Socket, DDR4 4400MHz, M.2 NVMe, DVI/VGA",
            tdpWatts = 20,
            socketType = "AM4",
            formFactor = "Micro-ATX",
            dealerPrices = listOf(
                DealerDirectory.NAKODA.copy(priceInr = 4650, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 4800, inStock = true)
            )
        ),

        // ================= GPUs =================
        ComponentItem(
            id = "gpu_rtx_5080",
            name = "NVIDIA GeForce RTX 5080 16GB GDDR7",
            category = ComponentCategory.GPU,
            brand = "NVIDIA",
            specs = "16GB GDDR7, Blackwell Architecture, 10752 CUDA Cores, DLSS 4, DP 2.1, 400W TDP",
            tdpWatts = 400,
            dimensionsMm = ComponentDimensions(lengthMm = 310, heightMm = 140, widthMm = 61),
            dealerPrices = listOf(
                DealerDirectory.MARINA_MICRO.copy(priceInr = 112000, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 114500, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 115000, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 116000, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 115500, inStock = true)
            )
        ),
        ComponentItem(
            id = "gpu_rtx_4060",
            name = "Zotac Gaming GeForce RTX 4060 8GB Twin Edge",
            category = ComponentCategory.GPU,
            brand = "Zotac",
            specs = "8GB GDDR6, DLSS 3, Dual Fan, 221mm Length, 115W TDP",
            tdpWatts = 115,
            dimensionsMm = ComponentDimensions(lengthMm = 221, heightMm = 123, widthMm = 43),
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 27800, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 27400, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 27950, inStock = true),
                DealerDirectory.NAKODA.copy(priceInr = 27100, inStock = true)
            )
        ),
        ComponentItem(
            id = "gpu_rtx_4070_super",
            name = "Gigabyte GeForce RTX 4070 Super Windforce OC 12GB",
            category = ComponentCategory.GPU,
            brand = "Gigabyte",
            specs = "12GB GDDR6X, Triple Fan, 261mm Length, 220W TDP",
            tdpWatts = 220,
            dimensionsMm = ComponentDimensions(lengthMm = 261, heightMm = 126, widthMm = 50),
            dealerPrices = listOf(
                DealerDirectory.DELTA.copy(priceInr = 58900, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 59400, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 59800, inStock = true)
            )
        ),
        ComponentItem(
            id = "gpu_rtx_4080_super",
            name = "ASUS TUF Gaming GeForce RTX 4080 Super 16GB",
            category = ComponentCategory.GPU,
            brand = "ASUS",
            specs = "16GB GDDR6X, 3.2-slot, 348mm Length, 320W TDP",
            tdpWatts = 320,
            dimensionsMm = ComponentDimensions(lengthMm = 348, heightMm = 150, widthMm = 72),
            dealerPrices = listOf(
                DealerDirectory.DELTA.copy(priceInr = 104500, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 106000, inStock = true),
                DealerDirectory.WAVES.copy(priceInr = 103900, inStock = true)
            )
        ),
        ComponentItem(
            id = "gpu_rtx_4090",
            name = "MSI GeForce RTX 4090 Suprim X 24GB",
            category = ComponentCategory.GPU,
            brand = "MSI",
            specs = "24GB GDDR6X, Tri-Frozr 3S, 336mm Length, 450W TDP",
            tdpWatts = 450,
            dimensionsMm = ComponentDimensions(lengthMm = 336, heightMm = 142, widthMm = 78),
            dealerPrices = listOf(
                DealerDirectory.IT_DEPOT.copy(priceInr = 189000, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 186500, inStock = true)
            )
        ),
        ComponentItem(
            id = "gpu_rx_7600",
            name = "Sapphire Pulse AMD Radeon RX 7600 8GB",
            category = ComponentCategory.GPU,
            brand = "Sapphire",
            specs = "8GB GDDR6, Dual-X Cooling, 240mm Length, 165W TDP",
            tdpWatts = 165,
            dimensionsMm = ComponentDimensions(lengthMm = 240, heightMm = 120, widthMm = 44),
            dealerPrices = listOf(
                DealerDirectory.SUPREME.copy(priceInr = 23900, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 23700, inStock = true),
                DealerDirectory.NAKODA.copy(priceInr = 23450, inStock = true)
            )
        ),

        // ================= CPU Coolers =================
        ComponentItem(
            id = "cooler_nzxt_kraken_360",
            name = "NZXT Kraken 360 RGB Liquid Cooler",
            category = ComponentCategory.COOLER,
            brand = "NZXT",
            specs = "360mm Radiator, 1.54\" LCD Display, 3x F120 RGB Core Fans, 394mm Radiator Length",
            tdpWatts = 15,
            dimensionsMm = ComponentDimensions(heightMm = 55, widthMm = 120, lengthMm = 394),
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 17800, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 18200, inStock = true),
                DealerDirectory.MARINA_MICRO.copy(priceInr = 18100, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 18500, inStock = true)
            )
        ),
        ComponentItem(
            id = "cooler_ak620",
            name = "DeepCool AK620 Dual-Tower High-Performance Cooler",
            category = ComponentCategory.COOLER,
            brand = "DeepCool",
            specs = "Dual 120mm PWM Fans, 6 Heatpipes, 160mm Height, 260W TDP Support",
            tdpWatts = 5,
            dimensionsMm = ComponentDimensions(heightMm = 160, widthMm = 129, lengthMm = 138),
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 5400, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 5250, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 5500, inStock = true)
            )
        ),
        ComponentItem(
            id = "cooler_ag400",
            name = "DeepCool AG400 ARGB Single Tower",
            category = ComponentCategory.COOLER,
            brand = "DeepCool",
            specs = "120mm ARGB Fan, 4 Heatpipes, 150mm Height, 220W TDP Support",
            tdpWatts = 4,
            dimensionsMm = ComponentDimensions(heightMm = 150, widthMm = 92, lengthMm = 125),
            dealerPrices = listOf(
                DealerDirectory.NAKODA.copy(priceInr = 1750, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 1850, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 1790, inStock = true)
            )
        ),
        ComponentItem(
            id = "cooler_liquid_360",
            name = "Corsair iCUE LINK H150i RGB 360mm AIO Liquid Cooler",
            category = ComponentCategory.COOLER,
            brand = "Corsair",
            specs = "360mm Radiator, 3x 120mm Magnetic Dome Fans, 397mm Radiator Length",
            tdpWatts = 15,
            dimensionsMm = ComponentDimensions(heightMm = 52, widthMm = 120, lengthMm = 397),
            dealerPrices = listOf(
                DealerDirectory.IT_DEPOT.copy(priceInr = 18400, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 17990, inStock = true)
            )
        ),
        ComponentItem(
            id = "cooler_wraith_stock",
            name = "AMD Wraith Stealth Stock Cooler",
            category = ComponentCategory.COOLER,
            brand = "AMD",
            specs = "Low-profile Top Down Airflow, 54mm Height, 65W TDP Support",
            tdpWatts = 2,
            dimensionsMm = ComponentDimensions(heightMm = 54, widthMm = 100, lengthMm = 100),
            dealerPrices = listOf(
                DealerDirectory.NAKODA.copy(priceInr = 650, inStock = true),
                DealerDirectory.ASHIRWAD.copy(priceInr = 700, inStock = true)
            )
        ),

        // ================= RAM =================
        ComponentItem(
            id = "ram_gskill_trident_z5_neo",
            name = "G.Skill Trident Z5 Neo RGB 32GB (2x16GB) DDR5-6000 CL30",
            category = ComponentCategory.RAM,
            brand = "G.Skill",
            specs = "32GB Kit (2x16GB), 6000MT/s, Ultra Low Latency CL30-36-36-96, AMD EXPO Certified",
            tdpWatts = 10,
            dealerPrices = listOf(
                DealerDirectory.CHALLENGER.copy(priceInr = 11950, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 12200, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 12400, inStock = true),
                DealerDirectory.MARINA_MICRO.copy(priceInr = 12100, inStock = true)
            )
        ),
        ComponentItem(
            id = "ram_corsair_ddr5_32gb",
            name = "Corsair Vengeance 32GB (2x16GB) DDR5 6000MHz CL30",
            category = ComponentCategory.RAM,
            brand = "Corsair",
            specs = "32GB Kit, 6000MT/s, CL30-36-36-76, AMD EXPO & Intel XMP 3.0",
            tdpWatts = 10,
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 9800, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 9600, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 9950, inStock = true)
            )
        ),
        ComponentItem(
            id = "ram_gskill_ddr5_64gb",
            name = "G.Skill Ripjaws S5 64GB (2x32GB) DDR5 6000MHz",
            category = ComponentCategory.RAM,
            brand = "G.Skill",
            specs = "64GB Dual Channel, 6000MT/s, Low Profile 33mm Height, XMP 3.0",
            tdpWatts = 15,
            dealerPrices = listOf(
                DealerDirectory.IT_DEPOT.copy(priceInr = 19200, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 18800, inStock = true)
            )
        ),
        ComponentItem(
            id = "ram_crucial_ddr4_16gb",
            name = "Crucial Basics 16GB (2x8GB) DDR4 3200MHz",
            category = ComponentCategory.RAM,
            brand = "Crucial",
            specs = "16GB Dual Channel, 3200MT/s, 1.2V, Desktop UDIMM",
            tdpWatts = 6,
            dealerPrices = listOf(
                DealerDirectory.NAKODA.copy(priceInr = 2950, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 3100, inStock = true),
                DealerDirectory.ASHIRWAD.copy(priceInr = 2900, inStock = true)
            )
        ),

        // ================= SSDs =================
        ComponentItem(
            id = "ssd_wd_sn580_1tb",
            name = "WD Blue SN580 1TB NVMe PCIe Gen 4x4 SSD",
            category = ComponentCategory.SSD,
            brand = "Western Digital",
            specs = "1TB, Up to 4150 MB/s Read, nCache 4.0, M.2 2280",
            tdpWatts = 5,
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 5850, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 5700, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 5900, inStock = true),
                DealerDirectory.NAKODA.copy(priceInr = 5650, inStock = true)
            )
        ),
        ComponentItem(
            id = "ssd_samsung_990pro_2tb",
            name = "Samsung 990 PRO 2TB PCIe 4.0 NVMe SSD with Heatsink",
            category = ComponentCategory.SSD,
            brand = "Samsung",
            specs = "2TB, 7450 MB/s Read, 6900 MB/s Write, 2GB LPDDR4 DRAM Cache",
            tdpWatts = 8,
            dealerPrices = listOf(
                DealerDirectory.IT_DEPOT.copy(priceInr = 17800, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 17350, inStock = true),
                DealerDirectory.WAVES.copy(priceInr = 17500, inStock = true)
            )
        ),
        ComponentItem(
            id = "ssd_crucial_p3_500gb",
            name = "Crucial P3 500GB PCIe 3.0 3D NAND NVMe SSD",
            category = ComponentCategory.SSD,
            brand = "Crucial",
            specs = "500GB, Up to 3500 MB/s Read, M.2 2280 Form Factor",
            tdpWatts = 4,
            dealerPrices = listOf(
                DealerDirectory.NAKODA.copy(priceInr = 3150, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 3250, inStock = true)
            )
        ),

        // ================= PSUs =================
        ComponentItem(
            id = "psu_corsair_rm850x",
            name = "Corsair RM850x 850W 80 Plus Gold Fully Modular",
            category = ComponentCategory.PSU,
            brand = "Corsair",
            specs = "850 Watt, ATX 3.1 & PCIe 5.1 Ready, Zero RPM Fan Mode, 135mm Mag-Lev Fan",
            wattageRating = 850,
            dealerPrices = listOf(
                DealerDirectory.CHALLENGER.copy(priceInr = 11400, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 11650, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 11500, inStock = true),
                DealerDirectory.MARINA_MICRO.copy(priceInr = 11550, inStock = true)
            )
        ),
        ComponentItem(
            id = "psu_msi_a650bn",
            name = "MSI MAG A650BN 650W 80 Plus Bronze",
            category = ComponentCategory.PSU,
            brand = "MSI",
            specs = "650 Watt, 80 Plus Bronze, DC-to-DC Design, 120mm Low-Noise Fan",
            wattageRating = 650,
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 4450, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 4350, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 4500, inStock = true)
            )
        ),
        ComponentItem(
            id = "psu_corsair_rm850e",
            name = "Corsair RM850e 850W 80 Plus Gold Fully Modular",
            category = ComponentCategory.PSU,
            brand = "Corsair",
            specs = "850 Watt, ATX 3.0 & PCIe 5.0 12VHPWR Native, Zero RPM Mode",
            wattageRating = 850,
            dealerPrices = listOf(
                DealerDirectory.DELTA.copy(priceInr = 10400, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 10600, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 10250, inStock = true)
            )
        ),
        ComponentItem(
            id = "psu_antec_c1000",
            name = "Antec Signature Platinum 1000W Fully Modular",
            category = ComponentCategory.PSU,
            brand = "Antec",
            specs = "1000 Watt, 80 Plus Platinum, 100% Japanese Capacitors, 10-Yr Warranty",
            wattageRating = 1000,
            dealerPrices = listOf(
                DealerDirectory.IT_DEPOT.copy(priceInr = 17500, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 16900, inStock = true)
            )
        ),
        ComponentItem(
            id = "psu_ant_vs500",
            name = "Ant Esports VS500L 500W Non-Modular PSU",
            category = ComponentCategory.PSU,
            brand = "Ant Esports",
            specs = "500 Watt, Single +12V Rail, 120mm Silent Sleeve Fan, Black Ribbed Cable",
            wattageRating = 500,
            dealerPrices = listOf(
                DealerDirectory.NAKODA.copy(priceInr = 1950, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 2050, inStock = true)
            )
        ),

        // ================= Cases =================
        ComponentItem(
            id = "case_lian_li_o11_evo",
            name = "Lian Li O11 Dynamic EVO Mid Tower",
            category = ComponentCategory.CASE,
            brand = "Lian Li",
            specs = "Dual-Chamber Modular, Dual Tempered Glass, Up to 420mm GPU, 167mm Cooler Height, 360mm Rad",
            maxGpuLengthMm = 420,
            maxCoolerHeightMm = 167,
            supportedFormFactors = listOf("E-ATX", "ATX", "Micro-ATX", "Mini-ITX"),
            dealerPrices = listOf(
                DealerDirectory.DELTA.copy(priceInr = 14200, inStock = true),
                DealerDirectory.MARINA_MICRO.copy(priceInr = 14600, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 14800, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 14500, inStock = true)
            )
        ),
        ComponentItem(
            id = "case_montech_air_903",
            name = "Montech AIR 903 MAX E-ATX Mid Tower",
            category = ComponentCategory.CASE,
            brand = "Montech",
            specs = "Mesh Front, 4x 140mm PWM Fans included, Type-C, Max GPU 400mm, Cooler 180mm",
            maxGpuLengthMm = 400,
            maxCoolerHeightMm = 180,
            supportedFormFactors = listOf("E-ATX", "ATX", "Micro-ATX", "Mini-ITX"),
            dealerPrices = listOf(
                DealerDirectory.DELTA.copy(priceInr = 6100, inStock = true),
                DealerDirectory.OASIS.copy(priceInr = 5950, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 6200, inStock = true)
            )
        ),
        ComponentItem(
            id = "case_lian_li_216",
            name = "Lian Li Lancool 216 RGB Focus Flow Mid Tower",
            category = ComponentCategory.CASE,
            brand = "Lian Li",
            specs = "2x 160mm ARGB Fans, 1x 140mm Rear Fan, Max GPU 392mm, Cooler 180mm",
            maxGpuLengthMm = 392,
            maxCoolerHeightMm = 180,
            supportedFormFactors = listOf("E-ATX", "ATX", "Micro-ATX", "Mini-ITX"),
            dealerPrices = listOf(
                DealerDirectory.IT_DEPOT.copy(priceInr = 8400, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 8150, inStock = true),
                DealerDirectory.WAVES.copy(priceInr = 8300, inStock = true)
            )
        ),
        ComponentItem(
            id = "case_ant_ice_100",
            name = "Ant Esports ICE-100 Auto RGB Micro-ATX Mini Tower",
            category = ComponentCategory.CASE,
            brand = "Ant Esports",
            specs = "Compact Micro-ATX, Acrylic Panel, Max GPU 310mm, Cooler 155mm",
            maxGpuLengthMm = 310,
            maxCoolerHeightMm = 155,
            supportedFormFactors = listOf("Micro-ATX", "Mini-ITX"),
            dealerPrices = listOf(
                DealerDirectory.NAKODA.copy(priceInr = 2450, inStock = true),
                DealerDirectory.SUPREME.copy(priceInr = 2550, inStock = true),
                DealerDirectory.ASHIRWAD.copy(priceInr = 2400, inStock = true)
            )
        ),
        ComponentItem(
            id = "case_nzxt_h5_flow",
            name = "NZXT H5 Flow Compact ATX Mid Tower",
            category = ComponentCategory.CASE,
            brand = "NZXT",
            specs = "Dedicated Bottom GPU Fan, Tempered Glass, Max GPU 365mm, Cooler 165mm",
            maxGpuLengthMm = 365,
            maxCoolerHeightMm = 165,
            supportedFormFactors = listOf("ATX", "Micro-ATX", "Mini-ITX"),
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 7400, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 7200, inStock = true)
            )
        ),

        // ================= Peripherals & Accessories =================
        ComponentItem(
            id = "acc_lg_ultragear_27",
            name = "LG UltraGear 27GR75Q 27\" QHD 165Hz IPS Gaming Monitor",
            category = ComponentCategory.ACCESSORIES,
            brand = "LG",
            specs = "2560x1440 2K, 1ms GtG, HDR10, AMD FreeSync Premium, sRGB 99%",
            tdpWatts = 35,
            dealerPrices = listOf(
                DealerDirectory.OASIS.copy(priceInr = 21500, inStock = true),
                DealerDirectory.DELTA.copy(priceInr = 20990, inStock = true),
                DealerDirectory.IT_DEPOT.copy(priceInr = 21800, inStock = true)
            )
        ),
        ComponentItem(
            id = "acc_logitech_g502",
            name = "Logitech G502 HERO High Performance Gaming Mouse",
            category = ComponentCategory.ACCESSORIES,
            brand = "Logitech",
            specs = "HERO 25K Sensor, 25600 DPI, 11 Programmable Buttons, Tunable Weights",
            tdpWatts = 1,
            dealerPrices = listOf(
                DealerDirectory.NAKODA.copy(priceInr = 3850, inStock = true),
                DealerDirectory.CHALLENGER.copy(priceInr = 3990, inStock = true)
            )
        )
    )

    // 4 Ready-to-Load Presets as decided in planning phase
    val presetBuilds: List<PresetBuild> = listOf(
        PresetBuild(
            id = "preset_esports",
            name = "Esports Champion",
            tagline = "High-FPS 1080p/1440p Competitive Valorant & CS2 Rig",
            targetAudience = "Competitive Gamers & College Streamers",
            estimatedPriceInr = 82500,
            components = mapOf(
                ComponentCategory.CPU to "cpu_r5_7600",
                ComponentCategory.MOTHERBOARD to "mobo_b650m_ds3h",
                ComponentCategory.GPU to "gpu_rtx_4060",
                ComponentCategory.COOLER to "cooler_ag400",
                ComponentCategory.RAM to "ram_corsair_ddr5_32gb",
                ComponentCategory.SSD to "ssd_wd_sn580_1tb",
                ComponentCategory.PSU to "psu_msi_a650bn",
                ComponentCategory.CASE to "case_montech_air_903"
            )
        ),
        PresetBuild(
            id = "preset_4k_titan",
            name = "4K Titan Gaming",
            tagline = "Max Ray-Tracing 4K Cyberpunk & Black Myth Wukong Monster",
            targetAudience = "Enthusiast Gamers & VR Creators",
            estimatedPriceInr = 210000,
            components = mapOf(
                ComponentCategory.CPU to "cpu_r7_7800x3d",
                ComponentCategory.MOTHERBOARD to "mobo_b650_gaming_plus",
                ComponentCategory.GPU to "gpu_rtx_4080_super",
                ComponentCategory.COOLER to "cooler_ak620",
                ComponentCategory.RAM to "ram_corsair_ddr5_32gb",
                ComponentCategory.SSD to "ssd_samsung_990pro_2tb",
                ComponentCategory.PSU to "psu_corsair_rm850e",
                ComponentCategory.CASE to "case_lian_li_216"
            )
        ),
        PresetBuild(
            id = "preset_ai_workstation",
            name = "AI & Deep Learning Station",
            tagline = "24GB VRAM LLM Inference, PyTorch Training & Blender CAD Rig",
            targetAudience = "AI Researchers, Data Scientists & 3D Animators",
            estimatedPriceInr = 325000,
            components = mapOf(
                ComponentCategory.CPU to "cpu_i7_14700k",
                ComponentCategory.MOTHERBOARD to "mobo_x670e_aorus",
                ComponentCategory.GPU to "gpu_rtx_4090",
                ComponentCategory.COOLER to "cooler_liquid_360",
                ComponentCategory.RAM to "ram_gskill_ddr5_64gb",
                ComponentCategory.SSD to "ssd_samsung_990pro_2tb",
                ComponentCategory.PSU to "psu_antec_c1000",
                ComponentCategory.CASE to "case_lian_li_216"
            )
        ),
        PresetBuild(
            id = "preset_budget_office",
            name = "Budget Office & Student",
            tagline = "Ultra-reliable daily productivity, coding & media playback",
            targetAudience = "Students, Office Workstations & Tech Shops",
            estimatedPriceInr = 30500,
            components = mapOf(
                ComponentCategory.CPU to "cpu_r5_5600g",
                ComponentCategory.MOTHERBOARD to "mobo_b450m_k",
                ComponentCategory.GPU to "gpu_rx_7600",
                ComponentCategory.COOLER to "cooler_wraith_stock",
                ComponentCategory.RAM to "ram_crucial_ddr4_16gb",
                ComponentCategory.SSD to "ssd_crucial_p3_500gb",
                ComponentCategory.PSU to "psu_ant_vs500",
                ComponentCategory.CASE to "case_ant_ice_100"
            )
        )
    )

    fun findById(id: String): ComponentItem? = allComponents.find { it.id == id }
}
