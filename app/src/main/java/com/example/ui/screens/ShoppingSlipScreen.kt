package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.engine.DealerStop
import com.example.engine.LaneCluster
import com.example.engine.ShoppingSlipEngine
import com.example.engine.ShoppingSlipSummary
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

@Composable
fun ShoppingSlipScreen(
    viewModel: RigViewModel,
    uiState: RigUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val slip = uiState.shoppingSlip

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBg)
            .testTag("shopping_slip_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RITCHIE STREET SHOPPING SLIP",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        ),
                        color = VioletAccent
                    )
                    Text(
                        text = "Lane-Clustered Walking Route",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = InkText
                    )
                }

                if (slip.totalItemCount > 0) {
                    ElevatedButton(
                        onClick = {
                            val text = ShoppingSlipEngine.generateFullSlipText(slip)
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, text)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share Ritchie Street Slip")
                            context.startActivity(shareIntent)
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = VioletAccent,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("share_slip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Share",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // 1. Route Walk Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TOTAL ESTIMATED RIG QUOTE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                ),
                                color = MutedText
                            )
                            Text(
                                text = "₹${"%,d".format(slip.totalBuildCostInr)}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = GreenLowest
                            )
                        }

                        Surface(
                            color = GreenBg,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Save ~₹${"%,d".format(slip.totalSavingsInr)}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = GreenLowest,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Route details row
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = CanvasBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsWalk,
                                    contentDescription = null,
                                    tint = VioletAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${slip.clusters.size} Stops · ~${slip.estimatedWalkingMeters}m Total Walk (~${slip.estimatedWalkingMinutes} min)",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = InkText
                                )
                            }
                            if (slip.walkingRouteDescription.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = slip.walkingRouteDescription,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp
                                    ),
                                    color = VioletAccent
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Ritchie Street Buyer Protection Tips Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VioletSoft),
                border = androidx.compose.foundation.BorderStroke(1.dp, VioletAccent.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = VioletAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RITCHIE STREET BUYER PROTECTION TIPS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp
                            ),
                            color = VioletAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ProtectionTipItem(text = "1. Verify brand hologram seal and serial number before unboxing.")
                        ProtectionTipItem(text = "2. Demand official 18% GST tax invoice with component serials recorded.")
                        ProtectionTipItem(text = "3. Test key components (CPU, Mobo, RAM) on the dealer shop bench before leaving.")
                        ProtectionTipItem(text = "4. Pay via official UPI or Card with tax breakup to retain authorized RMA warranty.")
                    }
                }
            }
        }

        // Empty state check
        if (slip.clusters.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = CardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = MutedText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No Components Equipped Yet",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = InkText
                        )
                        Text(
                            text = "Equip hardware in CAD Studio or load a preset to generate your Ritchie Street route.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedText,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.switchTab(AppTab.CAD_STUDIO) },
                            colors = ButtonDefaults.buttonColors(containerColor = VioletAccent),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Open CAD Studio")
                        }
                    }
                }
            }
        }

        // 3. Clustered Shops Stop-by-Stop
        itemsIndexed(slip.clusters) { laneIndex, cluster ->
            LaneClusterCard(
                laneIndex = laneIndex,
                cluster = cluster,
                checkedItems = uiState.checkedShoppingItems,
                onToggleItemChecked = { itemId ->
                    viewModel.toggleShoppingItemChecked(itemId)
                },
                onWhatsAppClicked = { stop ->
                    val intentUri = stop.getWhatsAppIntentUri()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri))
                    context.startActivity(intent)
                },
                onDirectionsClicked = { dealerName, laneName ->
                    val query = Uri.encode("$dealerName $laneName Ritchie Street Chennai")
                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$query"))
                    context.startActivity(mapIntent)
                },
                onCallClicked = { phone ->
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
private fun ProtectionTipItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = GreenLowest,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
            color = InkText
        )
    }
}

@Composable
private fun LaneClusterCard(
    laneIndex: Int,
    cluster: LaneCluster,
    checkedItems: Set<String>,
    onToggleItemChecked: (String) -> Unit,
    onWhatsAppClicked: (DealerStop) -> Unit,
    onDirectionsClicked: (String, String) -> Unit,
    onCallClicked: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lane_cluster_${laneIndex}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Lane Step Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(VioletAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${laneIndex + 1}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "LANE STOP ${laneIndex + 1}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = VioletAccent
                        )
                        Text(
                            text = cluster.laneName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = InkText
                        )
                    }
                }

                Text(
                    text = "₹${"%,d".format(cluster.totalLaneInr)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = GreenLowest
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dealer Stops within this Lane
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                cluster.dealerStops.forEach { stop ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CanvasBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Storefront,
                                        contentDescription = null,
                                        tint = VioletAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stop.dealerName,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = InkText
                                    )
                                }

                                Text(
                                    text = "Subtotal: ₹${"%,d".format(stop.subtotalInr)}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = GreenLowest
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Interactive Checklist for Counter Buying
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                stop.itemsWithPrices.forEach { (item, price) ->
                                    val isChecked = checkedItems.contains(item.id)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onToggleItemChecked(item.id) }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { onToggleItemChecked(item.id) },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = GreenLowest,
                                                uncheckedColor = MutedText
                                            ),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = item.name,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = if (isChecked) FontWeight.Normal else FontWeight.Medium
                                            ),
                                            color = if (isChecked) MutedText else InkText,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "₹${"%,d".format(price)}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = if (isChecked) MutedText else InkText
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action buttons: 1-Tap WhatsApp, Directions & Call
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onWhatsAppClicked(stop) },
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenLowest),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("whatsapp_btn_${stop.dealerId}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "WhatsApp Shop",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }

                                OutlinedButton(
                                    onClick = { onDirectionsClicked(stop.dealerName, cluster.laneName) },
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("directions_btn_${stop.dealerId}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Navigation,
                                        contentDescription = "Directions",
                                        tint = VioletAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Directions",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VioletAccent
                                    )
                                }

                                OutlinedButton(
                                    onClick = { onCallClicked(stop.phone) },
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "Call",
                                        tint = MutedText,
                                        modifier = Modifier.size(16.dp)
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
