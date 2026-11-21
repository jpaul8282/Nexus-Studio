package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.ChatMessage
import com.example.model.ChatSender
import com.example.model.UserProfile
import com.example.ui.theme.NexusBg
import com.example.ui.theme.NexusBlue
import com.example.ui.theme.NexusBlueContainer
import com.example.ui.theme.NexusBorder
import com.example.ui.theme.NexusDarkNavy
import com.example.ui.theme.NexusEmerald
import com.example.ui.theme.NexusEmeraldBg
import com.example.ui.theme.NexusOnBlueContainer
import com.example.ui.theme.NexusSurface
import com.example.ui.theme.NexusSurfaceVariant
import com.example.ui.theme.NexusTextMuted
import com.example.ui.theme.NexusTextPrimary
import com.example.ui.theme.NexusTextSecondary
import com.example.viewmodel.NexusStudioViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    viewModel: NexusStudioViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isGenerating by viewModel.isChatGenerating.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var inputPrompt by remember { mutableStateOf("") }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        containerColor = NexusBg,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NexusBlueContainer,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Hub,
                                    contentDescription = null,
                                    tint = NexusBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Nexus AI Copilot",
                                style = MaterialTheme.typography.titleMedium,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(NexusEmerald, CircleShape)
                                )
                                Text(
                                    text = "GEMINI 3.5 FLASH • ARCHITECTURE ENGINE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NexusEmerald,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("chat_back_button")
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
                        onClick = {
                            viewModel.clearChat()
                            Toast.makeText(context, "Chat history cleared", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("clear_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CleaningServices,
                            contentDescription = "Clear Chat",
                            tint = NexusTextMuted
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NexusSurface
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NexusSurface)
                    .border(1.dp, NexusBorder)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .windowInsetsPadding(WindowInsets.ime)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                // Quick Suggestion Chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    val suggestions = listOf(
                        "Explain Web3 Zero-Knowledge specs",
                        "Evaluate FinTech concurrency pipeline",
                        "Dutch GDPR & Article 17 protocols",
                        "Help me draft a systems project brief",
                        "Distributed edge latency benchmarks"
                    )
                    items(suggestions) { suggestion ->
                        Surface(
                            onClick = {
                                inputPrompt = suggestion
                                viewModel.sendChatMessage(suggestion)
                            },
                            shape = RoundedCornerShape(50),
                            color = NexusSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorder),
                            modifier = Modifier.testTag("suggestion_chip_${suggestion.take(10)}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = NexusBlue,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = suggestion,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NexusDarkNavy,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Chat Input Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputPrompt,
                        onValueChange = { inputPrompt = it },
                        placeholder = {
                            Text(
                                text = "Ask Nexus Architecture Copilot...",
                                color = NexusTextMuted,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = false,
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexusBlue,
                            unfocusedBorderColor = NexusBorder,
                            focusedContainerColor = NexusSurfaceVariant,
                            unfocusedContainerColor = NexusSurfaceVariant
                        )
                    )

                    Surface(
                        onClick = {
                            if (inputPrompt.isNotBlank() && !isGenerating) {
                                val query = inputPrompt
                                inputPrompt = ""
                                viewModel.sendChatMessage(query)
                            }
                        },
                        shape = CircleShape,
                        color = if (inputPrompt.isNotBlank() && !isGenerating) NexusBlue else NexusSurfaceVariant,
                        modifier = Modifier
                            .size(46.dp)
                            .testTag("chat_send_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isGenerating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = NexusBlue
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    tint = if (inputPrompt.isNotBlank()) Color.White else NexusTextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("chat_messages_list"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                ChatMessageItem(
                    message = message,
                    userProfile = userProfile,
                    onSuggestionClick = { prompt ->
                        viewModel.sendChatMessage(prompt)
                    }
                )
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    userProfile: UserProfile,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUser = message.sender == ChatSender.USER
    val context = LocalContext.current

    val timeString = remember(message.timestamp) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            // Assistant Avatar
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .border(1.dp, NexusBlue.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                color = NexusBlueContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "Copilot",
                        tint = NexusBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            // Sender name & time header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isUser) userProfile.fullName else "Nexus Copilot",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    fontSize = 9.sp
                )
            }

            // Message Bubble
            Surface(
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = if (isUser) 18.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 18.dp
                ),
                color = if (isUser) NexusBlue else NexusSurface,
                border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, NexusBorder),
                modifier = Modifier.testTag(if (isUser) "user_message_bubble" else "assistant_message_bubble")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (message.isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = NexusBlue
                            )
                            TypingIndicator()
                        }
                    } else {
                        Text(
                            text = message.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isUser) Color.White else NexusTextPrimary,
                            lineHeight = 20.sp,
                            fontSize = 13.sp
                        )

                        if (!isUser && message.text.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Copilot Response", message.text))
                                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    color = NexusSurfaceVariant
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = null,
                                            tint = NexusTextSecondary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "Copy",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = NexusTextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Quick suggestion buttons under initial greeting
            if (!isUser && message.suggestedActions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    message.suggestedActions.forEach { action ->
                        Surface(
                            onClick = { onSuggestionClick(action) },
                            shape = RoundedCornerShape(12.dp),
                            color = NexusSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBlue.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = action,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NexusBlue,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = NexusBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // User Avatar
            if (userProfile.hasCustomAvatar) {
                AsyncImage(
                    model = userProfile.profileImageUrl,
                    contentDescription = userProfile.fullName,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, NexusBlue, RoundedCornerShape(10.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, NexusBorder, RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    color = NexusBlueContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = userProfile.avatarInitials,
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    val transition = rememberInfiniteTransition(label = "typing")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Text(
        text = "Nexus Copilot is thinking...",
        style = MaterialTheme.typography.bodySmall,
        color = NexusTextSecondary,
        modifier = Modifier.alpha(alpha),
        fontSize = 12.sp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotModalSheet(
    viewModel: NexusStudioViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NexusBg,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(640.dp)
        ) {
            ChatbotScreen(
                viewModel = viewModel,
                onBack = onDismiss
            )
        }
    }
}
