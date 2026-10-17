package com.example.engine

import com.example.model.ClearanceReport
import com.example.model.ComponentCategory
import com.example.model.ComponentItem

object SpecCheckerEngine {

    fun evaluate(slots: Map<ComponentCategory, ComponentItem?>): ClearanceReport {
        val cpu = slots[ComponentCategory.CPU]
        val mobo = slots[ComponentCategory.MOTHERBOARD]
        val gpu = slots[ComponentCategory.GPU]
        val cooler = slots[ComponentCategory.COOLER]
        val ram = slots[ComponentCategory.RAM]
        val ssd = slots[ComponentCategory.SSD]
        val psu = slots[ComponentCategory.PSU]
        val case = slots[ComponentCategory.CASE]

        // 1. GPU Clearance Check
        val gpuLength = gpu?.dimensionsMm?.lengthMm ?: 0
        val caseMaxGpu = case?.maxGpuLengthMm ?: 0
        val isGpuFitting = if (gpu != null && case != null && caseMaxGpu > 0 && gpuLength > 0) {
            gpuLength <= caseMaxGpu
        } else {
            true
        }
        val gpuMargin = if (caseMaxGpu > 0 && gpuLength > 0) caseMaxGpu - gpuLength else 0

        // 2. CPU Cooler Clearance Check
        val coolerHeight = cooler?.dimensionsMm?.heightMm ?: 0
        val caseMaxCooler = case?.maxCoolerHeightMm ?: 0
        val isCoolerFitting = if (cooler != null && case != null && caseMaxCooler > 0 && coolerHeight > 0) {
            coolerHeight <= caseMaxCooler
        } else {
            true
        }
        val coolerMargin = if (caseMaxCooler > 0 && coolerHeight > 0) caseMaxCooler - coolerHeight else 0

        // 3. Socket Compatibility
        val cpuSocket = cpu?.socketType.orEmpty()
        val moboSocket = mobo?.socketType.orEmpty()
        val isSocketMatched = if (cpu != null && mobo != null && cpuSocket.isNotEmpty() && moboSocket.isNotEmpty()) {
            cpuSocket.equals(moboSocket, ignoreCase = true)
        } else {
            true
        }

        // 4. Form Factor Compatibility
        val moboFormFactor = mobo?.formFactor.orEmpty()
        val caseSupported = case?.supportedFormFactors ?: emptyList()
        val isFormFactorSupported = if (mobo != null && case != null && moboFormFactor.isNotEmpty() && caseSupported.isNotEmpty()) {
            caseSupported.any { it.contains(moboFormFactor, ignoreCase = true) }
        } else {
            true
        }

        // 5. TDP & Power Headroom
        val baseSystemOverheadWatts = 45 // Chipset, 3-4 cabinet fans, RGB controllers
        val totalTdp = (cpu?.tdpWatts ?: 0) +
                (mobo?.tdpWatts ?: 0) +
                (gpu?.tdpWatts ?: 0) +
                (cooler?.tdpWatts ?: 0) +
                (ram?.tdpWatts ?: 0) +
                (ssd?.tdpWatts ?: 0) +
                baseSystemOverheadWatts

        // Recommended PSU with 30% headroom buffer
        val rawRecommended = (totalTdp * 1.35).toInt()
        val recommendedPsu = when {
            rawRecommended <= 550 -> 550
            rawRecommended <= 650 -> 650
            rawRecommended <= 750 -> 750
            rawRecommended <= 850 -> 850
            rawRecommended <= 1000 -> 1000
            else -> 1200
        }

        val psuCapacity = psu?.wattageRating ?: 0
        val tdpHeadroom = if (psuCapacity > 0) psuCapacity - totalTdp else 0
        val isPsuAdequate = if (psuCapacity > 0) tdpHeadroom >= 50 else true

        // 6. Radiator & PCIe checks
        val isRadiatorCleared = if (cooler != null && cooler.dimensionsMm.lengthMm >= 360) {
            case != null && (case.id == "case_lian_li_o11_evo" || case.id == "case_lian_li_216" || case.id == "case_montech_air_903")
        } else {
            true
        }

        val isPcieGen5Cleared = if (gpu != null && gpu.specs.contains("PCIe 5.0", ignoreCase = true)) {
            mobo?.specs?.contains("PCIe 5.0", ignoreCase = true) ?: true
        } else {
            true
        }

        val allPass = isGpuFitting && isCoolerFitting && isSocketMatched && isFormFactorSupported && isPsuAdequate && isRadiatorCleared

        // Compute compatibility score (0-100)
        var score = 100
        if (!isSocketMatched) score -= 35
        if (!isGpuFitting) score -= 25
        if (!isCoolerFitting) score -= 20
        if (!isPsuAdequate) score -= 18
        if (!isFormFactorSupported) score -= 15
        if (!isRadiatorCleared) score -= 10
        // If DDR5 is equipped with AMD CPU, check for 6000MT/s advisory
        if (cpuSocket.equals("AM5", ignoreCase = true) && ram != null && !ram.specs.contains("6000", ignoreCase = true)) {
            score -= 6
        }
        val finalScore = score.coerceIn(15, 100)
        val fitStatusText = when {
            finalScore >= 90 -> "OPTIMAL FIT"
            finalScore >= 75 -> "GOOD FIT"
            finalScore >= 50 -> "ADVISORY"
            else -> "ACTION REQUIRED"
        }

        return ClearanceReport(
            compatibilityScore = finalScore,
            fitStatusText = fitStatusText,

            isGpuFitting = isGpuFitting,
            gpuMarginMm = gpuMargin,
            gpuLengthMm = gpuLength,
            caseMaxGpuLengthMm = caseMaxGpu,

            isCoolerFitting = isCoolerFitting,
            coolerMarginMm = coolerMargin,
            coolerHeightMm = coolerHeight,
            caseMaxCoolerHeightMm = caseMaxCooler,

            isSocketMatched = isSocketMatched,
            cpuSocket = cpuSocket,
            motherboardSocket = moboSocket,

            isFormFactorSupported = isFormFactorSupported,
            motherboardFormFactor = moboFormFactor,
            caseSupportedFormFactors = caseSupported,

            isPcieGen5Cleared = isPcieGen5Cleared,
            isRadiatorCleared = isRadiatorCleared,
            ddr5ExpoAdvisory = if (cpuSocket.equals("AM5", ignoreCase = true)) {
                "DDR5 AMD EXPO 6000MT/s Profile 1 recommendation (Sweet Spot)"
            } else {
                "Intel XMP 3.0 DDR5 Profile 1 optimized"
            },

            totalTdpWatts = totalTdp,
            psuCapacityWatts = psuCapacity,
            tdpHeadroomWatts = tdpHeadroom,
            isPsuAdequate = isPsuAdequate,
            recommendedPsuWatts = recommendedPsu,

            allPass = allPass
        )
    }

    /**
     * Resolves physical clearances, socket mismatches, or inadequate PSU in 1-Click
     */
    fun autoFixRig(slots: Map<ComponentCategory, ComponentItem?>): Map<ComponentCategory, ComponentItem?> {
        val mutable = slots.toMutableMap()
        val report = evaluate(slots)

        // 1. Fix Socket Mismatch
        if (!report.isSocketMatched) {
            val cpu = mutable[ComponentCategory.CPU]
            if (cpu != null) {
                val matchingMobo = com.example.data.HardwareCatalog.allComponents
                    .filter { it.category == ComponentCategory.MOTHERBOARD && it.socketType.equals(cpu.socketType, ignoreCase = true) }
                    .minByOrNull { it.getLowestPriceInr() }
                if (matchingMobo != null) {
                    mutable[ComponentCategory.MOTHERBOARD] = matchingMobo
                }
            }
        }

        // 2. Fix Inadequate PSU
        if (!report.isPsuAdequate) {
            val neededWattage = report.recommendedPsuWatts
            val adequatePsu = com.example.data.HardwareCatalog.allComponents
                .filter { it.category == ComponentCategory.PSU && it.wattageRating >= neededWattage }
                .minByOrNull { it.getLowestPriceInr() }
            if (adequatePsu != null) {
                mutable[ComponentCategory.PSU] = adequatePsu
            }
        }

        // 3. Fix GPU / Cooler / Radiator clearance
        if (!report.isGpuFitting || !report.isCoolerFitting || !report.isRadiatorCleared) {
            val spaciousCase = com.example.data.HardwareCatalog.findById("case_lian_li_o11_evo")
                ?: com.example.data.HardwareCatalog.findById("case_lian_li_216")
            if (spaciousCase != null) {
                mutable[ComponentCategory.CASE] = spaciousCase
            }
        }

        return mutable
    }
}
