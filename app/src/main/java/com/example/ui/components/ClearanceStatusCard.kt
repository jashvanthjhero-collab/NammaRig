package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
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
fun ClearanceStatusCard(
    clearanceReport: ClearanceReport,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val allGood = clearanceReport.allPass
    val primaryColor = if (allGood) EmeraldLowestPrice else AmberWarning

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("spec_clearance_card"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(primaryColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (allGood) Icons.Default.Security else Icons.Default.Warning,
                            contentDescription = "Spec Checker",
                            tint = primaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "SPEC & CLEARANCE CHECKER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = InkTextSecondary
                        )
                        Text(
                            text = if (allGood) "All 8 Components Compatible" else "Clearance / Spec Alert Detected",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = InkTextPrimary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = primaryColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (allGood) "VERIFIED" else "CHECK SPECS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = primaryColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle details",
                        tint = InkTextSecondary
                    )
                }
            }

            // Expanded Breakdown
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    ClearanceRowItem(
                        title = "GPU Physical Clearance",
                        subtitle = if (clearanceReport.caseMaxGpuLengthMm > 0) {
                            "Card: ${clearanceReport.gpuLengthMm}mm | Case Limit: ${clearanceReport.caseMaxGpuLengthMm}mm (${clearanceReport.gpuMarginMm}mm margin)"
                        } else "Case or GPU pending",
                        isOk = clearanceReport.isGpuFitting
                    )

                    ClearanceRowItem(
                        title = "CPU Cooler Height Fit",
                        subtitle = if (clearanceReport.caseMaxCoolerHeightMm > 0) {
                            "Cooler: ${clearanceReport.coolerHeightMm}mm | Case Clearance: ${clearanceReport.caseMaxCoolerHeightMm}mm (${clearanceReport.coolerMarginMm}mm margin)"
                        } else "Case or Cooler pending",
                        isOk = clearanceReport.isCoolerFitting
                    )

                    ClearanceRowItem(
                        title = "CPU Socket & Motherboard",
                        subtitle = if (clearanceReport.cpuSocket.isNotEmpty()) {
                            "CPU Socket ${clearanceReport.cpuSocket} vs Motherboard ${clearanceReport.motherboardSocket}"
                        } else "CPU or Motherboard pending",
                        isOk = clearanceReport.isSocketMatched
                    )

                    ClearanceRowItem(
                        title = "Motherboard Form Factor",
                        subtitle = if (clearanceReport.motherboardFormFactor.isNotEmpty()) {
                            "${clearanceReport.motherboardFormFactor} form factor supported by Case"
                        } else "Motherboard form factor check",
                        isOk = clearanceReport.isFormFactorSupported
                    )

                    ClearanceRowItem(
                        title = "PSU Wattage & 12V Rail Headroom",
                        subtitle = if (clearanceReport.psuCapacityWatts > 0) {
                            "${clearanceReport.psuCapacityWatts}W capacity vs ${clearanceReport.totalTdpWatts}W total draw (+${clearanceReport.tdpHeadroomWatts}W margin)"
                        } else "Recommended minimum: ${clearanceReport.recommendedPsuWatts}W",
                        isOk = clearanceReport.isPsuAdequate
                    )
                }
            }
        }
    }
}

@Composable
private fun ClearanceRowItem(
    title: String,
    subtitle: String,
    isOk: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isOk) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (isOk) EmeraldLowestPrice else CrimsonAlert,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = InkTextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = InkTextSecondary
            )
        }
    }
}
