package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.data.HardwareCatalog
import com.example.data.RigPersistenceManager
import com.example.engine.ShoppingSlipEngine
import com.example.engine.ShoppingSlipSummary
import com.example.engine.SpecCheckerEngine
import com.example.model.ClearanceReport
import com.example.model.ComponentCategory
import com.example.model.ComponentItem
import com.example.model.PresetBuild
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppTab(val title: String) {
    RIG_HUB("Rig Hub"),
    CAD_STUDIO("CAD Studio"),
    BLUEPRINT("Blueprint"),
    HUNTER("Hunter"),
    PRICE_PULSE("Price Pulse")
}

data class RigUiState(
    val currentTab: AppTab = AppTab.RIG_HUB,
    val slots: Map<ComponentCategory, ComponentItem?> = emptyMap(),
    val clearanceReport: ClearanceReport = ClearanceReport(),
    val shoppingSlip: ShoppingSlipSummary = ShoppingSlipSummary(
        clusters = emptyList(),
        totalBuildCostInr = 0,
        totalAverageCostInr = 0,
        totalSavingsInr = 0,
        totalItemCount = 0,
        estimatedWalkingMeters = 0,
        estimatedWalkingMinutes = 0,
        walkingRouteDescription = ""
    ),
    val activePresetId: String? = null,
    val highlightedSlot: ComponentCategory? = null,
    val selectedLocation: String = "Chennai — Ritchie Street",
    val checkedShoppingItems: Set<String> = emptySet(),
    val toastMessage: String? = null,

    // Hunter filters
    val hunterCategory: ComponentCategory? = null,
    val hunterSearchQuery: String = "",
    val hunterBudgetMax: Float = 150000f,
    val hunterSelectedDealer: String = "All",
    val hunterInStockOnly: Boolean = false,
    val hunterSelectedBrand: String = "All",

    // Dealers Screen filters
    val dealerLaneFilter: String = "All Lanes",
    val dealerSearchQuery: String = ""
)

class RigViewModel(application: Application) : AndroidViewModel(application) {

    private val persistence = RigPersistenceManager(application.applicationContext)

    private val _uiState = MutableStateFlow(RigUiState())
    val uiState: StateFlow<RigUiState> = _uiState.asStateFlow()

    init {
        val cachedLocation = persistence.loadSelectedLocation()
        val cachedChecked = persistence.loadCheckedItems()
        val cachedSlots = persistence.loadBuild()

        if (cachedSlots != null && cachedSlots.values.any { it != null }) {
            val report = SpecCheckerEngine.evaluate(cachedSlots)
            val slip = ShoppingSlipEngine.buildShoppingSlip(cachedSlots)
            _uiState.update {
                it.copy(
                    slots = cachedSlots,
                    clearanceReport = report,
                    shoppingSlip = slip,
                    selectedLocation = cachedLocation,
                    checkedShoppingItems = cachedChecked
                )
            }
        } else {
            // Load default Esports Champion preset on first launch
            val defaultPreset = HardwareCatalog.presetBuilds.first()
            loadPreset(defaultPreset)
            _uiState.update {
                it.copy(
                    selectedLocation = cachedLocation,
                    checkedShoppingItems = cachedChecked
                )
            }
        }
    }

    fun switchTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun equipComponent(category: ComponentCategory, item: ComponentItem) {
        _uiState.update { state ->
            val updatedSlots = state.slots.toMutableMap().apply {
                put(category, item)
            }
            val report = SpecCheckerEngine.evaluate(updatedSlots)
            val slip = ShoppingSlipEngine.buildShoppingSlip(updatedSlots)
            persistence.saveBuild(updatedSlots)
            state.copy(
                slots = updatedSlots,
                clearanceReport = report,
                shoppingSlip = slip,
                activePresetId = null,
                highlightedSlot = category,
                toastMessage = "Added ${item.name} to ${category.shortName} ⚡"
            )
        }
    }

    fun removeComponent(category: ComponentCategory) {
        _uiState.update { state ->
            val updatedSlots = state.slots.toMutableMap().apply {
                remove(category)
            }
            val report = SpecCheckerEngine.evaluate(updatedSlots)
            val slip = ShoppingSlipEngine.buildShoppingSlip(updatedSlots)
            persistence.saveBuild(updatedSlots)
            state.copy(
                slots = updatedSlots,
                clearanceReport = report,
                shoppingSlip = slip,
                activePresetId = null,
                highlightedSlot = if (state.highlightedSlot == category) null else state.highlightedSlot
            )
        }
    }

    fun loadPreset(preset: PresetBuild) {
        val newSlots = mutableMapOf<ComponentCategory, ComponentItem?>()
        ComponentCategory.entries.forEach { cat ->
            val compId = preset.components[cat]
            if (compId != null) {
                newSlots[cat] = HardwareCatalog.findById(compId)
            } else {
                newSlots[cat] = null
            }
        }
        val report = SpecCheckerEngine.evaluate(newSlots)
        val slip = ShoppingSlipEngine.buildShoppingSlip(newSlots)
        persistence.saveBuild(newSlots)

        _uiState.update { state ->
            state.copy(
                slots = newSlots,
                clearanceReport = report,
                shoppingSlip = slip,
                activePresetId = preset.id,
                highlightedSlot = null,
                toastMessage = "Loaded ${preset.name} preset!"
            )
        }
    }

    fun clearBuild() {
        val emptySlots = ComponentCategory.entries.associateWith { null }
        val report = SpecCheckerEngine.evaluate(emptySlots)
        val slip = ShoppingSlipEngine.buildShoppingSlip(emptySlots)
        persistence.clearBuild()
        _uiState.update { state ->
            state.copy(
                slots = emptySlots,
                clearanceReport = report,
                shoppingSlip = slip,
                activePresetId = null,
                highlightedSlot = null,
                toastMessage = "Build cleared"
            )
        }
    }

    fun autoFixRig() {
        val currentSlots = _uiState.value.slots
        val fixedSlots = SpecCheckerEngine.autoFixRig(currentSlots)
        val report = SpecCheckerEngine.evaluate(fixedSlots)
        val slip = ShoppingSlipEngine.buildShoppingSlip(fixedSlots)
        persistence.saveBuild(fixedSlots)

        _uiState.update { state ->
            state.copy(
                slots = fixedSlots,
                clearanceReport = report,
                shoppingSlip = slip,
                toastMessage = "Rig Auto-Fixed: Upgraded components for optimal fit & clearance! ✓"
            )
        }
    }

    fun toggleShoppingItemChecked(itemId: String) {
        _uiState.update { state ->
            val updated = state.checkedShoppingItems.toMutableSet()
            if (updated.contains(itemId)) {
                updated.remove(itemId)
            } else {
                updated.add(itemId)
            }
            persistence.saveCheckedItems(updated)
            state.copy(checkedShoppingItems = updated)
        }
    }

    fun setSelectedLocation(location: String) {
        persistence.saveSelectedLocation(location)
        _uiState.update { it.copy(selectedLocation = location) }
    }

    fun highlightSlot(category: ComponentCategory?) {
        _uiState.update { it.copy(highlightedSlot = category) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    // Hunter controls
    fun setHunterCategory(category: ComponentCategory?) {
        _uiState.update { it.copy(hunterCategory = category) }
    }

    fun setHunterSearchQuery(query: String) {
        _uiState.update { it.copy(hunterSearchQuery = query) }
    }

    fun setHunterBudgetMax(budget: Float) {
        _uiState.update { it.copy(hunterBudgetMax = budget) }
    }

    fun setHunterSelectedDealer(dealer: String) {
        _uiState.update { it.copy(hunterSelectedDealer = dealer) }
    }

    fun setHunterSelectedBrand(brand: String) {
        _uiState.update { it.copy(hunterSelectedBrand = brand) }
    }

    fun setHunterInStockOnly(inStock: Boolean) {
        _uiState.update { it.copy(hunterInStockOnly = inStock) }
    }

    fun resetHunterFilters() {
        _uiState.update {
            it.copy(
                hunterCategory = null,
                hunterSearchQuery = "",
                hunterBudgetMax = 150000f,
                hunterSelectedDealer = "All",
                hunterSelectedBrand = "All",
                hunterInStockOnly = false
            )
        }
    }

    // Dealer tab controls
    fun setDealerLaneFilter(lane: String) {
        _uiState.update { it.copy(dealerLaneFilter = lane) }
    }

    fun setDealerSearchQuery(query: String) {
        _uiState.update { it.copy(dealerSearchQuery = query) }
    }
}
