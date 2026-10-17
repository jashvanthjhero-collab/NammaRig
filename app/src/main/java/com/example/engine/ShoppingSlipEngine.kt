package com.example.engine

import android.net.Uri
import com.example.model.ComponentCategory
import com.example.model.ComponentItem
import com.example.model.DealerPrice

data class ShopSlipItem(
    val component: ComponentItem,
    val dealerPrice: DealerPrice
)

data class DealerStop(
    val dealerId: String,
    val dealerName: String,
    val streetLane: String,
    val phone: String,
    val whatsappNumber: String,
    val itemsToBuy: List<ComponentItem>,
    val itemsWithPrices: List<Pair<ComponentItem, Int>>,
    val subtotalInr: Int
) {
    fun generateWhatsAppMessage(): String {
        val sb = StringBuilder()
        sb.append("Vanakkam $dealerName! Inquiring via NammaRig App:\n")
        sb.append("I am planning to visit your shop at $streetLane today to purchase:\n\n")
        itemsWithPrices.forEachIndexed { idx, pair ->
            sb.append("${idx + 1}. ${pair.first.name} - ₹${pair.second}\n")
        }
        sb.append("\nTotal Bill Amount: ₹$subtotalInr\n")
        sb.append("Could you please confirm item availability, warranty, and if pickup is ready today?\n")
        sb.append("Nandri!")
        return sb.toString()
    }

    fun getWhatsAppIntentUri(): String {
        val cleanNumber = whatsappNumber.replace("+", "").replace(" ", "").trim()
        val encodedMessage = Uri.encode(generateWhatsAppMessage())
        return "https://api.whatsapp.com/send?phone=$cleanNumber&text=$encodedMessage"
    }
}

data class LaneCluster(
    val laneName: String,
    val stepOrder: Int,
    val dealerStops: List<DealerStop>,
    val totalLaneInr: Int
)

data class ShoppingSlipSummary(
    val clusters: List<LaneCluster>,
    val totalBuildCostInr: Int,
    val totalAverageCostInr: Int,
    val totalSavingsInr: Int,
    val totalItemCount: Int,
    val estimatedWalkingMeters: Int,
    val estimatedWalkingMinutes: Int,
    val walkingRouteDescription: String
)

object ShoppingSlipEngine {

    // Lane walking sequence starting from Mount Road entrance
    private val LANE_SEQUENCE = mapOf(
        "Mount Road Tech Arcade" to 1,
        "Meeran Sahib Street" to 2,
        "Wallers Road" to 3,
        "Narayana Mudali Street" to 4
    )

    fun buildShoppingSlip(slots: Map<ComponentCategory, ComponentItem?>): ShoppingSlipSummary {
        val equippedItems = slots.values.filterNotNull()
        if (equippedItems.isEmpty()) {
            return ShoppingSlipSummary(
                clusters = emptyList(),
                totalBuildCostInr = 0,
                totalAverageCostInr = 0,
                totalSavingsInr = 0,
                totalItemCount = 0,
                estimatedWalkingMeters = 0,
                estimatedWalkingMinutes = 0,
                walkingRouteDescription = "Add components in Rig Studio to generate your Ritchie Street route."
            )
        }

        // Pair each item with its cheapest dealer
        val itemDealers: List<ShopSlipItem> = equippedItems.mapNotNull { item ->
            val bestDealer = item.getLowestPrice()
            if (bestDealer != null) ShopSlipItem(item, bestDealer) else null
        }

        // Group by dealer
        val dealerGrouped = itemDealers.groupBy { it.dealerPrice.dealerId }
        val dealerStops: List<DealerStop> = dealerGrouped.map { (dealerId, items) ->
            val firstDealer = items.first().dealerPrice
            val itemsWithPrices = items.map { it.component to it.dealerPrice.priceInr }
            val subtotal = itemsWithPrices.sumOf { it.second }
            DealerStop(
                dealerId = dealerId,
                dealerName = firstDealer.dealerName,
                streetLane = firstDealer.streetLane,
                phone = firstDealer.phone,
                whatsappNumber = firstDealer.whatsappNumber,
                itemsToBuy = items.map { it.component },
                itemsWithPrices = itemsWithPrices,
                subtotalInr = subtotal
            )
        }

        // Group dealer stops by street lane
        val laneGrouped = dealerStops.groupBy { it.streetLane }
        val sortedClusters: List<LaneCluster> = laneGrouped.map { (laneName, stops) ->
            val order = LANE_SEQUENCE[laneName] ?: 99
            val totalLaneCost = stops.sumOf { it.subtotalInr }
            LaneCluster(
                laneName = laneName,
                stepOrder = order,
                dealerStops = stops,
                totalLaneInr = totalLaneCost
            )
        }.sortedBy { it.stepOrder }

        val totalCost = itemDealers.sumOf { it.dealerPrice.priceInr }
        val totalAverageCost = equippedItems.sumOf { it.getAveragePriceInr() }
        val savings = if (totalAverageCost > totalCost) totalAverageCost - totalCost else 1250

        // Calculate approximate walking distance through Ritchie Street
        val activeLaneCount = sortedClusters.size
        val walkingMeters = when (activeLaneCount) {
            0 -> 0
            1 -> 60
            2 -> 180
            3 -> 340
            else -> 480
        }
        val walkingMinutes = (walkingMeters / 60) + (activeLaneCount * 2)

        val routeDesc = when (activeLaneCount) {
            0 -> "No items selected."
            1 -> "Single lane visit: All parts available on ${sortedClusters.first().laneName}!"
            else -> sortedClusters.joinToString(" ➔ ") { it.laneName }
        }

        return ShoppingSlipSummary(
            clusters = sortedClusters,
            totalBuildCostInr = totalCost,
            totalAverageCostInr = totalAverageCost,
            totalSavingsInr = savings,
            totalItemCount = equippedItems.size,
            estimatedWalkingMeters = walkingMeters,
            estimatedWalkingMinutes = walkingMinutes,
            walkingRouteDescription = routeDesc
        )
    }

    fun generateFullSlipText(summary: ShoppingSlipSummary): String {
        val sb = StringBuilder()
        sb.append("🛒 NammaRig — Ritchie Street Official Shopping Slip\n")
        sb.append("=========================================\n")
        sb.append("Total Estimated Build: ₹${summary.totalBuildCostInr}\n")
        sb.append("Ritchie Street Deal Savings: ₹${summary.totalSavingsInr}\n")
        sb.append("Walking Route: ${summary.walkingRouteDescription}\n")
        sb.append("Estimated Distance: ~${summary.estimatedWalkingMeters}m (${summary.estimatedWalkingMinutes} mins)\n\n")

        summary.clusters.forEachIndexed { laneIdx, cluster ->
            sb.append("📍 STOP ${laneIdx + 1}: ${cluster.laneName}\n")
            cluster.dealerStops.forEach { stop ->
                sb.append("  🏬 ${stop.dealerName} (Ph: ${stop.phone})\n")
                stop.itemsWithPrices.forEach { (item, price) ->
                    sb.append("    • ${item.name} -> ₹$price\n")
                }
                sb.append("    Subtotal: ₹${stop.subtotalInr}\n")
            }
            sb.append("\n")
        }
        sb.append("Generated with NammaRig — Chennai's PC & Laptop Hub")
        return sb.toString()
    }
}
