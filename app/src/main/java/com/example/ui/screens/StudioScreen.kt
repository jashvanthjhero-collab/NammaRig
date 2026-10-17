package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.model.ComponentCategory
import com.example.model.ComponentItem
import com.example.ui.components.CadBlueprintView
import com.example.ui.components.ClearanceStatusCard
import com.example.ui.components.ComponentPickerBottomSheet
import com.example.ui.components.ComponentSlotItemCard
import com.example.ui.components.PresetsModal
import com.example.ui.components.TdpGaugeView
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldLowestPrice
import com.example.ui.theme.FrostLavenderCanvas
import com.example.ui.theme.InkTextPrimary
import com.example.ui.theme.InkTextSecondary
import com.example.viewmodel.AppTab
import com.example.viewmodel.RigUiState
import com.example.viewmodel.RigViewModel

@Composable
fun StudioScreen(
    viewModel: RigViewModel,
    uiState: RigUiState,
    modifier: Modifier = Modifier
) {
    var showPresetsModal by remember { mutableStateOf(false) }
    var pickerCategory by remember { mutableStateOf<ComponentCategory?>(null) }

    val eightSlots = listOf(
        ComponentCategory.CPU,
        ComponentCategory.MOTHERBOARD,
        ComponentCategory.GPU,
        ComponentCategory.COOLER,
        ComponentCategory.RAM,
        ComponentCategory.SSD,
        ComponentCategory.PSU,
        ComponentCategory.CASE
    )

    val totalCost = uiState.shoppingSlip.totalBuildCostInr
    val savings = uiState.shoppingSlip.totalSavingsInr

    val equippedCount = uiState.slots.values.filterNotNull().size
    val socketCode = uiState.slots[ComponentCategory.CPU]?.socketType ?: "AM5"
    val buildSerial = "NR-BLD-8821-$socketCode"

    Box(modifier = modifier.fillMaxSize().background(FrostLavenderCanvas)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("studio_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // App Title & Action Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "RIG STUDIO CAD",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.2.sp
                            ),
                            color = ElectricViolet
                        )
                        Text(
                            text = "8-Component Matrix",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = InkTextPrimary
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ElevatedButton(
                            onClick = { showPresetsModal = true },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = ElectricViolet,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("presets_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Presets",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.clearBuild() },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("clear_build_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear Build",
                                tint = InkTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // 8-Component Workbench Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "8 HARDWARE SLOTS ($equippedCount/8 EQUIPPED)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        ),
                        color = InkTextSecondary
                    )

                    Surface(
                        color = ElectricViolet.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = buildSerial,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = ElectricViolet,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // 8 Component Slot Cards
            items(eightSlots) { category ->
                val equipped = uiState.slots[category]
                ComponentSlotItemCard(
                    category = category,
                    item = equipped,
                    isHighlighted = uiState.highlightedSlot == category,
                    onCardClicked = {
                        viewModel.highlightSlot(category)
                    },
                    onPickRequested = {
                        pickerCategory = category
                    },
                    onRemoveRequested = {
                        viewModel.removeComponent(category)
                    }
                )
            }
        }

        // Bottom Sticky CAD Title Block
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("sticky_cad_title_block"),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricViolet.copy(alpha = 0.35f)),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = buildSerial,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp
                            ),
                            color = ElectricViolet
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = if (equippedCount == 8) EmeraldLowestPrice.copy(alpha = 0.15f) else ElectricViolet.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "$equippedCount/8 Slots",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = if (equippedCount == 8) EmeraldLowestPrice else ElectricViolet,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "₹${"%,d".format(totalCost)}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = EmeraldLowestPrice
                    )
                }

                Button(
                    onClick = { viewModel.switchTab(AppTab.PRICE_PULSE) },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    modifier = Modifier.testTag("get_shopping_slip_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Get Shopping Slip",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }

    // Presets Modal Sheet
    if (showPresetsModal) {
        PresetsModal(
            activePresetId = uiState.activePresetId,
            onSelectPreset = { preset ->
                viewModel.loadPreset(preset)
            },
            onDismiss = { showPresetsModal = false }
        )
    }

    // Component Picker Bottom Sheet
    pickerCategory?.let { category ->
        ComponentPickerBottomSheet(
            category = category,
            currentlySelected = uiState.slots[category],
            onComponentSelected = { item ->
                viewModel.equipComponent(category, item)
            },
            onDismiss = { pickerCategory = null }
        )
    }
}
