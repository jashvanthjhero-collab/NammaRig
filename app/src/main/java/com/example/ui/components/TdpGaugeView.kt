package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClearanceReport
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldLowestPrice
import com.example.ui.theme.InkTextPrimary
import com.example.ui.theme.InkTextSecondary

@Composable
fun TdpGaugeView(
    clearanceReport: ClearanceReport,
    modifier: Modifier = Modifier
) {
    val totalTdp = clearanceReport.totalTdpWatts
    val psuCapacity = clearanceReport.psuCapacityWatts
    val headroom = clearanceReport.tdpHeadroomWatts

    val loadFraction = if (psuCapacity > 0) {
        (totalTdp.toFloat() / psuCapacity.toFloat()).coerceIn(0f, 1.2f)
    } else {
        0.5f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = loadFraction.coerceAtMost(1f),
        animationSpec = tween(durationMillis = 600),
        label = "TdpProgress"
    )

    val (statusColor, statusText, statusIcon) = when {
        psuCapacity == 0 -> Triple(
            ElectricViolet,
            "PSU Pending Selection (Rec: ${clearanceReport.recommendedPsuWatts}W)",
            Icons.Default.Bolt
        )
        headroom >= 150 -> Triple(
            EmeraldLowestPrice,
            "Healthy Power Headroom (+${headroom}W buffer)",
            Icons.Default.CheckCircle
        )
        headroom >= 50 -> Triple(
            AmberWarning,
            "Tight Margin (+${headroom}W headroom)",
            Icons.Default.Warning
        )
        else -> Triple(
            CrimsonAlert,
            "PSU Overload Risk (Need ${clearanceReport.recommendedPsuWatts}W+)",
            Icons.Default.Warning
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tdp_headroom_gauge"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(statusColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Power Headroom",
                            tint = statusColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TDP POWER HEADROOM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = InkTextSecondary
                        )
                        Text(
                            text = if (psuCapacity > 0) "${totalTdp}W / ${psuCapacity}W" else "${totalTdp}W Draw (No PSU)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            ),
                            color = InkTextPrimary
                        )
                    }
                }

                // Headroom Badge
                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = if (psuCapacity > 0) "${(loadFraction * 100).toInt()}% Load" else "Rec ${clearanceReport.recommendedPsuWatts}W",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Gauge Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFE2E4F0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .clip(RoundedCornerShape(5.dp))
                        .background(statusColor)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Status Caption
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                    color = statusColor
                )
            }
        }
    }
}
