package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProjectRepository
import com.example.model.StudioService
import com.example.ui.components.CategoryFilterBar
import com.example.ui.components.ClientPortalModalSheet
import com.example.ui.components.ComplianceDocModalSheet
import com.example.ui.components.EngineeringDashboardSection
import com.example.ui.components.GalleryProjectCard
import com.example.ui.components.HeroSection
import com.example.ui.components.InquiryModalSheet
import com.example.ui.components.PortfolioViewToggle
import com.example.ui.components.ProjectCard
import com.example.ui.components.SecuritySsfBadgesSection
import com.example.ui.components.StudioTopBar
import com.example.ui.theme.NexusBg
import com.example.ui.theme.NexusBlue
import com.example.ui.theme.NexusBlueContainer
import com.example.ui.theme.NexusBorder
import com.example.ui.theme.NexusDarkNavy
import com.example.ui.theme.NexusEmerald
import com.example.ui.theme.NexusEmeraldBg
import com.example.ui.theme.NexusOnBlueContainer
import com.example.ui.theme.NexusOrange
import com.example.ui.theme.NexusSurface
import com.example.ui.theme.NexusSurfaceVariant
import com.example.ui.theme.NexusTextMuted
import com.example.ui.theme.NexusTextPrimary
import com.example.ui.theme.NexusTextSecondary
import com.example.viewmodel.NexusStudioViewModel

@Composable
fun HomeScreen(
    viewModel: NexusStudioViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit = {},
    onNavigateToAccountProfile: () -> Unit = {},
    onNavigateToChatbot: () -> Unit = {},
    onNavigateToTemplates: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val filteredProjects by viewModel.filteredProjects.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val inquiries by viewModel.inquiries.collectAsState()
    val cmsStatus by viewModel.cmsStatus.collectAsState()
    val telemetrySnapshot by viewModel.telemetrySnapshot.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        containerColor = NexusBg,
        topBar = {
            StudioTopBar(
                favoriteCount = favoriteIds.size,
                showFavoritesOnly = uiState.showFavoritesOnly,
                onToggleFavorites = { viewModel.toggleFavoritesOnly() },
                onOpenPortal = { viewModel.openClientPortal() },
                onOpenInquiry = { viewModel.openInquirySheet() },
                avatarInitials = userProfile.avatarInitials,
                profileImageUrl = userProfile.profileImageUrl,
                onOpenProfile = onNavigateToAccountProfile
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToChatbot,
                containerColor = NexusBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(50),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "Nexus Copilot",
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "AI Copilot",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("fab_chatbot")
            )
        }
    ) { innerPadding ->
        if (uiState.isGalleryGridMode) {
            // High-Impact Project Gallery View (LazyVerticalGrid)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("project_gallery_grid"),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Hero Section
                item(key = "hero", span = { GridItemSpan(maxLineSpan) }) {
                    HeroSection(
                        cmsStatus = cmsStatus,
                        onInquireClick = { viewModel.openInquirySheet() },
                        onNavigateToTemplates = onNavigateToTemplates
                    )
                }

                // 1b. Dashboard Focus Switcher (All Overview vs Telemetry & SSF Docs)
                item(key = "dashboard_focus_tabs", span = { GridItemSpan(maxLineSpan) }) {
                    DashboardFocusTabs(
                        activeFocus = uiState.activeDashboardFocus,
                        onSelectFocus = { viewModel.onDashboardFocusSelect(it) }
                    )
                }

                if (uiState.activeDashboardFocus == 1) {
                    item(key = "telemetry_dashboard_top", span = { GridItemSpan(maxLineSpan) }) {
                        EngineeringDashboardSection(
                            snapshot = telemetrySnapshot,
                            selectedTimeRange = uiState.selectedTelemetryRange,
                            selectedEnvironment = uiState.selectedPipelineEnv,
                            onTimeRangeChange = { viewModel.onTelemetryRangeSelect(it) },
                            onEnvironmentChange = { viewModel.onPipelineEnvironmentSelect(it) }
                        )
                    }
                    item(key = "security_ssf_badges_top", span = { GridItemSpan(maxLineSpan) }) {
                        SecuritySsfBadgesSection(
                            badges = viewModel.complianceBadges,
                            onBadgeClick = { viewModel.openComplianceBadgeDoc(it) }
                        )
                    }
                }

                // 2. Search Bar
                item(key = "search_bar", span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            placeholder = {
                                Text(
                                    text = "Search architectural builds, stack, or category...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NexusTextMuted
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = NexusBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = NexusTextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_input_field"),
                            shape = RoundedCornerShape(50),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NexusBlue,
                                unfocusedBorderColor = NexusBorder,
                                focusedTextColor = NexusTextPrimary,
                                unfocusedTextColor = NexusTextPrimary,
                                cursorColor = NexusBlue,
                                focusedContainerColor = NexusSurface,
                                unfocusedContainerColor = NexusSurface
                            )
                        )
                    }
                }

                // 3. Category Filter Chips
                item(key = "category_filters", span = { GridItemSpan(maxLineSpan) }) {
                    CategoryFilterBar(
                        selectedCategory = uiState.selectedCategory,
                        onSelectCategory = { viewModel.onCategorySelect(it) }
                    )
                }

                // 4. Portfolio Section Header with View Mode Toggle
                item(key = "portfolio_header", span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Project Gallery",
                                style = MaterialTheme.typography.titleMedium,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = NexusBlueContainer,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "${filteredProjects.size} showcases",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NexusOnBlueContainer,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        PortfolioViewToggle(
                            isGridMode = uiState.isGalleryGridMode,
                            onToggleMode = { viewModel.setGalleryGridMode(it) }
                        )
                    }
                }

                // 5. Empty State
                if (filteredProjects.isEmpty()) {
                    item(key = "empty_state", span = { GridItemSpan(maxLineSpan) }) {
                        EmptyProjectsState(
                            onReset = {
                                viewModel.onSearchQueryChange("")
                                viewModel.onCategorySelect(com.example.viewmodel.ProjectFilterCategory.ALL)
                                if (uiState.showFavoritesOnly) viewModel.toggleFavoritesOnly()
                            }
                        )
                    }
                }

                // 6. Gallery Project Cards
                items(
                    items = filteredProjects,
                    key = { it.id }
                ) { project ->
                    Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                        GalleryProjectCard(
                            project = project,
                            isFavorite = favoriteIds.contains(project.id),
                            onToggleFavorite = { viewModel.toggleFavorite(project.id) },
                            onClick = { onNavigateToDetail(project.id) }
                        )
                    }
                }

                // 7. Engineering Telemetry Charts (Build Frequency, Deploy Success, Team Activity)
                if (uiState.activeDashboardFocus == 0) {
                    item(key = "telemetry_dashboard", span = { GridItemSpan(maxLineSpan) }) {
                        EngineeringDashboardSection(
                            snapshot = telemetrySnapshot,
                            selectedTimeRange = uiState.selectedTelemetryRange,
                            selectedEnvironment = uiState.selectedPipelineEnv,
                            onTimeRangeChange = { viewModel.onTelemetryRangeSelect(it) },
                            onEnvironmentChange = { viewModel.onPipelineEnvironmentSelect(it) }
                        )
                    }

                    // 7b. Security, OpenSSF ("SSF"), ProGuard & Documentation Badges
                    item(key = "security_ssf_badges", span = { GridItemSpan(maxLineSpan) }) {
                        SecuritySsfBadgesSection(
                            badges = viewModel.complianceBadges,
                            onBadgeClick = { viewModel.openComplianceBadgeDoc(it) }
                        )
                    }
                }

                // 8. Studio Services Section
                item(key = "services_section", span = { GridItemSpan(maxLineSpan) }) {
                    StudioDisciplinesSection(
                        onInquire = { viewModel.openInquirySheet(it) }
                    )
                }

                // 9. Telemetry Metrics
                item(key = "telemetry_metrics", span = { GridItemSpan(maxLineSpan) }) {
                    StudioMetricsSection()
                }

                // 9. CTA Banner
                item(key = "cta_banner", span = { GridItemSpan(maxLineSpan) }) {
                    StudioCtaBanner(onInquireClick = { viewModel.openInquirySheet() })
                }

                // 10. Footer
                item(key = "footer", span = { GridItemSpan(maxLineSpan) }) {
                    StudioFooter(
                        onPrivacyPolicyClick = onNavigateToPrivacyPolicy,
                        onAccountProfileClick = onNavigateToAccountProfile
                    )
                }
            }
        } else {
            // Editorial Stream View (LazyColumn)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("home_projects_list")
            ) {
                item(key = "hero") {
                    HeroSection(
                        cmsStatus = cmsStatus,
                        onInquireClick = { viewModel.openInquirySheet() },
                        onNavigateToTemplates = onNavigateToTemplates
                    )
                }

                item(key = "dashboard_focus_tabs") {
                    DashboardFocusTabs(
                        activeFocus = uiState.activeDashboardFocus,
                        onSelectFocus = { viewModel.onDashboardFocusSelect(it) }
                    )
                }

                if (uiState.activeDashboardFocus == 1) {
                    item(key = "telemetry_dashboard_top") {
                        EngineeringDashboardSection(
                            snapshot = telemetrySnapshot,
                            selectedTimeRange = uiState.selectedTelemetryRange,
                            selectedEnvironment = uiState.selectedPipelineEnv,
                            onTimeRangeChange = { viewModel.onTelemetryRangeSelect(it) },
                            onEnvironmentChange = { viewModel.onPipelineEnvironmentSelect(it) }
                        )
                    }
                    item(key = "security_ssf_badges_top") {
                        SecuritySsfBadgesSection(
                            badges = viewModel.complianceBadges,
                            onBadgeClick = { viewModel.openComplianceBadgeDoc(it) }
                        )
                    }
                }

                item(key = "search_and_filter") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            placeholder = {
                                Text(
                                    text = "Search architectural builds, stack, or category...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NexusTextMuted
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = NexusBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = NexusTextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_input_field"),
                            shape = RoundedCornerShape(50),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NexusBlue,
                                unfocusedBorderColor = NexusBorder,
                                focusedTextColor = NexusTextPrimary,
                                unfocusedTextColor = NexusTextPrimary,
                                cursorColor = NexusBlue,
                                focusedContainerColor = NexusSurface,
                                unfocusedContainerColor = NexusSurface
                            )
                        )
                    }
                }

                item(key = "category_filters") {
                    CategoryFilterBar(
                        selectedCategory = uiState.selectedCategory,
                        onSelectCategory = { viewModel.onCategorySelect(it) }
                    )
                }

                item(key = "portfolio_header") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Selected Portfolio",
                                style = MaterialTheme.typography.titleMedium,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = NexusBlueContainer,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "${filteredProjects.size} builds",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NexusOnBlueContainer,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        PortfolioViewToggle(
                            isGridMode = uiState.isGalleryGridMode,
                            onToggleMode = { viewModel.setGalleryGridMode(it) }
                        )
                    }
                }

                if (filteredProjects.isEmpty()) {
                    item(key = "empty_state") {
                        EmptyProjectsState(
                            onReset = {
                                viewModel.onSearchQueryChange("")
                                viewModel.onCategorySelect(com.example.viewmodel.ProjectFilterCategory.ALL)
                                if (uiState.showFavoritesOnly) viewModel.toggleFavoritesOnly()
                            }
                        )
                    }
                }

                items(
                    items = filteredProjects,
                    key = { it.id }
                ) { project ->
                    ProjectCard(
                        project = project,
                        isFavorite = favoriteIds.contains(project.id),
                        onToggleFavorite = { viewModel.toggleFavorite(project.id) },
                        onClick = { onNavigateToDetail(project.id) }
                    )
                }

                if (uiState.activeDashboardFocus == 0) {
                    item(key = "telemetry_dashboard") {
                        EngineeringDashboardSection(
                            snapshot = telemetrySnapshot,
                            selectedTimeRange = uiState.selectedTelemetryRange,
                            selectedEnvironment = uiState.selectedPipelineEnv,
                            onTimeRangeChange = { viewModel.onTelemetryRangeSelect(it) },
                            onEnvironmentChange = { viewModel.onPipelineEnvironmentSelect(it) }
                        )
                    }

                    item(key = "security_ssf_badges") {
                        SecuritySsfBadgesSection(
                            badges = viewModel.complianceBadges,
                            onBadgeClick = { viewModel.openComplianceBadgeDoc(it) }
                        )
                    }
                }

                item(key = "services_section") {
                    StudioDisciplinesSection(
                        onInquire = { viewModel.openInquirySheet(it) }
                    )
                }

                item(key = "telemetry_metrics") {
                    StudioMetricsSection()
                }

                item(key = "cta_banner") {
                    StudioCtaBanner(onInquireClick = { viewModel.openInquirySheet() })
                }

                item(key = "footer") {
                    StudioFooter(
                        onPrivacyPolicyClick = onNavigateToPrivacyPolicy,
                        onAccountProfileClick = onNavigateToAccountProfile
                    )
                }
            }
        }
    }

    // Modal Sheets
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

    if (uiState.showClientPortalSheet) {
        ClientPortalModalSheet(
            clientName = userProfile.fullName,
            clientTier = userProfile.tier,
            cmsStatus = cmsStatus,
            favoriteProjects = ProjectRepository.DEFAULT_PROJECTS.filter { favoriteIds.contains(it.id) },
            inquiries = inquiries,
            onDismiss = { viewModel.closeClientPortal() },
            onProjectClick = { projectId ->
                onNavigateToDetail(projectId)
            },
            onOpenPrivacyPolicy = {
                onNavigateToPrivacyPolicy()
            },
            onOpenAccountProfile = {
                onNavigateToAccountProfile()
            }
        )
    }
}

@Composable
private fun DashboardFocusTabs(
    activeFocus: Int,
    onSelectFocus: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            0 to "Overview & Portfolio",
            1 to "Telemetry & SSF Badges"
        ).forEach { (index, label) ->
            val selected = activeFocus == index
            Surface(
                onClick = { onSelectFocus(index) },
                color = if (selected) NexusBlue else NexusSurface,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = if (selected) NexusBlue else NexusBorder,
                        shape = RoundedCornerShape(50)
                    )
                    .testTag("dashboard_focus_tab_$index")
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected) Color.White else NexusTextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyProjectsState(
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .border(1.dp, NexusBorder, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "No results",
                tint = NexusTextMuted,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No matching builds found",
                style = MaterialTheme.typography.titleMedium,
                color = NexusDarkNavy,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Try adjusting your search criteria or resetting filters.",
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                shape = RoundedCornerShape(50)
            ) {
                Text("Reset Filters", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun StudioDisciplinesSection(
    onInquire: (StudioService) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "STUDIO DISCIPLINES",
            style = MaterialTheme.typography.labelSmall,
            color = NexusTextMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Engineered Excellence",
            style = MaterialTheme.typography.headlineSmall,
            color = NexusDarkNavy,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ProjectRepository.STUDIO_SERVICES.forEach { service ->
                ServiceShowcaseCard(
                    service = service,
                    onInquire = { onInquire(service) }
                )
            }
        }
    }
}

@Composable
fun StudioMetricsSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "BENCHMARKS & SLA",
            style = MaterialTheme.typography.labelSmall,
            color = NexusTextMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ProjectRepository.STUDIO_METRICS.forEach { metric ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = NexusSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = metric.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusTextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = metric.value,
                                style = MaterialTheme.typography.titleLarge,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = metric.detail,
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        Surface(
                            color = NexusEmeraldBg,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.border(1.dp, NexusEmerald.copy(alpha = 0.3f), RoundedCornerShape(50))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = NexusEmerald,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = metric.change,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NexusEmerald,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudioCtaBanner(
    onInquireClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NexusBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Text(
                    text = "NOW ACCEPTING SELECT COMMISSIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Have a vision for your next digital platform?",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Connect with the Nexus Studio technical architecture team for scoping, architecture design, and production timelines.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onInquireClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = NexusBlue
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("cta_brief_button")
            ) {
                Text(
                    text = "Initiate Studio Briefing",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StudioFooter(
    onPrivacyPolicyClick: () -> Unit = {},
    onAccountProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder)
            .background(NexusSurface)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nexus Studio © 2026",
                style = MaterialTheme.typography.labelMedium,
                color = NexusDarkNavy,
                fontWeight = FontWeight.Bold
            )
            Surface(
                color = NexusBlueContainer,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "ALL SYSTEMS ONLINE",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusOnBlueContainer,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Decoupled edge architecture • High-frequency telemetry • Multimodal AI integrations",
            style = MaterialTheme.typography.bodySmall,
            color = NexusTextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = onAccountProfileClick,
                color = NexusBlueContainer,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .border(1.dp, NexusBlue.copy(alpha = 0.3f), RoundedCornerShape(50))
                    .testTag("footer_account_profile_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = NexusBlue,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Account & Profile",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Surface(
                onClick = onPrivacyPolicyClick,
                color = NexusSurfaceVariant,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .border(1.dp, NexusBorder, RoundedCornerShape(50))
                    .testTag("footer_privacy_policy_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = NexusBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Privacy Policy",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceShowcaseCard(
    service: StudioService,
    onInquire: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = service.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = NexusDarkNavy,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    onClick = onInquire,
                    shape = CircleShape,
                    color = NexusSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.NorthEast,
                            contentDescription = "Inquire",
                            tint = NexusBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Text(
                text = service.tagline,
                style = MaterialTheme.typography.bodySmall,
                color = NexusBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Deliverables
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                service.deliverables.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NexusBlue)
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
