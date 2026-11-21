package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BuildFrequencyPoint
import com.example.model.ComplianceBadge
import com.example.model.DashboardKpiSummary
import com.example.model.DeploymentSuccessPoint
import com.example.model.PipelineEnvironment
import com.example.model.ProjectTelemetrySnapshot
import com.example.model.TeamActivityPoint
import com.example.model.TelemetryTimeRange
import com.example.ui.theme.NexusBlue
import com.example.ui.theme.NexusBlueContainer
import com.example.ui.theme.NexusBorder
import com.example.ui.theme.NexusCyan
import com.example.ui.theme.NexusCyanBg
import com.example.ui.theme.NexusDarkNavy
import com.example.ui.theme.NexusEmerald
import com.example.ui.theme.NexusEmeraldBg
import com.example.ui.theme.NexusOnBlueContainer
import com.example.ui.theme.NexusOrange
import com.example.ui.theme.NexusOrangeBg
import com.example.ui.theme.NexusPurple
import com.example.ui.theme.NexusPurpleBg
import com.example.ui.theme.NexusSurface
import com.example.ui.theme.NexusSurfaceVariant
import com.example.ui.theme.NexusTextMuted
import com.example.ui.theme.NexusTextPrimary
import com.example.ui.theme.NexusTextSecondary
import kotlin.math.roundToInt

/**
 * Full Engineering Telemetry Dashboard combining Recharts-inspired interactive
 * Compose Canvas charts (Build Frequency BarChart, Deployment Success AreaChart,
 * Team Activity Multi-LineChart) with time-range & pipeline filters.
 */
@Composable
fun EngineeringDashboardSection(
    snapshot: ProjectTelemetrySnapshot,
    selectedTimeRange: TelemetryTimeRange,
    selectedEnvironment: PipelineEnvironment,
    onTimeRangeChange: (TelemetryTimeRange) -> Unit,
    onEnvironmentChange: (PipelineEnvironment) -> Unit,
    modifier: Modifier = Modifier,
    sectionTitle: String = "Engineering Telemetry",
    sectionEyebrow: String = "CI/CD & RECHARTS ANALYTICS ENGINE"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header + Time Range Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sectionEyebrow,
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = NexusDarkNavy,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Segmented Time Range Pills (7D / 14D / 30D)
            Surface(
                color = NexusSurfaceVariant,
                shape = RoundedCornerShape(50),
                modifier = Modifier.border(1.dp, NexusBorder, RoundedCornerShape(50))
            ) {
                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    TelemetryTimeRange.entries.forEach { range ->
                        val isSelected = range == selectedTimeRange
                        Surface(
                            onClick = { onTimeRangeChange(range) },
                            color = if (isSelected) NexusBlue else Color.Transparent,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("telemetry_range_${range.label}")
                        ) {
                            Text(
                                text = range.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color.White else NexusTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Pipeline Environment Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PipelineEnvironment.entries.forEach { env ->
                val isSelected = env == selectedEnvironment
                Surface(
                    onClick = { onEnvironmentChange(env) },
                    color = if (isSelected) NexusBlueContainer else NexusSurface,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (isSelected) NexusBlue else NexusBorder,
                            shape = RoundedCornerShape(50)
                        )
                        .testTag("telemetry_env_${env.name.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) NexusBlue else NexusTextMuted)
                        )
                        Text(
                            text = env.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) NexusOnBlueContainer else NexusTextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // 4 KPI Summary Cards (2x2 Grid)
        DashboardKpiGrid(kpi = snapshot.kpiSummary)

        // Chart 1: Build Frequency Stacked Bar Chart
        BuildFrequencyBarChartCard(points = snapshot.buildFrequency)

        // Chart 2: Deployment Success Rate Smooth Area Chart
        DeploymentSuccessAreaChartCard(points = snapshot.deploymentSuccess)

        // Chart 3: Team Activity Over Time Multi-Line Chart
        TeamActivityLineChartCard(points = snapshot.teamActivity)
    }
}

@Composable
fun DashboardKpiGrid(
    kpi: DashboardKpiSummary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KpiStatCard(
                title = "BUILD RUNS",
                value = "${kpi.totalBuilds}",
                delta = kpi.buildsDelta,
                icon = Icons.Default.Build,
                accentColor = NexusBlue,
                badgeBg = NexusBlueContainer,
                modifier = Modifier.weight(1f)
            )
            KpiStatCard(
                title = "DEPLOY SUCCESS",
                value = "${kpi.avgSuccessRatePct}%",
                delta = kpi.successDelta,
                icon = Icons.Default.RocketLaunch,
                accentColor = NexusEmerald,
                badgeBg = NexusEmeraldBg,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KpiStatCard(
                title = "TEAM ACTIVITY",
                value = "${kpi.totalTeamEvents}",
                delta = kpi.teamDelta,
                icon = Icons.Default.Groups,
                accentColor = NexusPurple,
                badgeBg = NexusPurpleBg,
                modifier = Modifier.weight(1f)
            )
            KpiStatCard(
                title = "AVG BUILD TIME",
                value = "${kpi.meanBuildDurationSec}s",
                delta = kpi.durationDelta,
                icon = Icons.Default.Speed,
                accentColor = NexusOrange,
                badgeBg = NexusOrangeBg,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun KpiStatCard(
    title: String,
    value: String,
    delta: String,
    icon: ImageVector,
    accentColor: Color,
    badgeBg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    letterSpacing = 0.6.sp
                )
                Surface(
                    shape = CircleShape,
                    color = badgeBg,
                    modifier = Modifier.size(26.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = accentColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = NexusDarkNavy,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = delta,
                style = MaterialTheme.typography.labelSmall,
                color = accentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        }
    }
}

/**
 * Chart 1: Recharts-style Stacked Bar Chart for Build Frequency
 * Tap any bar to inspect automated vs manual builds and average build duration.
 */
@Composable
fun BuildFrequencyBarChartCard(
    points: List<BuildFrequencyPoint>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember(points) { mutableIntStateOf((points.size - 1).coerceAtLeast(0)) }
    val animProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
        label = "buildBarAnim"
    )

    val maxTotal = remember(points) {
        (points.maxOfOrNull { it.totalBuilds } ?: 10).coerceAtLeast(10)
    }
    val yAxisMax = ((maxTotal + 9) / 10) * 10
    val selectedPoint = points.getOrNull(selectedIndex)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(20.dp))
            .testTag("chart_build_frequency"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "BUILD FREQUENCY",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "CI/CD Pipeline Executions",
                        style = MaterialTheme.typography.titleMedium,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Legend
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChartLegendDot(color = NexusBlue, label = "Auto")
                    ChartLegendDot(color = NexusCyan, label = "Manual")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Recharts-style Tooltip Banner
            if (selectedPoint != null) {
                Surface(
                    color = NexusSurfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${selectedPoint.fullDate} (${selectedPoint.label})",
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Avg duration: ${selectedPoint.avgDurationSec}s",
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            TooltipMetricPill("Auto", "${selectedPoint.automatedBuilds}", NexusBlue)
                            TooltipMetricPill("Manual", "${selectedPoint.manualBuilds}", NexusCyan)
                            TooltipMetricPill("Total", "${selectedPoint.totalBuilds}", NexusDarkNavy)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Cartesian Bar Chart Canvas + Y-Axis Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Y-Axis Ticks
                Column(
                    modifier = Modifier
                        .width(30.dp)
                        .height(145.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$yAxisMax",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        fontSize = 9.sp
                    )
                    Text(
                        text = "${yAxisMax / 2}",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        fontSize = 9.sp
                    )
                    Text(
                        text = "0",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        fontSize = 9.sp
                    )
                }

                // Interactive Canvas
                val gridColor = NexusBorder
                val autoColor = NexusBlue
                val manualColor = NexusCyan
                val highlightBg = NexusBlueContainer.copy(alpha = 0.45f)

                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(145.dp)
                        .pointerInput(points) {
                            detectTapGestures { offset ->
                                if (points.isNotEmpty()) {
                                    val slotWidth = size.width / points.size.toFloat()
                                    val tapped = (offset.x / slotWidth).toInt().coerceIn(0, points.lastIndex)
                                    selectedIndex = tapped
                                }
                            }
                        }
                ) {
                    val chartH = size.height
                    val chartW = size.width
                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)

                    // Cartesian horizontal gridlines (0%, 50%, 100%)
                    listOf(0f, 0.5f, 1f).forEach { ratio ->
                        val y = chartH * ratio
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(chartW, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = if (ratio < 1f) dashEffect else null
                        )
                    }

                    if (points.isNotEmpty()) {
                        val slotW = chartW / points.size.toFloat()
                        val barW = (slotW * 0.54f).coerceAtMost(26.dp.toPx())

                        points.forEachIndexed { index, pt ->
                            val centerX = slotW * index + slotW / 2f
                            val leftX = centerX - barW / 2f

                            if (index == selectedIndex) {
                                drawRoundRect(
                                    color = highlightBg,
                                    topLeft = Offset(slotW * index + 2.dp.toPx(), 0f),
                                    size = Size(slotW - 4.dp.toPx(), chartH),
                                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                                )
                            }

                            val autoH = (pt.automatedBuilds.toFloat() / yAxisMax) * chartH * animProgress
                            val manualH = (pt.manualBuilds.toFloat() / yAxisMax) * chartH * animProgress

                            // Automated builds (bottom segment)
                            drawRoundRect(
                                color = autoColor,
                                topLeft = Offset(leftX, chartH - autoH),
                                size = Size(barW, autoH),
                                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            )

                            // Manual builds (stacked top segment)
                            if (manualH > 1f) {
                                drawRoundRect(
                                    color = manualColor,
                                    topLeft = Offset(leftX, (chartH - autoH - manualH - 2.dp.toPx()).coerceAtLeast(0f)),
                                    size = Size(barW, manualH),
                                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                                )
                            }
                        }
                    }
                }
            }

            // X-Axis Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                points.forEachIndexed { index, pt ->
                    val isSelected = index == selectedIndex
                    Text(
                        text = pt.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) NexusBlue else NexusTextMuted,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 9.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }
    }
}

/**
 * Chart 2: Recharts-style Smooth Area Chart for Deployment Success Rates
 * Includes dashed 99.0% SLA threshold reference line and interactive point scrubbing.
 */
@Composable
fun DeploymentSuccessAreaChartCard(
    points: List<DeploymentSuccessPoint>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember(points) { mutableIntStateOf((points.size - 1).coerceAtLeast(0)) }
    val animProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing),
        label = "deployAreaAnim"
    )
    val selectedPoint = points.getOrNull(selectedIndex)

    val minRate = 95.0f
    val maxRate = 100.0f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(20.dp))
            .testTag("chart_deployment_success"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "DEPLOYMENT SUCCESS RATE",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Atomic Release Reliability (%)",
                        style = MaterialTheme.typography.titleMedium,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    color = NexusEmeraldBg,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.border(1.dp, NexusEmerald.copy(alpha = 0.3f), RoundedCornerShape(50))
                ) {
                    Text(
                        text = "SLA 99.0%",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedPoint != null) {
                Surface(
                    color = NexusSurfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${selectedPoint.fullDate} (${selectedPoint.label})",
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "p95 edge latency: ${selectedPoint.p95LatencyMs}ms",
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            TooltipMetricPill("Rate", "${selectedPoint.successRatePct}%", NexusEmerald)
                            TooltipMetricPill("Deploys", "${selectedPoint.totalDeployments}", NexusBlue)
                            TooltipMetricPill(
                                "Rollbacks",
                                "${selectedPoint.rollbacks}",
                                if (selectedPoint.rollbacks > 0) NexusOrange else NexusTextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .width(36.dp)
                        .height(140.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("100%", style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 9.sp)
                    Text("97.5%", style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 9.sp)
                    Text("95%", style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 9.sp)
                }

                val gridColor = NexusBorder
                val lineColor = NexusEmerald
                val slaColor = NexusOrange

                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp)
                        .pointerInput(points) {
                            detectTapGestures { offset ->
                                if (points.size > 1) {
                                    val stepX = size.width / (points.size - 1).toFloat()
                                    val idx = (offset.x / stepX).roundToInt().coerceIn(0, points.lastIndex)
                                    selectedIndex = idx
                                }
                            }
                        }
                ) {
                    val w = size.width
                    val h = size.height
                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)

                    // Grid lines
                    listOf(0f, 0.5f, 1f).forEach { ratio ->
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, h * ratio),
                            end = Offset(w, h * ratio),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = if (ratio < 1f) dashEffect else null
                        )
                    }

                    // 99.0% SLA Reference Line
                    val slaRatio = 1f - ((99.0f - minRate) / (maxRate - minRate))
                    drawLine(
                        color = slaColor.copy(alpha = 0.7f),
                        start = Offset(0f, h * slaRatio),
                        end = Offset(w, h * slaRatio),
                        strokeWidth = 1.3.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                    )

                    if (points.size >= 2) {
                        val stepX = w / (points.size - 1).toFloat()
                        val coords = points.mapIndexed { idx, pt ->
                            val norm = ((pt.successRatePct - minRate) / (maxRate - minRate)).coerceIn(0f, 1f)
                            val targetY = h - (norm * h * animProgress)
                            Offset(idx * stepX, targetY)
                        }

                        // Build smooth cubic Bezier path
                        val linePath = Path().apply {
                            moveTo(coords.first().x, coords.first().y)
                            for (i in 0 until coords.lastIndex) {
                                val p0 = coords[i]
                                val p1 = coords[i + 1]
                                val cx = (p0.x + p1.x) / 2f
                                cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                            }
                        }

                        val fillPath = Path().apply {
                            addPath(linePath)
                            lineTo(coords.last().x, h)
                            lineTo(coords.first().x, h)
                            close()
                        }

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    lineColor.copy(alpha = 0.28f),
                                    lineColor.copy(alpha = 0.02f)
                                )
                            )
                        )

                        drawPath(
                            path = linePath,
                            color = lineColor,
                            style = Stroke(width = 2.6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )

                        // Crosshair vertical line for selected point
                        coords.getOrNull(selectedIndex)?.let { sel ->
                            drawLine(
                                color = lineColor.copy(alpha = 0.45f),
                                start = Offset(sel.x, 0f),
                                end = Offset(sel.x, h),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = dashEffect
                            )
                        }

                        // Data point nodes
                        coords.forEachIndexed { idx, pt ->
                            val isSelected = idx == selectedIndex
                            drawCircle(
                                color = Color.White,
                                radius = if (isSelected) 6.dp.toPx() else 3.5.dp.toPx(),
                                center = pt
                            )
                            drawCircle(
                                color = lineColor,
                                radius = if (isSelected) 4.2.dp.toPx() else 2.2.dp.toPx(),
                                center = pt
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 36.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                points.forEachIndexed { index, pt ->
                    val isSelected = index == selectedIndex
                    Text(
                        text = pt.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) NexusEmerald else NexusTextMuted,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 9.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Chart 3: Recharts-style Multi-Series Line Chart for Team Activity Over Time
 * Supports interactive series legend toggles (Commits, PR Reviews, Security Audits) + tap inspection.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TeamActivityLineChartCard(
    points: List<TeamActivityPoint>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember(points) { mutableIntStateOf((points.size - 1).coerceAtLeast(0)) }
    var showCommits by remember { mutableStateOf(true) }
    var showReviews by remember { mutableStateOf(true) }
    var showAudits by remember { mutableStateOf(true) }

    val animProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "teamLineAnim"
    )

    val maxVal = remember(points, showCommits, showReviews, showAudits) {
        val peak = points.maxOfOrNull { pt ->
            maxOf(
                if (showCommits) pt.commits else 0,
                if (showReviews) pt.prReviews else 0,
                if (showAudits) pt.securityAudits else 0
            )
        } ?: 20
        (((peak + 9) / 10) * 10).coerceAtLeast(20)
    }

    val selectedPoint = points.getOrNull(selectedIndex)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(20.dp))
            .testTag("chart_team_activity"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "TEAM ACTIVITY OVER TIME",
                style = MaterialTheme.typography.labelSmall,
                color = NexusPurple,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.8.sp
            )
            Text(
                text = "Commits, Reviews & Security Audits",
                style = MaterialTheme.typography.titleMedium,
                color = NexusDarkNavy,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Interactive Series Toggle Pills
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SeriesToggleChip(
                    label = "Commits",
                    color = NexusBlue,
                    active = showCommits,
                    onClick = { showCommits = !showCommits }
                )
                SeriesToggleChip(
                    label = "PR Reviews",
                    color = NexusPurple,
                    active = showReviews,
                    onClick = { showReviews = !showReviews }
                )
                SeriesToggleChip(
                    label = "Security Audits",
                    color = NexusOrange,
                    active = showAudits,
                    onClick = { showAudits = !showAudits }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedPoint != null) {
                Surface(
                    color = NexusSurfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${selectedPoint.fullDate} (${selectedPoint.label})",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (showCommits) TooltipMetricPill("Commits", "${selectedPoint.commits}", NexusBlue)
                            if (showReviews) TooltipMetricPill("Reviews", "${selectedPoint.prReviews}", NexusPurple)
                            if (showAudits) TooltipMetricPill("Audits", "${selectedPoint.securityAudits}", NexusOrange)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .width(30.dp)
                        .height(140.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("$maxVal", style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 9.sp)
                    Text("${maxVal / 2}", style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 9.sp)
                    Text("0", style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 9.sp)
                }

                val gridColor = NexusBorder
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp)
                        .pointerInput(points) {
                            detectTapGestures { offset ->
                                if (points.size > 1) {
                                    val stepX = size.width / (points.size - 1).toFloat()
                                    val idx = (offset.x / stepX).roundToInt().coerceIn(0, points.lastIndex)
                                    selectedIndex = idx
                                }
                            }
                        }
                ) {
                    val w = size.width
                    val h = size.height
                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)

                    listOf(0f, 0.5f, 1f).forEach { ratio ->
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, h * ratio),
                            end = Offset(w, h * ratio),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = if (ratio < 1f) dashEffect else null
                        )
                    }

                    if (points.size >= 2) {
                        val stepX = w / (points.size - 1).toFloat()

                        // Selected vertical crosshair
                        val selX = selectedIndex * stepX
                        drawLine(
                            color = NexusDarkNavy.copy(alpha = 0.2f),
                            start = Offset(selX, 0f),
                            end = Offset(selX, h),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = dashEffect
                        )

                        fun drawSeries(values: List<Int>, color: Color) {
                            val coords = values.mapIndexed { idx, v ->
                                val norm = (v.toFloat() / maxVal).coerceIn(0f, 1f)
                                Offset(idx * stepX, h - norm * h * animProgress)
                            }
                            val path = Path().apply {
                                moveTo(coords.first().x, coords.first().y)
                                for (i in 0 until coords.lastIndex) {
                                    val p0 = coords[i]
                                    val p1 = coords[i + 1]
                                    val cx = (p0.x + p1.x) / 2f
                                    cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = color,
                                style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                            coords.getOrNull(selectedIndex)?.let { pt ->
                                drawCircle(color = Color.White, radius = 5.dp.toPx(), center = pt)
                                drawCircle(color = color, radius = 3.5.dp.toPx(), center = pt)
                            }
                        }

                        if (showCommits) drawSeries(points.map { it.commits }, NexusBlue)
                        if (showReviews) drawSeries(points.map { it.prReviews }, NexusPurple)
                        if (showAudits) drawSeries(points.map { it.securityAudits }, NexusOrange)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                points.forEachIndexed { index, pt ->
                    val isSelected = index == selectedIndex
                    Text(
                        text = pt.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) NexusPurple else NexusTextMuted,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 9.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun ChartLegendDot(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = NexusTextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TooltipMetricPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = NexusTextMuted,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun SeriesToggleChip(
    label: String,
    color: Color,
    active: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (active) color.copy(alpha = 0.12f) else NexusSurfaceVariant,
        shape = RoundedCornerShape(50),
        modifier = Modifier.border(
            width = 1.dp,
            color = if (active) color.copy(alpha = 0.45f) else NexusBorder,
            shape = RoundedCornerShape(50)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(if (active) color else NexusTextMuted)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (active) NexusDarkNavy else NexusTextMuted,
                fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                fontSize = 10.sp
            )
        }
    }
}

/**
 * Security, OpenSSF ("SSF"), ProGuard/R8, and Documentation Badges Strip.
 * Tap any badge to open the full interactive Compliance & Documentation Modal Sheet.
 */
@Composable
fun SecuritySsfBadgesSection(
    badges: List<ComplianceBadge>,
    onBadgeClick: (ComplianceBadge) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SECURITY, OPENSSF & GOVERNANCE",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Verified Compliance Badges & Docs",
                    style = MaterialTheme.typography.titleMedium,
                    color = NexusDarkNavy,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Surface(
                color = NexusEmeraldBg,
                shape = RoundedCornerShape(50),
                modifier = Modifier.border(1.dp, NexusEmerald.copy(alpha = 0.3f), RoundedCornerShape(50))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "OpenSSF Verified",
                        tint = NexusEmerald,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "SSF PASSING",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                }
            }
        }

        // Horizontal scroll of crisp, shield-style Compliance Badges
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            badges.forEach { badge ->
                ComplianceBadgeCard(
                    badge = badge,
                    onClick = { onBadgeClick(badge) }
                )
            }
        }
    }
}

@Composable
private fun ComplianceBadgeCard(
    badge: ComplianceBadge,
    onClick: () -> Unit
) {
    val (accent, bg, icon) = when (badge.id) {
        "openssf-best-practices" -> Triple(NexusEmerald, NexusEmeraldBg, Icons.Default.Verified)
        "openssf-scorecard" -> Triple(NexusBlue, NexusBlueContainer, Icons.Default.VerifiedUser)
        "proguard-r8" -> Triple(NexusPurple, NexusPurpleBg, Icons.Default.Lock)
        "security-mitm" -> Triple(NexusCyan, NexusCyanBg, Icons.Default.Shield)
        else -> Triple(NexusOrange, NexusOrangeBg, Icons.Default.Description)
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(196.dp)
            .minimumInteractiveComponentSize()
            .border(1.dp, NexusBorder, RoundedCornerShape(16.dp))
            .testTag("compliance_badge_${badge.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = bg,
                    modifier = Modifier.size(30.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = badge.title,
                            tint = accent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Shield-style two-tone status pill
                Surface(
                    color = bg,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.border(1.dp, accent.copy(alpha = 0.35f), RoundedCornerShape(50))
                ) {
                    Text(
                        text = badge.statusText,
                        style = MaterialTheme.typography.labelSmall,
                        color = accent,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = badge.category,
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                letterSpacing = 0.6.sp
            )
            Text(
                text = badge.title,
                style = MaterialTheme.typography.titleSmall,
                color = NexusDarkNavy,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = badge.scoreOrLevel,
                    style = MaterialTheme.typography.labelSmall,
                    color = accent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                Text(
                    text = "View Docs →",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplianceDocModalSheet(
    badge: ComplianceBadge,
    allBadges: List<ComplianceBadge>,
    onSelectBadge: (ComplianceBadge) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NexusSurface,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = NexusEmeraldBg,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Security & SSF Documentation",
                                tint = NexusEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = badge.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = badge.docTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, NexusBorder, CircleShape)
                        .testTag("close_compliance_doc_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NexusTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick switch tabs across all badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                allBadges.forEach { item ->
                    val selected = item.id == badge.id
                    Surface(
                        onClick = { onSelectBadge(item) },
                        color = if (selected) NexusBlue else NexusSurfaceVariant,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.border(
                            1.dp,
                            if (selected) NexusBlue else NexusBorder,
                            RoundedCornerShape(50)
                        )
                    ) {
                        Text(
                            text = item.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selected) Color.White else NexusTextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status Banner
            Surface(
                color = NexusEmeraldBg,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusEmerald.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NexusEmerald,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Status: ${badge.statusText}",
                            style = MaterialTheme.typography.labelMedium,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = badge.scoreOrLevel,
                        style = MaterialTheme.typography.labelMedium,
                        color = NexusEmerald,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = badge.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextPrimary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "VERIFIED CONTROLS & DOCUMENTATION",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                badge.highlights.forEach { point ->
                    Surface(
                        color = NexusSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NexusBlue,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(15.dp)
                            )
                            Text(
                                text = point,
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextPrimary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                color = NexusSurfaceVariant,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder, RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SOURCE ARTIFACT",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = badge.referencePath,
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlue,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
