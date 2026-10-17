package com.example.engine

import java.text.NumberFormat
import java.util.Locale

data class GstBreakdown(
    val baseAmount: Int,
    val cgst: Int, // 9% Central GST
    val sgst: Int, // 9% State GST (Tamil Nadu)
    val totalGst: Int, // 18% Total GST
    val totalAmount: Int // Inclusive Total
)

data class ShopSpreadSummary(
    val lowestTotal: Int,
    val highestTotal: Int,
    val averageTotal: Int,
    val potentialSavings: Int,
    val bestShopName: String
)

object PricingEngine {

    /**
     * Formats integer amount to Indian Rupee representation (e.g. ₹59,322)
     */
    fun formatInr(amount: Int): String {
        return try {
            val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            formatter.maximumFractionDigits = 0
            val formatted = formatter.format(amount)
            // Replace standard currency symbol if needed or use ₹ directly
            if (formatted.startsWith("INR")) {
                formatted.replace("INR", "₹").trim()
            } else if (!formatted.startsWith("₹")) {
                "₹$formatted"
            } else {
                formatted
            }
        } catch (e: Exception) {
            "₹${"%,d".format(amount)}"
        }
    }

    /**
     * Calculates 18% inclusive GST invoice breakdown for Chennai hardware retail purchases.
     */
    fun calculateGstInclusive(inclusiveTotal: Int): GstBreakdown {
        if (inclusiveTotal <= 0) {
            return GstBreakdown(0, 0, 0, 0, 0)
        }
        val base = (inclusiveTotal / 1.18).toInt()
        val totalGst = inclusiveTotal - base
        val cgst = totalGst / 2
        val sgst = totalGst - cgst

        return GstBreakdown(
            baseAmount = base,
            cgst = cgst,
            sgst = sgst,
            totalGst = totalGst,
            totalAmount = inclusiveTotal
        )
    }

    /**
     * Computes shop price spread savings across dealer quotes
     */
    fun calculateShopSpread(dealerPrices: List<Pair<String, Int>>): ShopSpreadSummary {
        val valid = dealerPrices.filter { it.second > 0 }
        if (valid.isEmpty()) {
            return ShopSpreadSummary(0, 0, 0, 0, "N/A")
        }
        val sorted = valid.sortedBy { it.second }
        val lowest = sorted.first()
        val highest = sorted.last()
        val avg = valid.map { it.second }.average().toInt()
        val savings = (highest.second - lowest.second).coerceAtLeast(0)

        return ShopSpreadSummary(
            lowestTotal = lowest.second,
            highestTotal = highest.second,
            averageTotal = avg,
            potentialSavings = savings,
            bestShopName = lowest.first
        )
    }
}
