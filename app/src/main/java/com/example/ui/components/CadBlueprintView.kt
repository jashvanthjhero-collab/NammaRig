package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClearanceReport
import com.example.model.ComponentCategory
import com.example.model.ComponentItem
import com.example.ui.theme.CadAccentAmber
import com.example.ui.theme.CadAccentCyan
import com.example.ui.theme.CadAccentGreen
import com.example.ui.theme.CadAccentViolet
import com.example.ui.theme.CadBlueprintBg
import com.example.ui.theme.CadGridLine
import com.example.ui.theme.ElectricViolet

@Composable
fun CadBlueprintView(
    slots: Map<ComponentCategory, ComponentItem?>,
    clearanceReport: ClearanceReport,
    highlightedSlot: ComponentCategory?,
    onSlotSelected: (ComponentCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "FanRotation")
    val fanGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowPulse"
    )

    val cpu = slots[ComponentCategory.CPU]
    val mobo = slots[ComponentCategory.MOTHERBOARD]
    val gpu = slots[ComponentCategory.GPU]
    val cooler = slots[ComponentCategory.COOLER]
    val case = slots[ComponentCategory.CASE]
    val psu = slots[ComponentCategory.PSU]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CadBlueprintBg)
            .border(1.5.dp, CadAccentCyan.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(12.dp)
            .testTag("cad_blueprint_view")
    ) {
        // Blueprint HUD Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (clearanceReport.allPass) CadAccentGreen else CadAccentAmber)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "RIG STUDIO 2D CAD SCHEMATIC",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = CadAccentCyan
                )
            }

            Text(
                text = if (case != null) "CASE MAX GPU: ${case.maxGpuLengthMm}mm" else "TAP SLOTS TO INSPECT",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                ),
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Blueprint Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF070B18))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // 1. Technical Grid Lines
                val gridStep = 24.dp.toPx()
                var x = 0f
                while (x < canvasWidth) {
                    drawLine(
                        color = CadGridLine,
                        start = Offset(x, 0f),
                        end = Offset(x, canvasHeight),
                        strokeWidth = 1f
                    )
                    x += gridStep
                }
                var y = 0f
                while (y < canvasHeight) {
                    drawLine(
                        color = CadGridLine,
                        start = Offset(0f, y),
                        end = Offset(canvasWidth, y),
                        strokeWidth = 1f
                    )
                    y += gridStep
                }

                // 2. Case Chassis Outer Boundary
                val caseLeft = 16.dp.toPx()
                val caseTop = 14.dp.toPx()
                val caseRight = canvasWidth - 16.dp.toPx()
                val caseBottom = canvasHeight - 20.dp.toPx()
                val caseWidth = caseRight - caseLeft
                val caseHeight = caseBottom - caseTop

                val isCaseHighlighted = highlightedSlot == ComponentCategory.CASE
                val caseColor = if (isCaseHighlighted) CadAccentCyan else Color(0xFF334155)

                drawRoundRect(
                    color = caseColor,
                    topLeft = Offset(caseLeft, caseTop),
                    size = Size(caseWidth, caseHeight),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                    style = Stroke(
                        width = if (isCaseHighlighted) 3.5f else 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 6f), 0f)
                    )
                )

                // Front Panel I/O Bezels (Right side)
                drawRect(
                    color = CadAccentCyan.copy(alpha = 0.4f),
                    topLeft = Offset(caseRight - 6.dp.toPx(), caseTop + 16.dp.toPx()),
                    size = Size(3.dp.toPx(), 24.dp.toPx())
                )

                // Case feet
                drawRect(
                    color = Color(0xFF475569),
                    topLeft = Offset(caseLeft + 16.dp.toPx(), caseBottom),
                    size = Size(20.dp.toPx(), 5.dp.toPx())
                )
                drawRect(
                    color = Color(0xFF475569),
                    topLeft = Offset(caseRight - 36.dp.toPx(), caseBottom),
                    size = Size(20.dp.toPx(), 5.dp.toPx())
                )

                // 3. Motherboard Tray
                val moboLeft = caseLeft + 16.dp.toPx()
                val moboTop = caseTop + 14.dp.toPx()
                val moboWidth = caseWidth * 0.58f
                val moboHeight = caseHeight * 0.65f
                val isMoboHighlighted = highlightedSlot == ComponentCategory.MOTHERBOARD

                drawRoundRect(
                    color = if (isMoboHighlighted) CadAccentViolet else Color(0xFF1E293B),
                    topLeft = Offset(moboLeft, moboTop),
                    size = Size(moboWidth, moboHeight),
                    cornerRadius = CornerRadius(6.dp.toPx()),
                    style = Stroke(width = if (isMoboHighlighted) 3f else 1.5f)
                )

                // 4. CPU Socket & Cooler (Inside Motherboard)
                val cpuLeft = moboLeft + 18.dp.toPx()
                val cpuTop = moboTop + 16.dp.toPx()
                val cpuSize = 44.dp.toPx()
                val isCpuHighlighted = highlightedSlot == ComponentCategory.CPU
                val isCoolerHighlighted = highlightedSlot == ComponentCategory.COOLER

                drawRoundRect(
                    color = if (isCpuHighlighted || isCoolerHighlighted) CadAccentCyan else Color(0xFF0EA5E9).copy(alpha = 0.8f),
                    topLeft = Offset(cpuLeft, cpuTop),
                    size = Size(cpuSize, cpuSize),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                    style = Stroke(width = 2f)
                )

                // CPU Fan concentric circle
                drawCircle(
                    color = if (isCoolerHighlighted) CadAccentViolet.copy(alpha = fanGlow) else Color(0xFF38BDF8).copy(alpha = 0.5f),
                    center = Offset(cpuLeft + cpuSize / 2, cpuTop + cpuSize / 2),
                    radius = cpuSize * 0.38f,
                    style = Stroke(width = 2f)
                )

                // 5. RAM Slots (Dual/Quad channel vertical strips next to CPU)
                val ramLeft = cpuLeft + cpuSize + 10.dp.toPx()
                val ramTop = cpuTop
                val ramWidth = 4.dp.toPx()
                val ramHeight = cpuSize
                val isRamHighlighted = highlightedSlot == ComponentCategory.RAM
                val ramColor = if (isRamHighlighted) CadAccentGreen else Color(0xFF10B981).copy(alpha = 0.7f)

                for (i in 0 until 4) {
                    drawRect(
                        color = ramColor,
                        topLeft = Offset(ramLeft + (i * 7.dp.toPx()), ramTop),
                        size = Size(ramWidth, ramHeight)
                    )
                }

                // 6. GPU PCIe Expansion Card Bracket (Horizontal block spanning case)
                val gpuLeft = moboLeft + 6.dp.toPx()
                val gpuTop = moboTop + cpuSize + 22.dp.toPx()

                // Calculate graphical GPU width scaled relative to case clearance
                val maxCaseMm = (case?.maxGpuLengthMm ?: 360).coerceAtLeast(300)
                val currentGpuMm = (gpu?.dimensionsMm?.lengthMm ?: 240).coerceAtLeast(180)
                val availableSpanPx = (caseRight - gpuLeft - 18.dp.toPx())
                val scaleRatio = (currentGpuMm.toFloat() / maxCaseMm.toFloat()).coerceIn(0.4f, 1.05f)
                val gpuWidthPx = availableSpanPx * scaleRatio
                val gpuHeightPx = 36.dp.toPx()

                val isGpuHighlighted = highlightedSlot == ComponentCategory.GPU
                val gpuColor = when {
                    !clearanceReport.isGpuFitting -> CadAccentAmber
                    isGpuHighlighted -> CadAccentGreen
                    else -> Color(0xFF10B981)
                }

                drawRoundRect(
                    color = gpuColor.copy(alpha = 0.25f),
                    topLeft = Offset(gpuLeft, gpuTop),
                    size = Size(gpuWidthPx, gpuHeightPx),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
                drawRoundRect(
                    color = gpuColor,
                    topLeft = Offset(gpuLeft, gpuTop),
                    size = Size(gpuWidthPx, gpuHeightPx),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                    style = Stroke(width = if (isGpuHighlighted) 3f else 2f)
                )

                // GPU Fans (Twin or Triple Fan indicators)
                val fanCount = if (currentGpuMm > 280) 3 else 2
                val fanSpacing = gpuWidthPx / (fanCount + 1)
                for (f in 1..fanCount) {
                    drawCircle(
                        color = gpuColor.copy(alpha = fanGlow * 0.9f),
                        center = Offset(gpuLeft + (f * fanSpacing), gpuTop + gpuHeightPx / 2),
                        radius = 11.dp.toPx(),
                        style = Stroke(width = 1.5f)
                    )
                }

                // GPU Length Clearance Bracket & Ruler
                val rulerY = gpuTop + gpuHeightPx + 4.dp.toPx()
                drawLine(
                    color = if (clearanceReport.isGpuFitting) CadAccentCyan else CadAccentAmber,
                    start = Offset(gpuLeft, rulerY),
                    end = Offset(gpuLeft + gpuWidthPx, rulerY),
                    strokeWidth = 2f
                )

                // 7. PSU Basement Shroud (Bottom compartment)
                val psuLeft = caseLeft + 12.dp.toPx()
                val psuTop = caseBottom - 38.dp.toPx()
                val psuWidth = caseWidth * 0.42f
                val psuHeight = 30.dp.toPx()
                val isPsuHighlighted = highlightedSlot == ComponentCategory.PSU

                drawRoundRect(
                    color = if (isPsuHighlighted) CadAccentViolet else Color(0xFF1E293B),
                    topLeft = Offset(psuLeft, psuTop),
                    size = Size(psuWidth, psuHeight),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
                drawRoundRect(
                    color = if (isPsuHighlighted) CadAccentViolet else Color(0xFF475569),
                    topLeft = Offset(psuLeft, psuTop),
                    size = Size(psuWidth, psuHeight),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                    style = Stroke(width = 1.5f)
                )

                // PSU Fan Grill Pattern
                for (g in 0 until 5) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.25f),
                        start = Offset(psuLeft + 10.dp.toPx() + (g * 6.dp.toPx()), psuTop + 6.dp.toPx()),
                        end = Offset(psuLeft + 10.dp.toPx() + (g * 6.dp.toPx()), psuTop + psuHeight - 6.dp.toPx()),
                        strokeWidth = 1.5f
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Blueprint Hotspot Badges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val gpuMm = gpu?.dimensionsMm?.lengthMm ?: 0
            val gpuMargin = clearanceReport.gpuMarginMm

            Surface(
                color = if (clearanceReport.isGpuFitting) Color(0xFF0F2C24) else Color(0xFF381E1E),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(
                    1.dp,
                    if (clearanceReport.isGpuFitting) CadAccentGreen.copy(alpha = 0.5f) else CadAccentAmber
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AspectRatio,
                        contentDescription = "GPU Clearance",
                        tint = if (clearanceReport.isGpuFitting) CadAccentGreen else CadAccentAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (gpu != null) "GPU: ${gpuMm}mm (${if (gpuMargin >= 0) "+${gpuMargin}mm" else "${gpuMargin}mm OVERFLOW"})"
                        else "GPU: Select card",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (clearanceReport.isGpuFitting) CadAccentGreen else CadAccentAmber
                    )
                }
            }

            Surface(
                color = if (clearanceReport.isCoolerFitting) Color(0xFF13223A) else Color(0xFF381E1E),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(
                    1.dp,
                    if (clearanceReport.isCoolerFitting) CadAccentCyan.copy(alpha = 0.5f) else CadAccentAmber
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Cooler Clearance",
                        tint = if (clearanceReport.isCoolerFitting) CadAccentCyan else CadAccentAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    val coolerHeight = cooler?.dimensionsMm?.heightMm ?: 0
                    Text(
                        text = if (cooler != null) "Cooler: ${coolerHeight}mm" else "Cooler: Default",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (clearanceReport.isCoolerFitting) CadAccentCyan else CadAccentAmber
                    )
                }
            }
        }
    }
}

private fun BorderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)
