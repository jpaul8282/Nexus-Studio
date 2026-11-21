package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.CmsProject
import com.example.ui.theme.NexusBg
import com.example.ui.theme.NexusBlue
import com.example.ui.theme.NexusBlueContainer
import com.example.ui.theme.NexusBorder
import com.example.ui.theme.NexusDarkNavy
import com.example.ui.theme.NexusOnBlueContainer
import com.example.ui.theme.NexusSurface
import com.example.ui.theme.NexusSurfaceVariant
import com.example.ui.theme.NexusTextMuted
import com.example.ui.theme.NexusTextPrimary
import com.example.ui.theme.NexusTextSecondary

/**
 * Gallery Card designed specifically for LazyVerticalGrid layout.
 * Showcases digital experience and architecture projects with high-quality imagery,
 * metadata pills, quick bookmark action, and responsive aspect ratios.
 */
@Composable
fun GalleryProjectCard(
    project: CmsProject,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag("gallery_item_${project.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // High-Quality Visual Imagery Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
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

                // Subtle gradient overlay at top for badge legibility
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.45f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Top Overlay: Index badge + Bookmark toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.94f),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.border(0.5.dp, NexusBorder, RoundedCornerShape(50))
                    ) {
                        Text(
                            text = "#${project.id.padStart(2, '0')}",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusDarkNavy,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.94f),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onToggleFavorite() }
                            .border(0.5.dp, NexusBorder, CircleShape)
                            .testTag("gallery_bookmark_${project.id}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) NexusBlue else NexusTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Bottom badge overlay on image (Latency SLA)
                Surface(
                    color = NexusDarkNavy.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(topStart = 8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = project.latency,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Project Details Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Category Tag
                Surface(
                    color = NexusBlueContainer,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = project.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusOnBlueContainer,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Title and Launch Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.NorthEast,
                        contentDescription = "View",
                        tint = NexusBlue,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Short Description
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Primary Tech Pills (first 2 items for grid brevity)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    project.technologies.take(2).forEach { tech ->
                        Surface(
                            color = NexusSurfaceVariant,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.border(0.5.dp, NexusBorder, RoundedCornerShape(50))
                        ) {
                            Text(
                                text = tech,
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusTextSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (project.technologies.size > 2) {
                        Surface(
                            color = NexusSurfaceVariant,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.border(0.5.dp, NexusBorder, RoundedCornerShape(50))
                        ) {
                            Text(
                                text = "+${project.technologies.size - 2}",
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusTextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dedicated Project Gallery Component powered by LazyVerticalGrid.
 * Showcases digital experience and architecture projects in an adaptive 2-column or multi-column grid.
 */
@Composable
fun ProjectGalleryGrid(
    projects: List<CmsProject>,
    favoriteIds: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onProjectClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    headerContent: (@Composable () -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("project_gallery_grid"),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (headerContent != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                headerContent()
            }
        }

        items(
            items = projects,
            key = { it.id }
        ) { project ->
            GalleryProjectCard(
                project = project,
                isFavorite = favoriteIds.contains(project.id),
                onToggleFavorite = { onToggleFavorite(project.id) },
                onClick = { onProjectClick(project.id) }
            )
        }

        if (footerContent != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                footerContent()
            }
        }
    }
}

/**
 * Gallery View Mode toggle control bar.
 */
@Composable
fun PortfolioViewToggle(
    isGridMode: Boolean,
    onToggleMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = NexusSurfaceVariant,
        shape = RoundedCornerShape(50),
        modifier = modifier
            .border(1.dp, NexusBorder, RoundedCornerShape(50))
            .testTag("portfolio_view_toggle")
    ) {
        Row(
            modifier = Modifier.padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (isGridMode) NexusBlue else Color.Transparent,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .clickable { onToggleMode(true) }
                    .testTag("toggle_grid_mode")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Gallery Grid View",
                        tint = if (isGridMode) Color.White else NexusTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Gallery",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isGridMode) Color.White else NexusTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (isGridMode) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }

            Surface(
                color = if (!isGridMode) NexusBlue else Color.Transparent,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .clickable { onToggleMode(false) }
                    .testTag("toggle_list_mode")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewAgenda,
                        contentDescription = "Editorial Stream View",
                        tint = if (!isGridMode) Color.White else NexusTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Editorial",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (!isGridMode) Color.White else NexusTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (!isGridMode) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
