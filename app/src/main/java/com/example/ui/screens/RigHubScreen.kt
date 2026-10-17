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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HardwareCatalog
import com.example.data.PricePulseDrop
import com.example.model.ComponentCategory
import com.example.model.PresetBuild
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CanvasBg
import com.example.ui.theme.CardBg
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldLowestPrice
import com.example.ui.theme.GreenBg
import com.example.ui.theme.GreenLowest
import com.example.ui.theme.InkText
import com.example.ui.theme.MutedText
import com.example.ui.theme.VioletAccent
import com.example.ui.theme.VioletGlow
import com.example.ui.theme.VioletSoft
import com.example.viewmodel.AppTab
import com.example.viewmodel.RigUiState
import com.example.viewmodel.RigViewModel

@Composable
fun RigHubScreen(
    viewModel: RigViewModel,
    uiState: RigUiState,
    modifier: Modifier = Modifier
) {
    var showLocationDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    val locations = listOf(
        "Chennai — Ritchie Street",
        "Chennai — Mount Road",
        "Chennai — T. Nagar",
        "Chennai — Velachery"
    )

    // Find the spotlight CPU (Ryzen 9 9950X3D)
    val spotlightCpu = HardwareCatalog.allComponents.find { it.id == "cpu_r9_9950x3d" }
        ?: HardwareCatalog.allComponents.first { it.category == ComponentCategory.CPU }
    val isSpotlightEquipped = uiState.slots[ComponentCategory.CPU]?.id == spotlightCpu.id

    val filledCount = uiState.slots.values.count { it != null }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBg)
            .testTag("rig_hub_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Bar: NAMMARIG Bold Logo + Location Pill + Notification + Search
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Location pill
                Surface(
                    onClick = { showLocationDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    color = VioletSoft,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.testTag("location_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(GreenLowest)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = uiState.selectedLocation,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = VioletAccent
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Change Location",
                            tint = VioletAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Action icons: Search & Notifications
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.switchTab(AppTab.HUNTER) },
                        modifier = Modifier.size(36.dp).testTag("hub_search_icon")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Catalog",
                            tint = InkText,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = { showNotificationDialog = true },
                        modifier = Modifier.size(36.dp).testTag("hub_bell_icon")
                    ) {
                        Box {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Live Notifications",
                                tint = InkText,
                                modifier = Modifier.size(20.dp)
                            )
                            // Unread pulse dot
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(ElectricViolet)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }
            }
        }

        // 2. Active Build Quick Status Banner (if build exists)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.switchTab(AppTab.CAD_STUDIO) },
                shape = RoundedCornerShape(16.dp),
                color = CardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(VioletSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeveloperBoard,
                                contentDescription = null,
                                tint = VioletAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CURRENT RIG",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.8.sp,
                                        fontSize = 10.sp
                                    ),
                                    color = MutedText
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = GreenBg,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${uiState.clearanceReport.compatibilityScore}% Fit",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Black,
                                            fontSize = 9.sp
                                        ),
                                        color = GreenLowest,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "₹${"%,d".format(uiState.shoppingSlip.totalBuildCostInr)} • $filledCount/8 Parts",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = InkText
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "CAD Studio",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = VioletAccent
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = VioletAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // 3. Spotlight Hardware Card (Hero Card with Chennai Launch Badge)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("spotlight_hardware_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, VioletAccent.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Badge row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = VioletSoft,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "SPOTLIGHT HARDWARE · CHENNAI LAUNCH",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.7.sp,
                                    fontSize = 9.5.sp
                                ),
                                color = VioletAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = GreenBg,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Save ₹9,568",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp
                                ),
                                color = GreenLowest,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title & Specs
                    Text(
                        text = spotlightCpu.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        ),
                        color = InkText
                    )
                    Text(
                        text = spotlightCpu.specs,
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Price Banner
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = CanvasBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "LOWEST RITCHIE ST PRICE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.6.sp,
                                        fontSize = 9.5.sp
                                    ),
                                    color = MutedText
                                )
                                Text(
                                    text = "₹59,322",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    color = GreenLowest
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = GreenLowest,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Delta Peripherals",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = InkText
                                    )
                                }
                                Text(
                                    text = "Meeran Sahib St, Ritchie St",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MutedText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.equipComponent(ComponentCategory.CPU, spotlightCpu) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSpotlightEquipped) GreenLowest else VioletAccent
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("spotlight_add_button")
                        ) {
                            Icon(
                                imageVector = if (isSpotlightEquipped) Icons.Default.Check else Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isSpotlightEquipped) "Added to Current Rig ✓" else "Add to Current Rig ⚡",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.setHunterCategory(ComponentCategory.CPU)
                                viewModel.setHunterSearchQuery("9950X3D")
                                viewModel.switchTab(AppTab.HUNTER)
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, VioletAccent),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = VioletAccent),
                            modifier = Modifier.testTag("spotlight_compare_button")
                        ) {
                            Text(
                                text = "Compare 8 Shops",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // 4. Ritchie St. Live Price Pulse Ticker Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(GreenLowest),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "RITCHIE ST. LIVE PRICE PULSE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp
                            ),
                            color = VioletAccent
                        )
                        Text(
                            text = "Today's Verified Hardware Price Drops",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = InkText
                        )
                    }
                }

                Text(
                    text = "Live Ticker",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = GreenLowest,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        // Horizontal Scrolling Price Drop Cards
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 2.dp),
                modifier = Modifier.testTag("price_pulse_ticker")
            ) {
                items(HardwareCatalog.livePriceDrops) { drop ->
                    PricePulseDropCard(
                        drop = drop,
                        onAddClicked = {
                            val comp = HardwareCatalog.allComponents.find { it.name.contains(drop.componentName.take(12), ignoreCase = true) }
                            if (comp != null) {
                                viewModel.equipComponent(comp.category, comp)
                            }
                        },
                        onViewClicked = {
                            viewModel.setHunterCategory(drop.category)
                            viewModel.setHunterSearchQuery(drop.componentName.split(" ").take(2).joinToString(" "))
                            viewModel.switchTab(AppTab.HUNTER)
                        }
                    )
                }
            }
        }

        // 5. Quick Navigation Grid (Studio, Spec Checker, Slip, Hunter)
        item {
            Column {
                Text(
                    text = "QUICK DISCOVERY & TOOLS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp
                    ),
                    color = MutedText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickNavCard(
                        title = "Rig Studio CAD",
                        subtitle = "8-Slot Matrix Builder",
                        icon = Icons.Default.Build,
                        accentColor = VioletAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AppTab.CAD_STUDIO) },
                        testTag = "nav_card_studio"
                    )

                    QuickNavCard(
                        title = "Spec Checker",
                        subtitle = "Clearance & TDP Headroom",
                        icon = Icons.Default.Layers,
                        accentColor = Color(0xFF0284C7),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AppTab.BLUEPRINT) },
                        testTag = "nav_card_checker"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickNavCard(
                        title = "Ritchie St Transit",
                        subtitle = "Shortest Walking Route",
                        icon = Icons.Default.Map,
                        accentColor = GreenLowest,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AppTab.PRICE_PULSE) },
                        testTag = "nav_card_transit"
                    )

                    QuickNavCard(
                        title = "Component Hunter",
                        subtitle = "Dealers & Budget Slider",
                        icon = Icons.Default.Search,
                        accentColor = Color(0xFFD97706),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AppTab.HUNTER) },
                        testTag = "nav_card_hunter"
                    )
                }
            }
        }

        // 6. Popular Chennai Rig Presets Carousel
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "READY-TO-BUILD CHENNAI PRESETS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp
                        ),
                        color = MutedText
                    )
                    Text(
                        text = "4 Configs",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VioletAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp),
                    modifier = Modifier.testTag("presets_carousel")
                ) {
                    items(HardwareCatalog.presetBuilds) { preset ->
                        PresetCard(
                            preset = preset,
                            isActive = uiState.activePresetId == preset.id,
                            onLoad = {
                                viewModel.loadPreset(preset)
                                viewModel.switchTab(AppTab.CAD_STUDIO)
                            }
                        )
                    }
                }
            }
        }
    }

    // Location selection dialog
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = {
                Text(
                    text = "Select Chennai Hardware Hub",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = InkText
                )
            },
            text = {
                Column {
                    Text(
                        text = "Compare prices and dealer inventories based on your preferred Chennai market location:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    locations.forEach { loc ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setSelectedLocation(loc)
                                    showLocationDialog = false
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = uiState.selectedLocation == loc,
                                onClick = {
                                    viewModel.setSelectedLocation(loc)
                                    showLocationDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = VioletAccent)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = loc,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (uiState.selectedLocation == loc) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = InkText
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("Close", color = VioletAccent)
                }
            }
        )
    }

    // Live notifications dialog
    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = VioletAccent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ritchie Street Live Alerts", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "• Samsung 990 Pro 2TB dropped ₹1,200 at Delta Peripherals (Meeran Sahib St)",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkText
                    )
                    Text(
                        text = "• RTX 5080 16GB official shipment arrived at Marina Micro (Mount Rd)",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkText
                    )
                    Text(
                        text = "• GST 18% input credit verified for all 8 listed dealer shops",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkText
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationDialog = false }) {
                    Text("Got It", color = VioletAccent)
                }
            }
        )
    }
}

@Composable
private fun PricePulseDropCard(
    drop: PricePulseDrop,
    onAddClicked: () -> Unit,
    onViewClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(260.dp)
            .testTag("price_drop_${drop.id}"),
        shape = RoundedCornerShape(16.dp),
        color = CardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = GreenBg,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Dropped ₹${"%,d".format(drop.dropAmountInr)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp
                        ),
                        color = GreenLowest,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = drop.category.shortName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MutedText,
                        fontSize = 10.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = drop.componentName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = InkText,
                maxLines = 1
            )

            Text(
                text = "${drop.shopName} • ${drop.streetLane}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MutedText,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${"%,d".format(drop.previousPriceInr)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MutedText,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = "₹${"%,d".format(drop.currentPriceInr)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = GreenLowest
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        onClick = onViewClicked,
                        shape = RoundedCornerShape(8.dp),
                        color = VioletSoft
                    ) {
                        Text(
                            text = "Compare",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = VioletAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }

                    Surface(
                        onClick = onAddClicked,
                        shape = RoundedCornerShape(8.dp),
                        color = VioletAccent
                    ) {
                        Text(
                            text = "+ Add",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickNavCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        modifier = modifier
            .testTag(testTag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = CardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = InkText
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MutedText,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun PresetCard(
    preset: PresetBuild,
    isActive: Boolean,
    onLoad: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(240.dp)
            .testTag("preset_${preset.id}"),
        shape = RoundedCornerShape(16.dp),
        color = if (isActive) VioletSoft else CardBg,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isActive) VioletAccent else BorderColor
        ),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = preset.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isActive) VioletAccent else InkText
                )

                if (isActive) {
                    Surface(
                        color = VioletAccent,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "ACTIVE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = preset.tagline,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MutedText,
                maxLines = 2,
                modifier = Modifier.height(32.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹${"%,d".format(preset.estimatedPriceInr)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = GreenLowest
                )

                Button(
                    onClick = onLoad,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isActive) GreenLowest else VioletAccent
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isActive) "Loaded ✓" else "Load Rig",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
