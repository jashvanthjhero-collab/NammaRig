package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.ComponentCategory
import com.example.model.ComponentItem
import org.json.JSONArray
import org.json.JSONObject

/**
 * Offline persistence manager for NammaRig.
 * Caches the user's active build slots, checked items on the shopping slip,
 * saved custom rigs, and selected Chennai location hub so that everything
 * remains completely accessible offline while walking through Ritchie Street lanes.
 */
class RigPersistenceManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("nammarig_offline_cache", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SLOTS_PREFIX = "rig_slot_"
        private const val KEY_CHECKED_SHOPPING_ITEMS = "checked_shopping_items"
        private const val KEY_SELECTED_LOCATION = "selected_location"
        private const val KEY_SAVED_RIGS = "saved_custom_rigs"
        const val DEFAULT_LOCATION = "Chennai — Ritchie Street"
    }

    /**
     * Save active build slots (maps ComponentCategory to componentId)
     */
    fun saveBuild(slots: Map<ComponentCategory, ComponentItem?>) {
        val editor = prefs.edit()
        ComponentCategory.entries.forEach { cat ->
            val item = slots[cat]
            if (item != null) {
                editor.putString(KEY_SLOTS_PREFIX + cat.name, item.id)
            } else {
                editor.remove(KEY_SLOTS_PREFIX + cat.name)
            }
        }
        editor.apply()
    }

    /**
     * Load active build slots from cache.
     * Returns null if no cached slots are found (so caller can load default preset).
     */
    fun loadBuild(): Map<ComponentCategory, ComponentItem?>? {
        var foundAny = false
        val map = mutableMapOf<ComponentCategory, ComponentItem?>()
        ComponentCategory.entries.forEach { cat ->
            val compId = prefs.getString(KEY_SLOTS_PREFIX + cat.name, null)
            if (compId != null) {
                foundAny = true
                map[cat] = HardwareCatalog.findById(compId)
            } else {
                map[cat] = null
            }
        }
        return if (foundAny) map else null
    }

    /**
     * Clear active build cache
     */
    fun clearBuild() {
        val editor = prefs.edit()
        ComponentCategory.entries.forEach { cat ->
            editor.remove(KEY_SLOTS_PREFIX + cat.name)
        }
        editor.apply()
    }

    /**
     * Save checked item IDs for counter checklist
     */
    fun saveCheckedItems(checkedIds: Set<String>) {
        prefs.edit().putStringSet(KEY_CHECKED_SHOPPING_ITEMS, checkedIds).apply()
    }

    /**
     * Load checked item IDs
     */
    fun loadCheckedItems(): Set<String> {
        return prefs.getStringSet(KEY_CHECKED_SHOPPING_ITEMS, emptySet()) ?: emptySet()
    }

    /**
     * Save selected location
     */
    fun saveSelectedLocation(location: String) {
        prefs.edit().putString(KEY_SELECTED_LOCATION, location).apply()
    }

    /**
     * Load selected location
     */
    fun loadSelectedLocation(): String {
        return prefs.getString(KEY_SELECTED_LOCATION, DEFAULT_LOCATION) ?: DEFAULT_LOCATION
    }

    /**
     * Save custom named build
     */
    fun saveCustomRig(name: String, slots: Map<ComponentCategory, ComponentItem?>) {
        val jsonArray = getSavedRigsJson()
        val rigObj = JSONObject()
        rigObj.put("id", "rig_${System.currentTimeMillis()}")
        rigObj.put("name", name)
        rigObj.put("timestamp", System.currentTimeMillis())

        val componentsObj = JSONObject()
        slots.forEach { (cat, item) ->
            if (item != null) {
                componentsObj.put(cat.name, item.id)
            }
        }
        rigObj.put("components", componentsObj)
        jsonArray.put(rigObj)

        prefs.edit().putString(KEY_SAVED_RIGS, jsonArray.toString()).apply()
    }

    fun getSavedRigsJson(): JSONArray {
        val raw = prefs.getString(KEY_SAVED_RIGS, null)
        return if (raw != null) {
            try {
                JSONArray(raw)
            } catch (e: Exception) {
                JSONArray()
            }
        } else {
            JSONArray()
        }
    }
}
