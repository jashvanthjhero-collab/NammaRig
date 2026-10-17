package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HardwareCatalog
import com.example.model.ComponentCategory
import com.example.model.ComponentItem
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CanvasBg
import com.example.ui.theme.CardBg
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldLowestPrice
import com.example.ui.theme.GreenBg
import com.example.ui.theme.GreenLowest
import com.example.ui.theme.InkText
import com.example.ui.theme.InkTextPrimary
import com.example.ui.theme.InkTextSecondary
import com.example.ui.theme.MutedText
import com.example.ui.theme.VioletAccent
import com.example.ui.theme.VioletSoft
import com.example.viewmodel.AppTab
import com.example.viewmodel.RigUiState
import com.example.viewmodel.RigViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HunterScreen(
    viewModel: RigViewModel,
    uiState: RigUiState,
    modifier: Modifier = Modifier
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    var itemToCompare by remember { mutableStateOf<ComponentItem?>(null) }

    val categoryPills = listOf(
        null to "All Hardware",
        ComponentCategory.CPU to "Processors",
        ComponentCategory.GPU to "Graphics",
        ComponentCategory.MOTHERBOARD to "Motherboards",
        ComponentCategory.RAM to "Memory",
        ComponentCategory.SSD to "Storage",
        ComponentCategory.COOLER to "Coolers",
        ComponentCategory.PSU to "Power",
        ComponentCategory.CASE to "Cabinets"
    )

    val brands = listOf("All", "AMD", "Intel", "NVIDIA", "ASUS", "MSI", "Corsair", "Samsung", "Lian Li", "DeepCool", "G.Skill")

    val filteredList = HardwareCatalog.allComponents.filter { item ->
        val matchesCategory = uiState.hunterCategory == null || item.category == uiState.hunterCategory
        val matchesBudget = item.getLowestPriceInr() <= uiState.hunterBudgetMax
        val matchesBrand = uiState.hunterSelectedBrand == "All" || item.brand.equals(uiState.hunterSelectedBrand, ignoreCase = true)
        val matchesInStock = !uiState.hunterInStockOnly || item.dealerPrices.any { it.inStock }

        val query = uiState.hunterSearchQuery.trim()
        val matchesSearch = query.isEmpty() ||
                item.name.contains(query, ignoreCase = true) ||
                item.brand.contains(query, ignoreCase = true) ||
                item.specs.contains(query, ignoreCase = true) ||
                item.socketType.contains(query, ignoreCase = true)

        matchesCategory && matchesBudget && matchesBrand && matchesInStock && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBg)
            .testTag("hunter_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Header
        item {
            Column {
                Text(
                    text = "COMPONENT HUNTER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    ),
                    color = VioletAccent
                )
                Text(
                    text = "Hardware Catalog & Deal Finder",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = InkText
                )
            }
        }

        // Search Bar with Filter Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.hunterSearchQuery,
                    onValueChange = { viewModel.setHunterSearchQuery(it) },
                    placeholder = { Text("Search CPU, GPU, brand, socket...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = VioletAccent
                        )
                    },
                    trailingIcon = {
                        if (uiState.hunterSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setHunterSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletAccent,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = CardBg,
                        unfocusedContainerColor = CardBg
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hunter_search_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    onClick = { showFilterSheet = true },
                    shape = RoundedCornerShape(12.dp),
                    color = if (uiState.hunterSelectedBrand != "All" || uiState.hunterInStockOnly || uiState.hunterBudgetMax < 150000f) VioletAccent else CardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.size(52.dp).testTag("hunter_filter_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter Options",
                            tint = if (uiState.hunterSelectedBrand != "All" || uiState.hunterInStockOnly || uiState.hunterBudgetMax < 150000f) Color.White else InkText
                        )
                    }
                }
            }
        }

        // Swipeable Category Pills
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoryPills.forEach { (cat, label) ->
                    val isSelected = uiState.hunterCategory == cat

                    ElevatedFilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setHunterCategory(cat) },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        },
                        colors = FilterChipDefaults.elevatedFilterChipColors(
                            selectedContainerColor = VioletAccent,
                            selectedLabelColor = Color.White,
                            containerColor = CardBg,
                            labelColor = InkText
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("hunter_chip_${cat?.name?.lowercase() ?: "all"}")
                    )
                }
            }
        }

        // Budget Slider Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = VioletAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MAX BUDGET FILTER",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                ),
                                color = MutedText
                            )
                        }

                        Text(
                            text = "Up to ₹${"%,d".format(uiState.hunterBudgetMax.toInt())}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            ),
                            color = VioletAccent
                        )
                    }

                    Slider(
                        value = uiState.hunterBudgetMax,
                        onValueChange = { viewModel.setHunterBudgetMax(it) },
                        valueRange = 5000f..150000f,
                        steps = 29,
                        colors = SliderDefaults.colors(
                            thumbColor = VioletAccent,
                            activeTrackColor = VioletAccent,
                            inactiveTrackColor = BorderColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("budget_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "₹5,000",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedText
                        )
                        Text(
                            text = "₹1,50,000+",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedText
                        )
                    }
                }
            }
        }

        // Active Filter Indicators (Brand, in-stock)
        if (uiState.hunterSelectedBrand != "All" || uiState.hunterInStockOnly) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (uiState.hunterSelectedBrand != "All") {
                            Surface(
                                color = VioletSoft,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Brand: ${uiState.hunterSelectedBrand}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VioletAccent
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = VioletAccent,
                                        modifier = Modifier.size(12.dp).clickable { viewModel.setHunterSelectedBrand("All") }
                                    )
                                }
                            }
                        }

                        if (uiState.hunterInStockOnly) {
                            Surface(
                                color = GreenBg,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "In-Stock Only",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = GreenLowest
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = GreenLowest,
                                        modifier = Modifier.size(12.dp).clickable { viewModel.setHunterInStockOnly(false) }
                                    )
                                }
                            }
                        }
                    }

                    TextButton(onClick = { viewModel.resetHunterFilters() }) {
                        Text("Reset", color = VioletAccent, fontSize = 11.sp)
                    }
                }
            }
        }

        // Results Header
        item {
            Text(
                text = "${filteredList.size} COMPONENTS FOUND",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                ),
                color = MutedText
            )
        }

        // Product Cards
        items(filteredList) { item ->
            val lowestPrice = item.getLowestPrice()
            val isEquipped = uiState.slots[item.category]?.id == item.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hunter_item_${item.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = androidx.compose.foundation.BorderStroke(
                    if (isEquipped) 1.5.dp else 1.dp,
                    if (isEquipped) VioletAccent else BorderColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = VioletSoft,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = item.category.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = VioletAccent,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = InkText
                            )
                            Text(
                                text = item.specs,
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedText,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${"%,d".format(item.getLowestPriceInr())}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = GreenLowest
                            )
                            if (lowestPrice != null) {
                                Text(
                                    text = "@ ${lowestPrice.dealerName}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MutedText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (item.tdpWatts > 0) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = MutedText,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${item.tdpWatts}W TDP",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedText
                                )
                            }
                            if (item.dimensionsMm.lengthMm > 0) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "• ${item.dimensionsMm.lengthMm}mm",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedText
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { itemToCompare = item },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, VioletAccent),
                                modifier = Modifier.testTag("compare_btn_${item.id}")
                            ) {
                                Text(
                                    text = "Compare Shops",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VioletAccent
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.equipComponent(item.category, item)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isEquipped) GreenLowest else VioletAccent
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("equip_btn_${item.id}")
                            ) {
                                Icon(
                                    imageVector = if (isEquipped) Icons.Default.Check else Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isEquipped) "✓ Added" else "+ Add",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Filter Modal Bottom Sheet
    if (showFilterSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState,
            containerColor = CardBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filter Hardware",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = InkText
                    )
                    TextButton(onClick = { viewModel.resetHunterFilters() }) {
                        Text("Reset Filters", color = VioletAccent)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Brand Pills
                Text(
                    text = "BRAND SELECTOR",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MutedText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    brands.forEach { brand ->
                        val isSelected = uiState.hunterSelectedBrand == brand
                        ElevatedFilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setHunterSelectedBrand(brand) },
                            label = { Text(brand) },
                            colors = FilterChipDefaults.elevatedFilterChipColors(
                                selectedContainerColor = VioletAccent,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // In-Stock Only Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "In-Stock Only",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = InkText
                        )
                        Text(
                            text = "Show only components currently on dealer shelves",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedText
                        )
                    }
                    Switch(
                        checked = uiState.hunterInStockOnly,
                        onCheckedChange = { viewModel.setHunterInStockOnly(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = VioletAccent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showFilterSheet = false },
                    colors = ButtonDefaults.buttonColors(containerColor = VioletAccent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Filters (${filteredList.size} Results)", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Compare Shops Dialog
    itemToCompare?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToCompare = null },
            title = {
                Column {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = InkText
                    )
                    Text(
                        text = "Chennai Dealer Price Comparison",
                        style = MaterialTheme.typography.labelSmall,
                        color = VioletAccent
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item.dealerPrices.sortedBy { it.priceInr }.forEach { dealerPrice ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = CanvasBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = dealerPrice.dealerName,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = InkText
                                    )
                                    Text(
                                        text = dealerPrice.streetLane,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = MutedText
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "₹${"%,d".format(dealerPrice.priceInr)}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace
                                        ),
                                        color = GreenLowest
                                    )
                                    Text(
                                        text = if (dealerPrice.inStock) "In Stock ✓" else "Order on Request",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (dealerPrice.inStock) GreenLowest else MutedText,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.equipComponent(item.category, item)
                        itemToCompare = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VioletAccent)
                ) {
                    Text("Add to Rig")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToCompare = null }) {
                    Text("Close", color = MutedText)
                }
            }
        )
    }
}
