package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ComponentCategory
import com.example.model.ComponentItem
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldLowestPrice
import com.example.ui.theme.InkTextPrimary
import com.example.ui.theme.InkTextSecondary

@Composable
fun ComponentSlotItemCard(
    category: ComponentCategory,
    item: ComponentItem?,
    isHighlighted: Boolean,
    onCardClicked: () -> Unit,
    onPickRequested: () -> Unit,
    onRemoveRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClicked() }
            .testTag("slot_card_${category.name.lowercase()}"),
        shape = RoundedCornerShape(14.dp),
        color = if (isHighlighted) ElectricViolet.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            if (isHighlighted) 2.dp else 1.dp,
            if (isHighlighted) ElectricViolet else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
        ),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Badge Icon
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (item != null) ElectricViolet.copy(alpha = 0.12f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.shortName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp
                    ),
                    color = if (item != null) ElectricViolet else InkTextSecondary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Component Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = InkTextSecondary
                    )
                }

                if (item != null) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = InkTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val lowestDealer = item.getLowestPrice()
                    Row(
                        modifier = Modifier.padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₹${"%,d".format(item.getLowestPriceInr())}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = EmeraldLowestPrice
                        )
                        if (lowestDealer != null) {
                            Text(
                                text = " @ ${lowestDealer.dealerName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = InkTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Slot Empty — Tap to Choose",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = InkTextSecondary.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Buttons
            if (item != null) {
                IconButton(
                    onClick = onPickRequested,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Swap Component",
                        tint = ElectricViolet,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onRemoveRequested,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove Component",
                        tint = InkTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                OutlinedButton(
                    onClick = onPickRequested,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = ElectricViolet,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = ElectricViolet
                    )
                }
            }
        }
    }
}
