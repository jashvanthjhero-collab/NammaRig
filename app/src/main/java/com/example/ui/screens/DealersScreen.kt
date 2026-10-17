package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DealerDirectory
import com.example.data.HardwareCatalog
import com.example.model.ComponentItem
import com.example.model.DealerPrice
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldLowestPrice
import com.example.ui.theme.FrostLavenderCanvas
import com.example.ui.theme.InkTextPrimary
import com.example.ui.theme.InkTextSecondary
import com.example.viewmodel.AppTab
import com.example.viewmodel.RigUiState
import com.example.viewmodel.RigViewModel

@Composable
fun DealersScreen(
    viewModel: RigViewModel,
    uiState: RigUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lanes = listOf("All Lanes") + DealerDirectory.ALL_LANES

    // Filter components by search query
    val filteredComponents = HardwareCatalog.allComponents.filter { item ->
        val query = uiState.dealerSearchQuery.trim()
        val matchesQuery = query.isEmpty() ||
                item.name.contains(query, ignoreCase = true) ||
                item.brand.contains(query, ignoreCase = true) ||
                item.category.displayName.contains(query, ignoreCase = true)

        val matchesLane = uiState.dealerLaneFilter == "All Lanes" ||
                item.dealerPrices.any { it.streetLane.equals(uiState.dealerLaneFilter, ignoreCase = true) }

        matchesQuery && matchesLane
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FrostLavenderCanvas)
            .testTag("dealers_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Header
        item {
            Column {
                Text(
                    text = "CHENNAI DEALER PRICE HUB",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    ),
                    color = ElectricViolet
                )
                Text(
                    text = "Ritchie Street Price Comparison",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = InkTextPrimary
                )
                Text(
                    text = "Live price differences across Meeran Sahib St, Wallers Rd & Narayana Mudali",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkTextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = uiState.dealerSearchQuery,
                onValueChange = { viewModel.setDealerSearchQuery(it) },
                placeholder = { Text("Search GPU, CPU, RAM or brand...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = ElectricViolet
                    )
                },
                trailingIcon = {
                    if (uiState.dealerSearchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setDealerSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricViolet,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dealer_search_input")
            )
        }

        // Street Lane Chips Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                lanes.forEach { lane ->
                    val isSelected = uiState.dealerLaneFilter == lane
                    ElevatedFilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setDealerLaneFilter(lane) },
                        label = {
                            Text(
                                text = lane,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        },
                        colors = FilterChipDefaults.elevatedFilterChipColors(
                            selectedContainerColor = ElectricViolet,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = InkTextPrimary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("lane_chip_${lane.replace(" ", "_").lowercase()}")
                    )
                }
            }
        }

        // Comparison Result Count
        item {
            Text(
                text = "${filteredComponents.size} HARDWARE ITEMS FOUND",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                ),
                color = InkTextSecondary
            )
        }

        // Component Comparison Cards
        items(filteredComponents) { component ->
            val lowestPrice = component.getLowestPrice()
            val sortedDealerPrices = component.dealerPrices.sortedBy { it.priceInr }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dealer_card_${component.id}"),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Component Title & Specs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = ElectricViolet.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = component.category.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = ElectricViolet,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = component.name,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = InkTextPrimary
                            )
                            Text(
                                text = component.specs,
                                style = MaterialTheme.typography.bodySmall,
                                color = InkTextSecondary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.equipComponent(component.category, component)
                                viewModel.switchTab(AppTab.CAD_STUDIO)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Equip",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dealer Price Rows
                    Text(
                        text = "DEALER QUOTES IN CHENNAI",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = InkTextSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        sortedDealerPrices.forEach { dealerPrice ->
                            val isCheapest = lowestPrice?.dealerId == dealerPrice.dealerId

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isCheapest) EmeraldLowestPrice.copy(alpha = 0.08f)
                                else MaterialTheme.colorScheme.background,
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isCheapest) 1.5.dp else 1.dp,
                                    if (isCheapest) EmeraldLowestPrice else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = dealerPrice.dealerName,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = if (isCheapest) FontWeight.Bold else FontWeight.Medium
                                                ),
                                                color = InkTextPrimary
                                            )
                                            if (isCheapest) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    color = EmeraldLowestPrice,
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = "LOWEST",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontSize = 8.5.sp,
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        color = Color.White,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.padding(top = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Place,
                                                contentDescription = null,
                                                tint = InkTextSecondary,
                                                modifier = Modifier.size(11.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = dealerPrice.streetLane,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = InkTextSecondary
                                            )
                                        }
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "₹${"%,d".format(dealerPrice.priceInr)}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontFamily = FontFamily.Monospace
                                            ),
                                            color = if (isCheapest) EmeraldLowestPrice else InkTextPrimary
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // WhatsApp inquiry button
                                        IconButton(
                                            onClick = {
                                                val cleanNum = dealerPrice.whatsappNumber.replace("+", "").replace(" ", "").trim()
                                                val msg = Uri.encode(
                                                    "Vanakkam ${dealerPrice.dealerName}! Checking availability for ${component.name} listed at ₹${dealerPrice.priceInr} on NammaRig. Is this in stock today at your ${dealerPrice.streetLane} shop?"
                                                )
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://api.whatsapp.com/send?phone=$cleanNum&text=$msg")
                                                )
                                                context.startActivity(intent)
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Chat,
                                                contentDescription = "WhatsApp Dealer",
                                                tint = EmeraldLowestPrice,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
