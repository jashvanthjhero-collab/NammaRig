package com.example.model

enum class ComponentCategory(val displayName: String, val shortName: String) {
    CPU("Processor (CPU)", "CPU"),
    MOTHERBOARD("Motherboard", "Mobo"),
    GPU("Graphics Card (GPU)", "GPU"),
    COOLER("CPU Cooler", "Cooler"),
    RAM("Memory (RAM)", "RAM"),
    SSD("Storage (NVMe SSD)", "SSD"),
    PSU("Power Supply (PSU)", "PSU"),
    CASE("Chassis (Cabinet)", "Case"),
    ACCESSORIES("Monitor & Peripherals", "Acc")
}

data class ComponentDimensions(
    val lengthMm: Int = 0,
    val heightMm: Int = 0,
    val widthMm: Int = 0
)

data class DealerPrice(
    val dealerId: String,
    val dealerName: String,
    val streetLane: String,
    val priceInr: Int,
    val inStock: Boolean = true,
    val warrantyYears: Int = 3,
    val phone: String = "+91 44 2841 0000",
    val whatsappNumber: String = "919840123456"
)

data class ComponentItem(
    val id: String,
    val name: String,
    val category: ComponentCategory,
    val brand: String,
    val specs: String,
    val tdpWatts: Int = 0,
    val dimensionsMm: ComponentDimensions = ComponentDimensions(),
    val socketType: String = "",
    val formFactor: String = "",
    val wattageRating: Int = 0, // For PSU
    val maxGpuLengthMm: Int = 0, // For Case
    val maxCoolerHeightMm: Int = 0, // For Case
    val supportedFormFactors: List<String> = emptyList(), // For Case
    val dealerPrices: List<DealerPrice> = emptyList()
) {
    fun getLowestPrice(): DealerPrice? = dealerPrices.minByOrNull { it.priceInr }
    fun getLowestPriceInr(): Int = getLowestPrice()?.priceInr ?: 0
    fun getAveragePriceInr(): Int = if (dealerPrices.isNotEmpty()) {
        dealerPrices.map { it.priceInr }.average().toInt()
    } else 0
}

data class RigSlot(
    val category: ComponentCategory,
    val selectedItem: ComponentItem? = null
)

data class PresetBuild(
    val id: String,
    val name: String,
    val tagline: String,
    val targetAudience: String,
    val estimatedPriceInr: Int,
    val components: Map<ComponentCategory, String> // category to componentId
)

data class ClearanceReport(
    val compatibilityScore: Int = 94,
    val fitStatusText: String = "OPTIMAL FIT",

    val isGpuFitting: Boolean = true,
    val gpuMarginMm: Int = 0,
    val gpuLengthMm: Int = 0,
    val caseMaxGpuLengthMm: Int = 0,

    val isCoolerFitting: Boolean = true,
    val coolerMarginMm: Int = 0,
    val coolerHeightMm: Int = 0,
    val caseMaxCoolerHeightMm: Int = 0,

    val isSocketMatched: Boolean = true,
    val cpuSocket: String = "",
    val motherboardSocket: String = "",

    val isFormFactorSupported: Boolean = true,
    val motherboardFormFactor: String = "",
    val caseSupportedFormFactors: List<String> = emptyList(),

    val isPcieGen5Cleared: Boolean = true,
    val isRadiatorCleared: Boolean = true,
    val ddr5ExpoAdvisory: String = "DDR5 AMD EXPO 6000MT/s Profile 1 sweet spot recommended",

    val totalTdpWatts: Int = 0,
    val psuCapacityWatts: Int = 0,
    val tdpHeadroomWatts: Int = 0,
    val isPsuAdequate: Boolean = true,
    val recommendedPsuWatts: Int = 550,

    val allPass: Boolean = true
)
