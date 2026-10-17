package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ComponentCategory
import com.example.ui.components.CadBlueprintView
import com.example.ui.components.TdpGaugeView
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CanvasBg
import com.example.ui.theme.CardBg
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.GreenBg
import com.example.ui.theme.GreenLowest
import com.example.ui.theme.InkText
import com.example.ui.theme.MutedText
import com.example.ui.theme.VioletAccent
import com.example.ui.theme.VioletSoft
import com.example.viewmodel.RigUiState
import com.example.viewmodel.RigViewModel

@Composable
fun SpecCheckerScreen(
    viewModel: RigViewModel,
    uiState: RigUiState,
    modifier: Modifier = Modifier
) {
    val report = uiState.clearanceReport
    val slots = uiState.slots

    val cpu = slots[ComponentCategory.CPU]
    val mobo = slots[ComponentCategory.MOTHERBOARD]
    val gpu = slots[ComponentCategory.GPU]
    val cooler = slots[ComponentCategory.COOLER]
    val ram = slots[ComponentCategory.RAM]
    val psu = slots[ComponentCategory.PSU]
    val case = slots[ComponentCategory.CASE]

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBg)
            .testTag("spec_checker_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column {
                Text(
                    text = "SPEC CHECKER & CLEARANCE CAD",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    ),
                    color = VioletAccent
                )
                Text(
                    text = "Hardware Fit Diagnostics",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = InkText
                )
            }
        }

        // 1. Circular Compatibility Radial Meter Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("compatibility_radial_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Circular Gauge Meter
                    Box(
                        modifier = Modifier.size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val animatedScore by animateFloatAsState(
                            targetValue = report.compatibilityScore.toFloat(),
                            animationSpec = tween(durationMillis = 800),
                            label = "score_anim"
                        )

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 14.dp.toPx()
                            val arcSize = size.width - strokeWidth
                            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                            // Track circle
                            drawArc(
                                color = Color(0xFFE5E7EB),
                                startAngle = 135f,
                                sweepAngle = 270f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(arcSize, arcSize),
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // Progress arc with gradient
                            val sweepAngle = (animatedScore / 100f) * 270f
                            val arcColor = when {
                                animatedScore >= 90 -> GreenLowest
                                animatedScore >= 75 -> Color(0xFF0284C7)
                                animatedScore >= 50 -> Color(0xFFD97706)
                                else -> Color(0xFFDC2626)
                            }

                            drawArc(
                                color = arcColor,
                                startAngle = 135f,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(arcSize, arcSize),
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        // Center score readout
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${report.compatibilityScore}%",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 34.sp
                                ),
                                color = InkText
                            )
                            Surface(
                                color = if (report.compatibilityScore >= 85) GreenBg else Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = report.fitStatusText,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp
                                    ),
                                    color = if (report.compatibilityScore >= 85) GreenLowest else Color(0xFF92400E),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (report.allPass) {
                            "All components validated for electrical headroom and mechanical dimensions."
                        } else {
                            "Hardware warnings detected. Click 1-Click Auto-Fix to automatically upgrade slots."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 1-Click Auto-Fix Rig Button
                    Button(
                        onClick = { viewModel.autoFixRig() },
                        colors = ButtonDefaults.buttonColors(containerColor = VioletAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auto_fix_rig_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoFixHigh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "1-Click Auto-Fix Rig",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // 2. Real-Time Electrical Power Gauge (TDP Headroom)
        item {
            TdpGaugeView(clearanceReport = report)
        }

        // 3. Diagnostic Rule Check Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "DIAGNOSTIC RULE CHECKS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp
                    ),
                    color = MutedText
                )

                // Rule 1: AM5/LGA Socket & BIOS Compatibility
                DiagnosticRuleCard(
                    title = "Socket & Motherboard BIOS",
                    subtitle = if (cpu != null && mobo != null) {
                        "${cpu.name.take(16)} (${report.cpuSocket}) ↔ ${mobo.name.take(16)} (${report.motherboardSocket})"
                    } else "AM5 / LGA1700 Socket Match Check",
                    statusText = if (report.isSocketMatched) "PASSED" else "MISMATCH",
                    isPassed = report.isSocketMatched,
                    details = if (report.isSocketMatched) {
                        "CPU socket perfectly matches motherboard socket. No BIOS flashback required."
                    } else {
                        "CPU socket (${report.cpuSocket}) does not match motherboard socket (${report.motherboardSocket})!"
                    },
                    icon = Icons.Default.Memory
                )

                // Rule 2: PCIe Gen 5.0 x16 GPU & M.2 Lane Clearance
                DiagnosticRuleCard(
                    title = "PCIe Gen 5.0 GPU & M.2 Lane Clearance",
                    subtitle = "16x PCIe Gen 5 Lanes directly mapped to CPU root complex",
                    statusText = if (report.isPcieGen5Cleared) "PASSED" else "ADVISORY",
                    isPassed = report.isPcieGen5Cleared,
                    details = "Full x16 bandwidth available without sharing or throttling secondary NVMe M.2 slots.",
                    icon = Icons.Default.Speed
                )

                // Rule 3: DDR5 AMD EXPO 6000MT/s Profile 1 Recommendation
                DiagnosticRuleCard(
                    title = "DDR5 Memory Sweet Spot",
                    subtitle = report.ddr5ExpoAdvisory,
                    statusText = "RECOMMENDED",
                    isPassed = true,
                    isAdvisory = true,
                    details = "6000MT/s CL30 provides 1:1 FCLK infinity fabric synchronization with sub-65ns latency.",
                    icon = Icons.Default.Tune
                )

                // Rule 4: Chassis Clearance for 360mm Radiator & GPU
                DiagnosticRuleCard(
                    title = "Chassis Clearance & 360mm Radiator",
                    subtitle = if (case != null) {
                        "${case.name.take(20)} • Max GPU: ${report.caseMaxGpuLengthMm}mm • Margin: +${report.gpuMarginMm}mm"
                    } else "Chassis dimensions check",
                    statusText = if (report.isGpuFitting && report.isCoolerFitting && report.isRadiatorCleared) "PASSED" else "CLEARANCE ISSUE",
                    isPassed = report.isGpuFitting && report.isCoolerFitting && report.isRadiatorCleared,
                    details = "Chassis has +${report.gpuMarginMm}mm GPU headroom and +${report.coolerMarginMm}mm CPU cooler clearance.",
                    icon = Icons.Default.CheckCircle
                )
            }
        }

        // 4. Interactive 2D CAD Blueprint Visualizer
        item {
            Column {
                Text(
                    text = "2D CAD CHASSIS BLUEPRINT SCHEMATIC",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp
                    ),
                    color = MutedText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CadBlueprintView(
                    slots = slots,
                    clearanceReport = report,
                    highlightedSlot = uiState.highlightedSlot,
                    onSlotSelected = { cat ->
                        viewModel.highlightSlot(cat)
                    }
                )
            }
        }
    }
}

@Composable
private fun DiagnosticRuleCard(
    title: String,
    subtitle: String,
    statusText: String,
    isPassed: Boolean,
    isAdvisory: Boolean = false,
    details: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
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
                            .background(
                                when {
                                    isAdvisory -> Color(0xFFEFF6FF)
                                    isPassed -> GreenBg
                                    else -> Color(0xFFFEE2E2)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = when {
                                isAdvisory -> Color(0xFF2563EB)
                                isPassed -> GreenLowest
                                else -> Color(0xFFDC2626)
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = InkText
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                            color = MutedText,
                            maxLines = 1
                        )
                    }
                }

                Surface(
                    color = when {
                        isAdvisory -> Color(0xFFEFF6FF)
                        isPassed -> GreenBg
                        else -> Color(0xFFFEE2E2)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 9.5.sp
                        ),
                        color = when {
                            isAdvisory -> Color(0xFF2563EB)
                            isPassed -> GreenLowest
                            else -> Color(0xFFDC2626)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = details,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = InkText
            )
        }
    }
}
