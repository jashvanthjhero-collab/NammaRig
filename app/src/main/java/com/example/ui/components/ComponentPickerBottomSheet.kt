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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HardwareCatalog
import com.example.model.ComponentCategory
import com.example.model.ComponentItem
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldLowestPrice
import com.example.ui.theme.InkTextPrimary
import com.example.ui.theme.InkTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentPickerBottomSheet(
    category: ComponentCategory,
    currentlySelected: ComponentItem?,
    onComponentSelected: (ComponentItem) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val availableItems = HardwareCatalog.allComponents.filter { it.category == category }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("component_picker_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "EQUIP ${category.name}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = ElectricViolet
                    )
                    Text(
                        text = "Select ${category.displayName}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = InkTextPrimary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(availableItems) { item ->
                    val isEquipped = item.id == currentlySelected?.id
                    val lowestPrice = item.getLowestPrice()

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onComponentSelected(item)
                                onDismiss()
                            }
                            .testTag("pick_item_${item.id}"),
                        shape = RoundedCornerShape(14.dp),
                        color = if (isEquipped) ElectricViolet.copy(alpha = 0.08f) else MaterialTheme.colorScheme.background,
                        border = androidx.compose.foundation.BorderStroke(
                            if (isEquipped) 2.dp else 1.dp,
                            if (isEquipped) ElectricViolet else MaterialTheme.colorScheme.outline
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = InkTextPrimary
                                )
                                Text(
                                    text = item.specs,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = InkTextSecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                if (lowestPrice != null) {
                                    Row(
                                        modifier = Modifier.padding(top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Storefront,
                                            contentDescription = null,
                                            tint = EmeraldLowestPrice,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Best: ${lowestPrice.dealerName} (${lowestPrice.streetLane})",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = EmeraldLowestPrice
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${"%,d".format(item.getLowestPriceInr())}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = EmeraldLowestPrice
                                )
                                if (isEquipped) {
                                    Surface(
                                        color = ElectricViolet,
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text(
                                            text = "EQUIPPED",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
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
