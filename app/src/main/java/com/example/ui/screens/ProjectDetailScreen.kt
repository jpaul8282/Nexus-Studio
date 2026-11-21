package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.ProjectRepository
import com.example.model.CmsProject
import com.example.ui.components.CodeViewerCard
import com.example.ui.components.ComplianceDocModalSheet
import com.example.ui.components.EngineeringDashboardSection
import com.example.ui.components.InquiryModalSheet
import com.example.ui.components.SecuritySsfBadgesSection
import com.example.ui.components.SpecMetricsGrid
import com.example.ui.components.TechPill
import com.example.ui.theme.NexusBg
import com.example.ui.theme.NexusBlue
import com.example.ui.theme.NexusBlueContainer
import com.example.ui.theme.NexusBorder
import com.example.ui.theme.NexusDarkNavy
import com.example.ui.theme.NexusOnBlueContainer
import com.example.ui.theme.NexusOrange
import com.example.ui.theme.NexusSurface
import com.example.ui.theme.NexusSurfaceVariant
import com.example.ui.theme.NexusTextMuted
import com.example.ui.theme.NexusTextPrimary
import com.example.ui.theme.NexusTextSecondary
import com.example.viewmodel.NexusStudioViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectDetailScreen(
    projectId: String,
    viewModel: NexusStudioViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val project = viewModel.getProject(projectId)
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("OVERVIEW", "CODE ARCHITECTURE", "SYSTEM SPECS")

    if (project == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NexusBg)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Project Not Found",
                    style = MaterialTheme.typography.titleLarge,
                    color = NexusDarkNavy,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Return to Portfolio", fontWeight = FontWeight.SemiBold)
                }
            }
        }
        return
    }

    val isFavorite = favoriteIds.contains(project.id)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        containerColor = NexusBg,
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder),
                color = NexusSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.dp, NexusBorder, CircleShape)
                            .testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NexusTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Surface(
                        color = NexusBlueContainer,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.border(1.dp, NexusBorder, RoundedCornerShape(50))
                    ) {
                        Text(
                            text = "PROJECT #${project.id.padStart(2, '0')}",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusOnBlueContainer,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Share
                        IconButton(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Explore ${project.title} (${project.category}) on Nexus Studio: ${project.link}")
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share Project")
                                context.startActivity(shareIntent)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .border(1.dp, NexusBorder, CircleShape)
                                .testTag("share_project_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = NexusTextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Bookmark
                        IconButton(
                            onClick = { viewModel.toggleFavorite(project.id) },
                            modifier = Modifier
                                .size(36.dp)
                                .border(1.dp, if (isFavorite) NexusBlue else NexusBorder, CircleShape)
                                .background(if (isFavorite) NexusBlueContainer else Color.Transparent)
                                .testTag("detail_favorite_button")
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isFavorite) NexusBlue else NexusTextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                color = NexusSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { viewModel.openInquirySheet() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("inquire_similar_button"),
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusTextPrimary)
                    ) {
                        Text(
                            text = "Commission Build",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            try {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(project.link))
                                context.startActivity(browserIntent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Opening site: ${project.link}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp)
                            .testTag("launch_live_site_button"),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Launch Live Site",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Default.NorthEast,
                                contentDescription = "Launch",
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Project Header Information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NexusBg)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = NexusBlueContainer,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = project.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusOnBlueContainer,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = NexusSurface,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.border(1.dp, NexusBorder, RoundedCornerShape(50))
                    ) {
                        Text(
                            text = "CLIENT: ${project.client.uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextMuted,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = project.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = NexusDarkNavy,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Technology Stack Pills
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    project.technologies.forEach { tech ->
                        TechPill(text = tech)
                    }
                }
            }

            // Tab Row Navigation
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NexusSurface,
                contentColor = NexusBlue,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = NexusBlue,
                        height = 3.dp
                    )
                },
                divider = {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(NexusBorder))
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) NexusBlue else NexusTextSecondary,
                                fontSize = 11.sp
                            )
                        },
                        modifier = Modifier.testTag("tab_${title.lowercase().replace(" ", "_")}")
                    )
                }
            }

            // Tab Contents
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        // Overview Tab
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            // Hero Imagery
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                                    .border(1.dp, NexusBorder, RoundedCornerShape(20.dp))
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(NexusSurfaceVariant)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(project.image)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = project.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // Executive Summary Card
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
                                colors = CardDefaults.cardColors(containerColor = NexusSurface),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text(
                                        text = "EXECUTIVE SUMMARY",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NexusBlue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = project.longOverview.ifBlank { project.description },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = NexusTextPrimary,
                                        lineHeight = 22.sp
                                    )
                                }
                            }

                            // Key Features Checklist
                            if (project.keyFeatures.isNotEmpty()) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
                                    colors = CardDefaults.cardColors(containerColor = NexusSurface),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Text(
                                            text = "ARCHITECTURAL DELIVERABLES & FEATURES",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = NexusBlue,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                            project.keyFeatures.forEach { feature ->
                                                Row(
                                                    verticalAlignment = Alignment.Top,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    Surface(
                                                        shape = CircleShape,
                                                        color = NexusBlueContainer,
                                                        modifier = Modifier.size(20.dp)
                                                    ) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Icon(
                                                                imageVector = Icons.Default.Check,
                                                                contentDescription = "Delivered",
                                                                tint = NexusBlue,
                                                                modifier = Modifier.size(12.dp)
                                                            )
                                                        }
                                                    }
                                                    Text(
                                                        text = feature,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = NexusTextSecondary,
                                                        lineHeight = 20.sp,
                                                        modifier = Modifier.padding(top = 1.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Code Architecture Tab
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            CodeViewerCard(
                                code = project.code,
                                title = "${project.title} Core Architecture"
                            )
                        }
                    }

                    2 -> {
                        // System Specs & Project Telemetry Tab
                        val projectTelemetry = remember(project.id, uiState.selectedTelemetryRange, uiState.selectedPipelineEnv) {
                            viewModel.getProjectTelemetry(project.id)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            SpecMetricsGrid(project = project)
                            EngineeringDashboardSection(
                                snapshot = projectTelemetry,
                                selectedTimeRange = uiState.selectedTelemetryRange,
                                selectedEnvironment = uiState.selectedPipelineEnv,
                                onTimeRangeChange = { viewModel.onTelemetryRangeSelect(it) },
                                onEnvironmentChange = { viewModel.onPipelineEnvironmentSelect(it) },
                                sectionTitle = "${project.title} Telemetry",
                                sectionEyebrow = "PROJECT PIPELINE METRICS"
                            )
                            SecuritySsfBadgesSection(
                                badges = viewModel.complianceBadges,
                                onBadgeClick = { viewModel.openComplianceBadgeDoc(it) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Modal Inquiry Sheet
    if (uiState.showInquirySheet) {
        InquiryModalSheet(
            services = ProjectRepository.STUDIO_SERVICES,
            selectedService = uiState.selectedServiceForInquiry,
            successMessage = uiState.inquirySuccessMessage,
            initialNotes = uiState.prefilledInquiryNotes ?: "",
            onDismiss = { viewModel.closeInquirySheet() },
            onSubmit = { name, email, service, budget, timeline, notes ->
                viewModel.submitInquiry(name, email, service, budget, timeline, notes)
            }
        )
    }

    uiState.selectedComplianceBadge?.let { badge ->
        ComplianceDocModalSheet(
            badge = badge,
            allBadges = viewModel.complianceBadges,
            onSelectBadge = { viewModel.openComplianceBadgeDoc(it) },
            onDismiss = { viewModel.closeComplianceBadgeDoc() }
        )
    }
}
