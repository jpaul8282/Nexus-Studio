package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ForwardToInbox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ArchitectureTemplate
import com.example.model.CustomTemplateRequest
import com.example.model.TemplateCategory
import com.example.ui.theme.NexusBg
import com.example.ui.theme.NexusBlue
import com.example.ui.theme.NexusBlueContainer
import com.example.ui.theme.NexusBorder
import com.example.ui.theme.NexusDarkNavy
import com.example.ui.theme.NexusEmerald
import com.example.ui.theme.NexusRuby
import com.example.ui.theme.NexusSurface
import com.example.ui.theme.NexusSurfaceVariant
import com.example.ui.theme.NexusTextMuted
import com.example.ui.theme.NexusTextPrimary
import com.example.ui.theme.NexusTextSecondary
import com.example.viewmodel.NexusStudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplatesScreen(
    viewModel: NexusStudioViewModel,
    onBack: () -> Unit,
    onNavigateToCopilot: () -> Unit,
    onOpenInquiryWithTemplate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val filteredTemplates by viewModel.filteredTemplates.collectAsState()
    val favoriteIds by viewModel.favoriteTemplateIds.collectAsState()

    var showDetailSheet by remember { mutableStateOf(false) }
    var selectedTemplate by remember { mutableStateOf<ArchitectureTemplate?>(null) }
    var showAiGeneratorSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(NexusBg),
        containerColor = NexusBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Architecture Templates",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NexusDarkNavy
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NexusBlue.copy(alpha = 0.12f),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = "AI GENERATOR",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NexusBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Production RFCs, Web3 specs & enterprise blueprints",
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("templates_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NexusDarkNavy
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showAiGeneratorSheet = true },
                        modifier = Modifier.testTag("open_ai_generator_icon")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Generate Custom Template",
                            tint = NexusBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NexusSurface,
                    titleContentColor = NexusDarkNavy
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .testTag("templates_list"),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. AI Generation Hero Banner
            item(key = "hero_generator_banner") {
                HeroGeneratorBanner(
                    onOpenGenerator = { showAiGeneratorSheet = true }
                )
            }

            // 2. Search & Category Filters
            item(key = "search_and_filters") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Search Bar
                    OutlinedTextField(
                        value = uiState.templateSearchQuery,
                        onValueChange = { viewModel.onTemplateSearchQueryChange(it) },
                        placeholder = { Text("Search templates, stacks, or keywords...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NexusBlue)
                        },
                        trailingIcon = {
                            if (uiState.templateSearchQuery.isNotBlank()) {
                                IconButton(onClick = { viewModel.onTemplateSearchQueryChange("") }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = NexusTextMuted)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NexusSurface,
                            unfocusedContainerColor = NexusSurface,
                            focusedBorderColor = NexusBlue,
                            unfocusedBorderColor = NexusBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("template_search_input")
                    )

                    // Category Chips Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Favorites Filter Toggle
                        FilterChip(
                            selected = uiState.showTemplateFavoritesOnly,
                            onClick = { viewModel.toggleTemplateFavoritesOnly() },
                            label = { Text("Saved ⭐", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NexusDarkNavy,
                                selectedLabelColor = Color.White,
                                containerColor = NexusSurface
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = uiState.showTemplateFavoritesOnly,
                                borderColor = if (uiState.showTemplateFavoritesOnly) NexusDarkNavy else NexusBorder
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("filter_favorite_templates")
                        )

                        TemplateCategory.entries.forEach { category ->
                            val isSelected = uiState.selectedTemplateCategory == category
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.onTemplateCategorySelect(category) },
                                label = { Text(category.displayName, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NexusBlue,
                                    selectedLabelColor = Color.White,
                                    containerColor = NexusSurface
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) NexusBlue else NexusBorder
                                ),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.testTag("filter_category_${category.name.lowercase()}")
                            )
                        }
                    }
                }
            }

            // 3. Count Header
            item(key = "templates_count_header") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AVAILABLE TEMPLATES (${filteredTemplates.size})",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.6.sp
                    )

                    Text(
                        text = "Tap to view, copy & customize",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            // 4. Template Cards List
            if (filteredTemplates.isEmpty()) {
                item(key = "empty_templates") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = NexusSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = NexusTextMuted,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No templates matched your query",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NexusDarkNavy
                            )
                            Text(
                                text = "Try adjusting filters or generate a custom template with AI.",
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextSecondary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showAiGeneratorSheet = true },
                                colors = ButtonDefaults.buttonColors(containerColor = NexusBlue),
                                shape = RoundedCornerShape(50)
                            ) {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Generate Custom with AI", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                items(filteredTemplates, key = { it.id }) { template ->
                    val isFavorite = favoriteIds.contains(template.id)
                    ArchitectureTemplateCard(
                        template = template,
                        isFavorite = isFavorite,
                        onToggleFavorite = { viewModel.toggleTemplateFavorite(template.id) },
                        onSelect = {
                            selectedTemplate = template
                            showDetailSheet = true
                        },
                        onQuickCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText(template.title, template.markdownContent))
                            Toast.makeText(context, "'${template.title}' copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        onLoadIntoBrief = {
                            viewModel.applyTemplateToInquiry(template)
                            onOpenInquiryWithTemplate()
                        },
                        onCopilotReview = {
                            viewModel.sendTemplateToCopilot(template, onNavigateToCopilot)
                        }
                    )
                }
            }

            item(key = "templates_footer_spacer") {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Modal: AI Custom Template Generator
    if (showAiGeneratorSheet) {
        CustomTemplateGeneratorSheet(
            isGenerating = uiState.isGeneratingCustomTemplate,
            onDismiss = { showAiGeneratorSheet = false },
            onGenerate = { request ->
                viewModel.generateCustomTemplate(request) { generated ->
                    selectedTemplate = generated
                    showAiGeneratorSheet = false
                    showDetailSheet = true
                }
            }
        )
    }

    // Modal: Template Detail & Customizer Sheet
    if (showDetailSheet && selectedTemplate != null) {
        val activeTemplate = selectedTemplate!!
        TemplateDetailModalSheet(
            template = activeTemplate,
            isFavorite = favoriteIds.contains(activeTemplate.id),
            onDismiss = { showDetailSheet = false },
            onToggleFavorite = { viewModel.toggleTemplateFavorite(activeTemplate.id) },
            onLoadIntoInquiry = {
                viewModel.applyTemplateToInquiry(activeTemplate)
                showDetailSheet = false
                onOpenInquiryWithTemplate()
            },
            onSendToCopilot = {
                viewModel.sendTemplateToCopilot(activeTemplate, onNavigateToCopilot)
                showDetailSheet = false
            },
            onDelete = if (activeTemplate.isCustomGenerated) {
                {
                    viewModel.deleteCustomTemplate(activeTemplate.id)
                    showDetailSheet = false
                }
            } else null
        )
    }
}

@Composable
fun HeroGeneratorBanner(
    onOpenGenerator: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenGenerator() }
            .testTag("hero_generator_banner"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NexusDarkNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            NexusDarkNavy,
                            Color(0xFF0F2B59),
                            NexusBlue.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.Yellow,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "GEMINI 3.5 AI ENGINE",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Text(
                        text = "Instant Generation",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Generate Tailored Systems Architecture",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Input your project domain, latency targets, and compliance requirements to create bespoke RFCs, smart contract specs & Android blueprints.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 16.sp,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onOpenGenerator,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = NexusDarkNavy
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(42.dp)
                        .testTag("hero_generate_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NexusBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generate Custom Template",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArchitectureTemplateCard(
    template: ArchitectureTemplate,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSelect: () -> Unit,
    onQuickCopy: () -> Unit,
    onLoadIntoBrief: () -> Unit,
    onCopilotReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(18.dp))
            .clickable { onSelect() }
            .testTag("template_card_${template.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row: Category Badge + Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NexusBlueContainer
                    ) {
                        Text(
                            text = template.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    if (template.isCustomGenerated) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NexusEmerald.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "AI GENERATED",
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusEmerald,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("fav_btn_${template.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Template",
                        tint = if (isFavorite) NexusBlue else NexusTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Subtitle
            Text(
                text = template.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NexusDarkNavy,
                fontSize = 16.sp
            )

            Text(
                text = template.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = NexusBlue,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = template.description,
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextSecondary,
                lineHeight = 17.sp,
                fontSize = 12.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tech Stack & Tags
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                template.tags.take(4).forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = NexusSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorder)
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextPrimary,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Divider & Actions Row
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = NexusBorder
            ) {}

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = NexusTextMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = template.estimatedTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        fontSize = 11.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Quick Copy
                    IconButton(
                        onClick = onQuickCopy,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("quick_copy_${template.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Markdown",
                            tint = NexusTextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Copilot Review
                    IconButton(
                        onClick = onCopilotReview,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("copilot_review_${template.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "Review in Copilot",
                            tint = NexusBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Load Into Brief
                    Button(
                        onClick = onLoadIntoBrief,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NexusBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("load_brief_${template.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ForwardToInbox,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Use in Brief",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTemplateGeneratorSheet(
    isGenerating: Boolean,
    onDismiss: () -> Unit,
    onGenerate: (CustomTemplateRequest) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var projectTitle by remember { mutableStateOf("") }
    var domain by remember { mutableStateOf("Distributed Systems") }
    var selectedCategory by remember { mutableStateOf(TemplateCategory.ARCHITECTURE_RFC) }
    var targetPlatform by remember { mutableStateOf("Android & Cloud Kubernetes") }
    var throughputTarget by remember { mutableStateOf("< 50ms p99 latency") }
    var complianceStandard by remember { mutableStateOf("Dutch & EU GDPR / SOC-2") }
    var coreFeatures by remember { mutableStateOf("High-concurrency gRPC pipeline, offline-first Room local cache, event-driven replay") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NexusSurface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("custom_template_generator_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NexusBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Template Generator",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = NexusDarkNavy
                        )
                    }
                    Text(
                        text = "Powered by Gemini 3.5 architectural synthesis",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NexusTextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Preset Suggestions
            Text(
                text = "POPULAR PRESETS",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.6.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickPresetButton("DeFi Settlement", "Web3 High-Speed Settlement") {
                    projectTitle = "Aura Decentralized Settlement Bridge"
                    domain = "Web3 & DeFi Liquidity"
                    selectedCategory = TemplateCategory.WEB3_ZK
                    targetPlatform = "Rust, Circom, Ethereum L2"
                    throughputTarget = "< 1.5s finality, 10k TPS"
                    complianceStandard = "European MiCA & Smart Contract Auditing"
                    coreFeatures = "Account Abstraction ERC-4337, zk-SNARK verifier, token paymaster"
                }

                QuickPresetButton("FinTech Order Engine", "High-Throughput FinTech") {
                    projectTitle = "Titan High-Frequency Matching Core"
                    domain = "FinTech & Quantitative Trading"
                    selectedCategory = TemplateCategory.FINTECH_LEDGER
                    targetPlatform = "Java 21, LMAX Disruptor, ScyllaDB"
                    throughputTarget = "Sub-millisecond matching (< 850µs)"
                    complianceStandard = "Dutch AFM / DNB PSD2 & SOC-2"
                    coreFeatures = "FIX 4.4 gateway, double-entry invariant validation, idempotency key ring buffer"
                }

                QuickPresetButton("Android Clean App", "Mobile Client Blueprint") {
                    projectTitle = "Pulse Architecture Client"
                    domain = "Mobile Systems & Jetpack Compose"
                    selectedCategory = TemplateCategory.ANDROID_BLUEPRINT
                    targetPlatform = "Kotlin 2.0, Jetpack Compose, Room KSP"
                    throughputTarget = "60-120fps smooth scrolling, <150ms boot"
                    complianceStandard = "Google Play Security & EU GDPR"
                    coreFeatures = "MVVM UDF flow, Room offline-first sync, M3 dynamic theming, DataStore credentials"
                }

                QuickPresetButton("AI RAG Multi-Agent", "Autonomous Agent Pipeline") {
                    projectTitle = "Nexus Cognition RAG Pipeline"
                    domain = "Enterprise AI & Vector Intelligence"
                    selectedCategory = TemplateCategory.AI_PIPELINE
                    targetPlatform = "Gemini 3.5 Flash, pgvector, Python FastAPI"
                    throughputTarget = "< 350ms Time-to-First-Token"
                    complianceStandard = "EU AI Act 2024 & ISO/IEC 42001"
                    coreFeatures = "Vector chunking 512-dim, reciprocal rank fusion, tool-calling guardrails"
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Project Title
            Text(
                text = "PROJECT / SYSTEM TITLE",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = projectTitle,
                onValueChange = { projectTitle = it },
                placeholder = { Text("e.g., Hyperion Event Streaming Gateway") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_custom_project_title"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Domain / Industry
            Text(
                text = "DOMAIN / INDUSTRY",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = domain,
                onValueChange = { domain = it },
                placeholder = { Text("e.g., Distributed Cloud / Edge Computing") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_custom_domain"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Category Selector
            Text(
                text = "TEMPLATE CATEGORY",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TemplateCategory.entries.filterNot { it == TemplateCategory.ALL }.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.displayName, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NexusBlue,
                            selectedLabelColor = Color.White,
                            containerColor = NexusSurfaceVariant
                        ),
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Target Platform & Stack
            Text(
                text = "TARGET PLATFORM & TECH STACK",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = targetPlatform,
                onValueChange = { targetPlatform = it },
                placeholder = { Text("e.g., Go / Rust, gRPC, Apache Kafka, ScyllaDB") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_custom_platform"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Throughput / Latency Targets
            Text(
                text = "THROUGHPUT & LATENCY TARGETS",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = throughputTarget,
                onValueChange = { throughputTarget = it },
                placeholder = { Text("e.g., < 50ms p99, 100,000 req/sec, 99.999% uptime") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_custom_throughput"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 6. Regulatory / Compliance Standard
            Text(
                text = "COMPLIANCE & REGULATORY FRAMEWORK",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = complianceStandard,
                onValueChange = { complianceStandard = it },
                placeholder = { Text("e.g., Dutch & EU GDPR, SOC-2 Type II, ISO 27001") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_custom_compliance"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 7. Core Capabilities
            Text(
                text = "CORE CAPABILITIES & SPECIAL REQUIREMENTS",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = coreFeatures,
                onValueChange = { coreFeatures = it },
                placeholder = { Text("Describe main modules, failure recovery, security requirements...") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_custom_features"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Action Button
            Button(
                onClick = {
                    val request = CustomTemplateRequest(
                        projectTitle = projectTitle.ifBlank { "Custom ${selectedCategory.displayName}" },
                        domain = domain.ifBlank { "Distributed Systems" },
                        category = selectedCategory,
                        targetPlatform = targetPlatform.ifBlank { "Cross-Platform Edge" },
                        throughputLatencyTarget = throughputTarget.ifBlank { "< 85ms latency" },
                        complianceStandard = complianceStandard.ifBlank { "EU GDPR" },
                        coreFeatures = coreFeatures.ifBlank { "High-concurrency decoupled microservices" }
                    )
                    onGenerate(request)
                },
                enabled = !isGenerating,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NexusBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_generate_template_button")
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Generating Architecture Specification...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Architectural Template", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun QuickPresetButton(
    label: String,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = NexusSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Code,
                contentDescription = contentDescription,
                tint = NexusBlue,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = NexusDarkNavy,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateDetailModalSheet(
    template: ArchitectureTemplate,
    isFavorite: Boolean,
    onDismiss: () -> Unit,
    onToggleFavorite: () -> Unit,
    onLoadIntoInquiry: () -> Unit,
    onSendToCopilot: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isCustomizingVariables by remember { mutableStateOf(false) }
    var customProjectName by remember { mutableStateOf("Nexus Hyperion Gateway") }
    var customAuthorName by remember { mutableStateOf("Lead Systems Architect") }

    val activeContent = remember(template.markdownContent, isCustomizingVariables, customProjectName, customAuthorName) {
        if (isCustomizingVariables) {
            template.markdownContent
                .replace("[PROJECT_NAME]", customProjectName)
                .replace("[YOUR_NAME / LEAD_ARCHITECT]", customAuthorName)
                .replace("[DAPP_PROJECT_NAME]", customProjectName)
                .replace("[FINTECH_ORGANIZATION]", customProjectName)
                .replace("[COMPANY_NAME]", customProjectName)
                .replace("[APP_NAME]", customProjectName)
                .replace("[AI_SYSTEM_NAME]", customProjectName)
                .replace("[CLIENT_COMPANY_NAME]", customProjectName)
                .replace("[DATE]", "2026-08-16")
        } else {
            template.markdownContent
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NexusSurface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("template_detail_modal_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 10.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NexusBlueContainer
                    ) {
                        Text(
                            text = template.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    if (template.isCustomGenerated) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NexusEmerald.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "CUSTOM SPEC",
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusEmerald,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isFavorite) NexusBlue else NexusTextMuted
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NexusTextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Subtitle
            Text(
                text = template.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = NexusDarkNavy
            )

            Text(
                text = template.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = NexusBlue,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Target Stack: ${template.targetStack}",
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Variable Customizer Toggle Bar
            Surface(
                color = NexusSurfaceVariant,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = NexusBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Personalize Template Placeholders",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = NexusDarkNavy
                            )
                        }

                        TextButton(
                            onClick = { isCustomizingVariables = !isCustomizingVariables }
                        ) {
                            Text(
                                text = if (isCustomizingVariables) "Hide" else "Customize",
                                color = NexusBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    AnimatedVisibility(visible = isCustomizingVariables) {
                        Column(
                            modifier = Modifier.padding(top = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = customProjectName,
                                onValueChange = { customProjectName = it },
                                label = { Text("Your Project Name", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = customAuthorName,
                                onValueChange = { customAuthorName = it },
                                label = { Text("Author / Lead Name", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons Strip (Copy, Share, Load Brief, Copilot)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Copy Markdown
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText(template.title, activeContent))
                        Toast.makeText(context, "Full Markdown copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("detail_copy_markdown_btn")
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Markdown", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // 2. Load into Brief
                Button(
                    onClick = onLoadIntoInquiry,
                    colors = ButtonDefaults.buttonColors(containerColor = NexusDarkNavy, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("detail_use_in_brief_btn")
                ) {
                    Icon(imageVector = Icons.Default.ForwardToInbox, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Load into Brief", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // 3. Review in Copilot
                OutlinedButton(
                    onClick = onSendToCopilot,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBlue),
                    modifier = Modifier.testTag("detail_copilot_review_btn")
                ) {
                    Icon(imageVector = Icons.Default.SmartToy, contentDescription = null, tint = NexusBlue, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copilot Review", color = NexusBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // 4. Share System Intent
                OutlinedButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TITLE, template.title)
                            putExtra(Intent.EXTRA_TEXT, activeContent)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Architectural Template"))
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorder)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = NexusTextPrimary, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share", color = NexusTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Markdown Content Viewer Box
            Text(
                text = "SPECIFICATION MARKDOWN",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.6.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = Color(0xFF0F172A),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
            ) {
                SelectionContainer {
                    Text(
                        text = activeContent,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFE2E8F0),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }

            // Delete Custom Option
            if (onDelete != null) {
                Spacer(modifier = Modifier.height(18.dp))
                OutlinedButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusRuby),
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusRuby.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("delete_custom_template_button")
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Custom Template", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
