package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.ProjectRepository
import com.example.model.CmsProject
import com.example.model.StudioInquiry
import com.example.model.StudioService
import com.example.ui.theme.NexusBg
import com.example.ui.theme.NexusBlue
import com.example.ui.theme.NexusBlueContainer
import com.example.ui.theme.NexusBorder
import com.example.ui.theme.NexusDarkNavy
import com.example.ui.theme.NexusOnBlueContainer
import com.example.ui.theme.NexusOrange
import com.example.ui.theme.NexusOrangeBg
import com.example.ui.theme.NexusSurface
import com.example.ui.theme.NexusSurfaceMuted
import com.example.ui.theme.NexusSurfaceVariant
import com.example.ui.theme.NexusTextMuted
import com.example.ui.theme.NexusTextPrimary
import com.example.ui.theme.NexusTextSecondary
import com.example.viewmodel.ProjectFilterCategory

@Composable
fun StudioTopBar(
    favoriteCount: Int,
    showFavoritesOnly: Boolean,
    onToggleFavorites: () -> Unit,
    onOpenPortal: () -> Unit,
    onOpenInquiry: () -> Unit,
    avatarInitials: String = "JP",
    profileImageUrl: String? = null,
    onOpenProfile: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder),
        color = NexusSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Monogram (Clean Minimalist styling)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.testTag("brand_logo_row")
            ) {
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = NexusBlue
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Hub,
                            contentDescription = "Nexus Studio Logo",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = "Nexus Studio",
                        style = MaterialTheme.typography.titleMedium,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.2).sp
                    )
                    Text(
                        text = "DIGITAL CRAFT",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            // Quick actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Favorites toggle pill
                Surface(
                    color = if (showFavoritesOnly) NexusBlueContainer else NexusSurfaceVariant,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .clickable { onToggleFavorites() }
                        .border(
                            1.dp,
                            if (showFavoritesOnly) NexusBlue else NexusBorder,
                            RoundedCornerShape(50)
                        )
                        .testTag("favorites_filter_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = if (showFavoritesOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmarks",
                            tint = if (showFavoritesOnly) NexusBlue else NexusTextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "$favoriteCount",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (showFavoritesOnly) NexusBlue else NexusTextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Inquire Button
                Button(
                    onClick = onOpenInquiry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NexusBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 7.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("top_inquire_button")
                ) {
                    Text(
                        text = "Brief Studio",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Portal Profile Button with Photo/Initials Avatar
                Surface(
                    onClick = onOpenProfile ?: onOpenPortal,
                    shape = CircleShape,
                    color = NexusBlueContainer,
                    modifier = Modifier
                        .size(34.dp)
                        .border(1.dp, NexusBlue.copy(alpha = 0.3f), CircleShape)
                        .testTag("portal_profile_button")
                ) {
                    if (!profileImageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(profileImageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "User Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = avatarInitials,
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroSection(
    cmsStatus: String,
    onInquireClick: () -> Unit,
    onNavigateToTemplates: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NexusBg)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        // Clean Minimalism Featured Hero Container Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NexusBlueContainer),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Status Pill Badge & Templates Quick Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.9f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(NexusBlue)
                        )
                        Text(
                            text = "SYSTEM ACTIVE // $cmsStatus",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusDarkNavy,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Surface(
                        onClick = onNavigateToTemplates,
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBlue.copy(alpha = 0.3f)),
                        modifier = Modifier.testTag("hero_templates_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✨ Templates",
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Headline
                Text(
                    text = "Crafting Digital Precision.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = NexusDarkNavy,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp,
                    lineHeight = 36.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Minimalist creative engineering studio delivering luxury web systems, autonomous agent pipelines, and high-throughput enterprise platforms.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NexusTextSecondary,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onInquireClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NexusBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .height(42.dp)
                            .testTag("hero_brief_button")
                    ) {
                        Text(
                            text = "Start a Project",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Surface(
                        onClick = onNavigateToTemplates,
                        shape = RoundedCornerShape(50),
                        color = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier
                            .height(42.dp)
                            .border(1.dp, NexusBorder, RoundedCornerShape(50))
                            .testTag("hero_browse_templates_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Browse Architecture Specs",
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Client Roster Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NexusSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "SELECT CLIENT ROSTER",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProjectRepository.CLIENTS.forEach { client ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(NexusBlue)
                            )
                            Text(
                                text = client,
                                style = MaterialTheme.typography.labelMedium,
                                color = NexusTextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryFilterBar(
    selectedCategory: ProjectFilterCategory,
    onSelectCategory: (ProjectFilterCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProjectFilterCategory.values().forEach { category ->
            val isSelected = category == selectedCategory
            Surface(
                color = if (isSelected) NexusBlue else NexusSurface,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .clickable { onSelectCategory(category) }
                    .border(
                        1.dp,
                        if (isSelected) NexusBlue else NexusBorder,
                        RoundedCornerShape(50)
                    )
                    .testTag("filter_chip_${category.name.lowercase()}")
            ) {
                Text(
                    text = category.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) Color.White else NexusTextSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectCard(
    project: CmsProject,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .border(1.dp, NexusBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("project_card_${project.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // Visual Image Header with rounded top corners
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
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

                // Top bar overlay (index & favorite)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.92f),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.border(1.dp, NexusBorder, RoundedCornerShape(50))
                    ) {
                        Text(
                            text = "#${project.id.padStart(2, '0')}",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.92f),
                        shape = CircleShape,
                        modifier = Modifier
                            .clickable { onToggleFavorite() }
                            .border(1.dp, NexusBorder, CircleShape)
                            .testTag("bookmark_btn_${project.id}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) NexusBlue else NexusTextSecondary,
                            modifier = Modifier
                                .padding(7.dp)
                                .size(16.dp)
                        )
                    }
                }
            }

            // Card Body Info Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
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
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Text(
                        text = project.latency,
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        shape = CircleShape,
                        color = NexusSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.NorthEast,
                                contentDescription = "Open",
                                tint = NexusBlue,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = NexusTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Technologies FlowRow
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
        }
    }
}

@Composable
fun TechPill(text: String) {
    Surface(
        color = NexusSurfaceVariant,
        shape = RoundedCornerShape(50),
        modifier = Modifier.border(1.dp, NexusBorder, RoundedCornerShape(50))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = NexusTextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun CodeViewerCard(
    code: String,
    title: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, NexusBorder, RoundedCornerShape(16.dp))
            .background(Color(0xFF13151A))
    ) {
        // Code header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1B1E26))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(NexusBlue)
                )
                Text(
                    text = title.ifBlank { "Source Architecture" },
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Surface(
                color = if (copied) NexusBlue else Color(0xFF282C38),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .clickable {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Source Code", code)
                        clipboard.setPrimaryClip(clip)
                        copied = true
                        Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
                    }
                    .testTag("copy_code_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = if (copied) "Copied" else "Copy Code",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Code content with line numbering
        val lines = remember(code) { code.lines() }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            lines.forEachIndexed { index, line ->
                Row(modifier = Modifier.padding(vertical = 1.dp)) {
                    Text(
                        text = "${index + 1}".padStart(2, ' '),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        ),
                        color = Color(0xFF6B7280),
                        modifier = Modifier.width(28.dp)
                    )
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        ),
                        color = if (line.trim().startsWith("//")) {
                            Color(0xFF93C5FD)
                        } else if (line.contains("import") || line.contains("export") || line.contains("class") || line.contains("const") || line.contains("val")) {
                            Color(0xFFFDBA74)
                        } else {
                            Color(0xFFE5E7EB)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SpecMetricsGrid(
    project: CmsProject,
    modifier: Modifier = Modifier
) {
    val specs = listOf(
        Triple("LATENCY TARGET", project.latency, Icons.Default.Speed),
        Triple("SECURITY LAYER", project.security, Icons.Default.Shield),
        Triple("UPTIME INTEGRITY", project.uptime, Icons.Default.Verified),
        Triple("PLATFORM TYPE", project.platformType, Icons.Default.Tune)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        specs.chunked(2).forEach { rowSpecs ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowSpecs.forEach { (label, value, icon) ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = NexusSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = NexusBlueContainer,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = NexusBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusTextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = value,
                                style = MaterialTheme.typography.titleMedium,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Architecture summary callout card
        Surface(
            color = NexusBlueContainer.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NexusBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = NexusBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "ARCHITECTURE & INFRASTRUCTURE SUMMARY",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = project.architectureSummary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = NexusTextPrimary,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiryModalSheet(
    services: List<StudioService>,
    selectedService: StudioService?,
    successMessage: String?,
    initialNotes: String = "",
    onDismiss: () -> Unit,
    onSubmit: (name: String, email: String, serviceTitle: String, budget: String, timeline: String, notes: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var clientName by remember { mutableStateOf("") }
    var clientEmail by remember { mutableStateOf("") }
    var selectedServiceTitle by remember { mutableStateOf(selectedService?.title ?: services.firstOrNull()?.title ?: "Digital Architecture") }
    var selectedBudget by remember { mutableStateOf("$25k - $50k") }
    var selectedTimeline by remember { mutableStateOf("4-6 Weeks") }
    var projectNotes by remember(initialNotes) { mutableStateOf(initialNotes) }

    val budgetOptions = listOf("< $25k", "$25k - $50k", "$50k - $100k", "$100k+")
    val timelineOptions = listOf("2-4 Weeks", "4-6 Weeks", "8+ Weeks", "Ongoing Retainer")

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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Studio Briefing",
                        style = MaterialTheme.typography.headlineSmall,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Submit parameters for your next digital build",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, NexusBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NexusTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (successMessage != null) {
                // Success state
                Surface(
                    color = NexusBlueContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NexusBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = NexusBlue,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Briefing Registered",
                            style = MaterialTheme.typography.titleMedium,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = successMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Close Briefing", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelMedium)
                }
            } else {
                // Inputs
                Text(
                    text = "SELECT SERVICE ARCHITECTURE",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    services.forEach { service ->
                        val isSelected = service.title == selectedServiceTitle
                        Surface(
                            color = if (isSelected) NexusBlueContainer else NexusSurfaceVariant,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedServiceTitle = service.title }
                                .border(
                                    1.dp,
                                    if (isSelected) NexusBlue else NexusBorder,
                                    RoundedCornerShape(14.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = service.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (isSelected) NexusDarkNavy else NexusTextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = service.tagline,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NexusTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = NexusBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Budget Range Pills
                Text(
                    text = "TARGET BUDGET BRACKET",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    budgetOptions.forEach { budget ->
                        val isSelected = budget == selectedBudget
                        Surface(
                            color = if (isSelected) NexusBlue else NexusSurfaceVariant,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .clickable { selectedBudget = budget }
                                .border(
                                    1.dp,
                                    if (isSelected) NexusBlue else NexusBorder,
                                    RoundedCornerShape(50)
                                )
                        ) {
                            Text(
                                text = budget,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color.White else NexusTextSecondary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Target Timeline Pills
                Text(
                    text = "ESTIMATED TIMELINE",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timelineOptions.forEach { timeline ->
                        val isSelected = timeline == selectedTimeline
                        Surface(
                            color = if (isSelected) NexusBlue else NexusSurfaceVariant,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .clickable { selectedTimeline = timeline }
                                .border(
                                    1.dp,
                                    if (isSelected) NexusBlue else NexusBorder,
                                    RoundedCornerShape(50)
                                )
                        ) {
                            Text(
                                text = timeline,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color.White else NexusTextSecondary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name & Email
                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("Lead Contact / Company Name", style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.fillMaxWidth().testTag("inquiry_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexusBlue,
                        unfocusedBorderColor = NexusBorder,
                        focusedLabelColor = NexusBlue,
                        unfocusedLabelColor = NexusTextMuted,
                        focusedTextColor = NexusTextPrimary,
                        unfocusedTextColor = NexusTextPrimary,
                        unfocusedContainerColor = NexusSurfaceVariant,
                        focusedContainerColor = NexusSurface
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = clientEmail,
                    onValueChange = { clientEmail = it },
                    label = { Text("Direct Email Address", style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.fillMaxWidth().testTag("inquiry_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexusBlue,
                        unfocusedBorderColor = NexusBorder,
                        focusedLabelColor = NexusBlue,
                        unfocusedLabelColor = NexusTextMuted,
                        focusedTextColor = NexusTextPrimary,
                        unfocusedTextColor = NexusTextPrimary,
                        unfocusedContainerColor = NexusSurfaceVariant,
                        focusedContainerColor = NexusSurface
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = projectNotes,
                    onValueChange = { projectNotes = it },
                    label = { Text("Architectural Specifications / Project Notes", style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("inquiry_notes_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexusBlue,
                        unfocusedBorderColor = NexusBorder,
                        focusedLabelColor = NexusBlue,
                        unfocusedLabelColor = NexusTextMuted,
                        focusedTextColor = NexusTextPrimary,
                        unfocusedTextColor = NexusTextPrimary,
                        unfocusedContainerColor = NexusSurfaceVariant,
                        focusedContainerColor = NexusSurface
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onSubmit(clientName, clientEmail, selectedServiceTitle, selectedBudget, selectedTimeline, projectNotes)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_briefing_button")
                ) {
                    Text(
                        text = "Transmit Architectural Brief",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientPortalModalSheet(
    clientName: String,
    clientTier: String,
    cmsStatus: String,
    favoriteProjects: List<CmsProject>,
    inquiries: List<StudioInquiry>,
    onDismiss: () -> Unit,
    onProjectClick: (String) -> Unit,
    onOpenPrivacyPolicy: () -> Unit = {},
    onOpenAccountProfile: () -> Unit = {}
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Client Portal",
                        style = MaterialTheme.typography.headlineSmall,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Enterprise Client & Architectural Gateway",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, NexusBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NexusTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Client Status Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = NexusSurfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CLIENT IDENTITY",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = NexusBlueContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "VERIFIED",
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusBlue,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = clientName,
                        style = MaterialTheme.typography.titleLarge,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = clientTier,
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlue,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NexusBorder, RoundedCornerShape(10.dp))
                            .background(NexusSurface, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CMS PROTOCOL:",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextMuted,
                            fontSize = 10.sp
                        )
                        Text(
                            text = cmsStatus,
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onDismiss()
                            onOpenAccountProfile()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("portal_account_profile_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Manage Account Profile & Security",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Favorited Projects
            Text(
                text = "SAVED ARCHITECTURAL BUILDS (${favoriteProjects.size})",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (favoriteProjects.isEmpty()) {
                Surface(
                    color = NexusSurfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NexusBorder, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "No saved projects yet. Bookmark projects in the portfolio to pin them here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextMuted
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    favoriteProjects.forEach { project ->
                        Surface(
                            color = NexusSurface,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDismiss()
                                    onProjectClick(project.id)
                                }
                                .border(1.dp, NexusBorder, RoundedCornerShape(14.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = project.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = NexusDarkNavy,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = project.category,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NexusBlue,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Open",
                                    tint = NexusBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submitted Inquiries History
            Text(
                text = "TRANSMITTED BRIEFS (${inquiries.size})",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (inquiries.isEmpty()) {
                Surface(
                    color = NexusSurfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NexusBorder, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "No briefings transmitted in current session.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextMuted
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    inquiries.forEach { inq ->
                        Surface(
                            color = NexusSurface,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, NexusBorder, RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = inq.id,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NexusBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = inq.status,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NexusOrange,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = inq.serviceTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = NexusDarkNavy,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Budget: ${inq.budgetRange} • Timeline: ${inq.timeline}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NexusTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Privacy & Data Governance Card
            Text(
                text = "DATA GOVERNANCE & PRIVACY",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = NexusSurfaceVariant,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder, RoundedCornerShape(14.dp))
                    .clickable {
                        onDismiss()
                        onOpenPrivacyPolicy()
                    }
                    .testTag("portal_privacy_policy_button")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = NexusBlueContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = NexusBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Privacy Policy & GDPR Notice",
                                style = MaterialTheme.typography.titleSmall,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Updated Aug 16, 2026 • FIRST AID SPONDER",
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View Policy",
                        tint = NexusBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
