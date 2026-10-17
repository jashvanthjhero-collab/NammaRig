package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HardwareCatalog
import com.example.engine.ShoppingSlipEngine
import com.example.engine.SpecCheckerEngine
import com.example.model.ComponentCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun readStringFromContext_matchesNammaRig() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("NammaRig", appName)
    }

    @Test
    fun specCheckerEngine_validatesEsportsPresetClearance() {
        val preset = HardwareCatalog.presetBuilds.first { it.id == "preset_esports" }
        val slots = mutableMapOf<ComponentCategory, com.example.model.ComponentItem?>()
        ComponentCategory.entries.forEach { cat ->
            val id = preset.components[cat]
            if (id != null) {
                slots[cat] = HardwareCatalog.findById(id)
            }
        }

        val report = SpecCheckerEngine.evaluate(slots)

        assertTrue("GPU should fit inside case", report.isGpuFitting)
        assertTrue("GPU margin should be positive", report.gpuMarginMm > 0)
        assertTrue("CPU Cooler should fit inside case", report.isCoolerFitting)
        assertTrue("Socket should match AM5", report.isSocketMatched)
        assertTrue("PSU should be adequate", report.isPsuAdequate)
        assertTrue("Overall clearance check should pass", report.allPass)
    }

    @Test
    fun shoppingSlipEngine_groupsByStreetLanesAndGeneratesWhatsAppQuotes() {
        val preset = HardwareCatalog.presetBuilds.first { it.id == "preset_esports" }
        val slots = mutableMapOf<ComponentCategory, com.example.model.ComponentItem?>()
        ComponentCategory.entries.forEach { cat ->
            val id = preset.components[cat]
            if (id != null) {
                slots[cat] = HardwareCatalog.findById(id)
            }
        }

        val summary = ShoppingSlipEngine.buildShoppingSlip(slots)

        assertTrue("Should have lane clusters", summary.clusters.isNotEmpty())
        assertTrue("Total build cost should be greater than zero", summary.totalBuildCostInr > 0)
        assertTrue("Walking distance should be estimated", summary.estimatedWalkingMeters > 0)

        val firstCluster = summary.clusters.first()
        val firstStop = firstCluster.dealerStops.first()
        val waUri = firstStop.getWhatsAppIntentUri()

        assertTrue("WhatsApp URI must be formatted", waUri.startsWith("https://api.whatsapp.com/send?phone="))
        assertTrue("WhatsApp message must contain Vanakkam", waUri.contains("Vanakkam"))
    }
}
