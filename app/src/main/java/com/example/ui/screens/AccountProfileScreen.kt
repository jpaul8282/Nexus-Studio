package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.UserProfile
import com.example.ui.theme.NexusBg
import com.example.ui.theme.NexusBlue
import com.example.ui.theme.NexusBlueContainer
import com.example.ui.theme.NexusBorder
import com.example.ui.theme.NexusDarkNavy
import com.example.ui.theme.NexusEmerald
import com.example.ui.theme.NexusEmeraldBg
import com.example.ui.theme.NexusOnBlueContainer
import com.example.ui.theme.NexusOrange
import com.example.ui.theme.NexusOrangeBg
import com.example.ui.theme.NexusRuby
import com.example.ui.theme.NexusSurface
import com.example.ui.theme.NexusSurfaceVariant
import com.example.ui.theme.NexusTextMuted
import com.example.ui.theme.NexusTextPrimary
import com.example.ui.theme.NexusTextSecondary
import com.example.viewmodel.NexusStudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountProfileScreen(
    viewModel: NexusStudioViewModel,
    onBack: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToChatbot: () -> Unit = {},
    onNavigateToTemplates: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val profile by viewModel.userProfile.collectAsState()
    val inquiries by viewModel.inquiries.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    var showEditProfileSheet by remember { mutableStateOf(false) }
    var showPhotoPickerSheet by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showExportDataDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showSessionsDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateProfilePhoto(it.toString())
            Toast.makeText(context, "Profile picture updated from gallery", Toast.LENGTH_SHORT).show()
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
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Account & Profile",
                            style = MaterialTheme.typography.titleMedium,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "IDENTITY & GOVERNANCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("account_back_button")
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
                        onClick = { showEditProfileSheet = true },
                        modifier = Modifier.testTag("edit_profile_top_action")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = NexusBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NexusSurface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .testTag("account_profile_list"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Profile Hero Card with Photo Picker affordance
            item(key = "profile_hero") {
                ProfileHeroCard(
                    profile = profile,
                    onEditClick = { showEditProfileSheet = true },
                    onPhotoClick = { showPhotoPickerSheet = true }
                )
            }

            // 2. Quick Metrics Row
            item(key = "profile_metrics") {
                ProfileMetricsRow(
                    securityScore = profile.securityScore,
                    inquiryCount = inquiries.size,
                    favoriteCount = favoriteIds.size,
                    memberSince = profile.memberSince
                )
            }

            // 3. Section: Profile & Identity Details
            item(key = "identity_details") {
                AccountSectionCard(
                    title = "ORGANIZATION & IDENTITY",
                    subtitle = "Verified enterprise profile credentials"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        InfoRow(
                            icon = Icons.Default.Badge,
                            label = "Account ID",
                            value = profile.id,
                            actionLabel = "Copy",
                            onAction = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Account ID", profile.id))
                                Toast.makeText(context, "Account ID copied", Toast.LENGTH_SHORT).show()
                            }
                        )
                        InfoRow(
                            icon = Icons.Default.Email,
                            label = "Corporate Email",
                            value = profile.email
                        )
                        InfoRow(
                            icon = Icons.Default.Business,
                            label = "Organization",
                            value = profile.organization
                        )
                        InfoRow(
                            icon = Icons.Default.Public,
                            label = "Jurisdiction & Region",
                            value = "${profile.country} (GDPR Compliant)"
                        )
                        InfoRow(
                            icon = Icons.Default.Phone,
                            label = "Direct Contact",
                            value = profile.phone
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showEditProfileSheet = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("edit_profile_button"),
                            shape = RoundedCornerShape(50),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = NexusBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Edit Profile & Professional Bio",
                                style = MaterialTheme.typography.labelMedium,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // 4. Section: Communication & Notification Settings
            item(key = "notification_settings") {
                AccountSectionCard(
                    title = "COMMUNICATION & ALERTS",
                    subtitle = "Direct notification channels and delivery preferences"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingToggleRow(
                            icon = Icons.Default.Notifications,
                            title = "Push Notifications",
                            subtitle = "Architecture project milestones & build status",
                            checked = profile.pushNotificationsEnabled,
                            onCheckedChange = { viewModel.togglePushNotifications() },
                            testTag = "toggle_push_notifications"
                        )
                        SettingToggleRow(
                            icon = Icons.Default.Sms,
                            title = "SMS Text Alerts",
                            subtitle = "Critical security OTP & emergency responses (No-sharing policy)",
                            checked = profile.smsAlertsEnabled,
                            onCheckedChange = { viewModel.toggleSmsAlerts() },
                            testTag = "toggle_sms_alerts"
                        )
                        SettingToggleRow(
                            icon = Icons.Default.Email,
                            title = "Weekly Architecture Digest",
                            subtitle = "Curated Web3 & distributed systems benchmark reports",
                            checked = profile.emailDigestEnabled,
                            onCheckedChange = { viewModel.toggleEmailDigest() },
                            testTag = "toggle_email_digest"
                        )
                    }
                }
            }

            // 5. Section: Security & Authentication Settings
            item(key = "security_settings") {
                AccountSectionCard(
                    title = "SECURITY & ACCESS CONTROL",
                    subtitle = "Enterprise authentication and active device management"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingToggleRow(
                            icon = Icons.Default.Security,
                            title = "Two-Factor Authentication (2FA)",
                            subtitle = "Hardware key or TOTP authenticator verification",
                            checked = profile.twoFactorAuthEnabled,
                            onCheckedChange = { viewModel.toggleTwoFactorAuth() },
                            testTag = "toggle_two_factor_auth"
                        )
                        SettingToggleRow(
                            icon = Icons.Default.Fingerprint,
                            title = "Biometric Passcode / Touch ID",
                            subtitle = "Unlock client architectural telemetry with biometrics",
                            checked = profile.biometricAuthEnabled,
                            onCheckedChange = { viewModel.toggleBiometricAuth() },
                            testTag = "toggle_biometric_auth"
                        )

                        ActionNavRow(
                            icon = Icons.Default.Devices,
                            title = "Active Sessions & Authorized Devices",
                            subtitle = "2 active sessions (Amsterdam, Utrecht)",
                            onClick = { showSessionsDialog = true },
                            testTag = "active_sessions_row"
                        )

                        ActionNavRow(
                            icon = Icons.Default.Key,
                            title = "Change Master Password",
                            subtitle = "Last rotated 45 days ago",
                            onClick = { showChangePasswordDialog = true },
                            testTag = "change_password_row"
                        )
                    }
                }
            }

            // 6. Section: Privacy & Data Governance (GDPR)
            item(key = "privacy_gdpr_settings") {
                AccountSectionCard(
                    title = "DATA GOVERNANCE & PRIVACY (GDPR)",
                    subtitle = "Data subject rights, exports and erasure controls"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ActionNavRow(
                            icon = Icons.Default.Download,
                            title = "Export Personal Data",
                            subtitle = "Download all telemetry, inquiries and profile records as JSON",
                            onClick = { showExportDataDialog = true },
                            testTag = "export_personal_data_row"
                        )

                        ActionNavRow(
                            icon = Icons.Default.Shield,
                            title = "Privacy Policy & Legal Terms",
                            subtitle = "Review Dutch jurisdiction privacy policies and SMS notices",
                            onClick = onNavigateToPrivacyPolicy,
                            testTag = "privacy_policy_nav_row"
                        )

                        ActionNavRow(
                            icon = Icons.Default.DeleteForever,
                            title = "Request Account Erasure",
                            subtitle = "Permanently delete account records under GDPR Article 17",
                            iconTint = NexusRuby,
                            titleColor = NexusRuby,
                            onClick = { showDeleteAccountDialog = true },
                            testTag = "delete_account_row"
                        )
                    }
                }
            }

            // 7. Section: Diagnostics & Application State
            item(key = "app_preferences") {
                AccountSectionCard(
                    title = "DIAGNOSTICS & SYSTEM PREFERENCES",
                    subtitle = "Local state cache and telemetry stream"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingToggleRow(
                            icon = Icons.Default.Tune,
                            title = "High-Frequency Telemetry",
                            subtitle = "Stream real-time latency (< 85ms) and memory stats",
                            checked = profile.highFrequencyTelemetry,
                            onCheckedChange = { viewModel.toggleHighFrequencyTelemetry() },
                            testTag = "toggle_telemetry"
                        )

                        ActionNavRow(
                            icon = Icons.Default.SmartToy,
                            title = "Nexus AI Architecture Copilot",
                            subtitle = "Interactive systems assistant powered by Gemini 1.5 Flash",
                            onClick = onNavigateToChatbot,
                            testTag = "open_copilot_from_profile"
                        )

                        ActionNavRow(
                            icon = Icons.Default.Badge,
                            title = "Architecture Templates Engine",
                            subtitle = "Browse & generate reusable system blueprints",
                            onClick = onNavigateToTemplates,
                            testTag = "open_templates_from_profile"
                        )

                        ActionNavRow(
                            icon = Icons.Default.CleaningServices,
                            title = "Clear Local Data & Cache",
                            subtitle = "Free memory without removing saved preferences",
                            onClick = {
                                viewModel.clearLocalCache()
                                Toast.makeText(context, "Local cache cleared (14.2 MB freed)", Toast.LENGTH_SHORT).show()
                            },
                            testTag = "clear_cache_row"
                        )
                    }
                }
            }

            // 8. Footer Info
            item(key = "account_footer") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "FIRST AID SPONDER // NEXUS STUDIO BUILD v3.4.0",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Jurisdiction: Netherlands (EU) • GDPR & SOC-2 Compliant",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextMuted,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Modal Edit Profile Sheet
    if (showEditProfileSheet) {
        EditProfileModalSheet(
            profile = profile,
            onDismiss = { showEditProfileSheet = false },
            onSave = { name, email, org, role, phone, country, bio ->
                viewModel.updateProfile(name, email, org, role, phone, country, bio)
                showEditProfileSheet = false
                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onConfirm = {
                showChangePasswordDialog = false
                Toast.makeText(context, "Password updated securely", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Export Data Dialog
    if (showExportDataDialog) {
        val jsonData = viewModel.exportPersonalDataJson()
        ExportDataDialog(
            jsonData = jsonData,
            onDismiss = { showExportDataDialog = false },
            onCopy = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Nexus Studio Data Export", jsonData))
                Toast.makeText(context, "Data JSON copied to clipboard", Toast.LENGTH_SHORT).show()
            },
            onShare = {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, jsonData)
                    type = "application/json"
                }
                context.startActivity(Intent.createChooser(sendIntent, "Share GDPR Data Export"))
            }
        )
    }

    // Delete Account Dialog
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = {
                Text(
                    text = "Request Account Erasure",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NexusDarkNavy
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to request data erasure for ${profile.email}? In compliance with GDPR and our 24-month retention schedule, all active session tokens and personal identifiers will be scheduled for permanent anonymization.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NexusTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountDialog = false
                        viewModel.showNotification("Erasure request submitted to Data Protection Officer.")
                        Toast.makeText(context, "Erasure request dispatched", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NexusRuby, contentColor = Color.White)
                ) {
                    Text("Confirm Erasure", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel", color = NexusTextSecondary)
                }
            },
            containerColor = NexusSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Active Sessions Dialog
    if (showSessionsDialog) {
        ActiveSessionsDialog(
            onDismiss = { showSessionsDialog = false },
            onRevokeOther = {
                showSessionsDialog = false
                Toast.makeText(context, "Remote session terminated", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Profile Photo Picker Sheet
    if (showPhotoPickerSheet) {
        ProfilePhotoPickerSheet(
            profile = profile,
            onDismiss = { showPhotoPickerSheet = false },
            onPickGallery = {
                showPhotoPickerSheet = false
                photoPickerLauncher.launch("image/*")
            },
            onSelectPreset = { presetUrl ->
                viewModel.updateProfilePhoto(presetUrl)
                showPhotoPickerSheet = false
                Toast.makeText(context, "Preset avatar applied", Toast.LENGTH_SHORT).show()
            },
            onSaveCustomUrl = { customUrl ->
                viewModel.updateProfilePhoto(customUrl)
                showPhotoPickerSheet = false
                Toast.makeText(context, "Profile picture URL updated", Toast.LENGTH_SHORT).show()
            },
            onRemovePhoto = {
                viewModel.updateProfilePhoto(null)
                showPhotoPickerSheet = false
                Toast.makeText(context, "Profile photo removed", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun ProfileHeroCard(
    profile: UserProfile,
    onEditClick: () -> Unit,
    onPhotoClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .border(1.dp, NexusBorder, RoundedCornerShape(24.dp))
            .testTag("profile_hero_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Avatar Squircle with Photo and Camera Badge
                    Box(
                        modifier = Modifier
                            .clickable { onPhotoClick() }
                            .testTag("avatar_profile_badge")
                    ) {
                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = RoundedCornerShape(22.dp),
                            color = NexusBlueContainer,
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, NexusBlue)
                        ) {
                            if (profile.hasCustomAvatar) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(profile.profileImageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "${profile.fullName} Profile Picture",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(22.dp))
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = profile.avatarInitials,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = NexusBlue,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 24.sp
                                    )
                                }
                            }
                        }

                        // Verified Status Pill
                        Surface(
                            shape = CircleShape,
                            color = NexusEmerald,
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.BottomEnd)
                                .border(2.dp, NexusSurface, CircleShape)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Verified",
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }

                        // Camera Icon Change Badge
                        Surface(
                            shape = CircleShape,
                            color = NexusDarkNavy,
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.TopEnd)
                                .border(2.dp, NexusSurface, CircleShape)
                                .testTag("change_photo_badge")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change Profile Picture",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }

                    Column {
                        Text(
                            text = profile.fullName,
                            style = MaterialTheme.typography.titleLarge,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = profile.roleTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = profile.organization,
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bio & Tier Badge
            Surface(
                color = NexusSurfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
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
                                text = profile.tier,
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusOnBlueContainer,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = NexusBlue,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = profile.country,
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusTextSecondary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = profile.bio,
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileMetricsRow(
    securityScore: Int,
    inquiryCount: Int,
    favoriteCount: Int,
    memberSince: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MetricCard(
            label = "SECURITY",
            value = "$securityScore%",
            subtext = "2FA Active",
            accentColor = NexusEmerald,
            accentBg = NexusEmeraldBg,
            modifier = Modifier.weight(1f)
        )
        MetricCard(
            label = "BRIEFINGS",
            value = "$inquiryCount",
            subtext = "Dispatched",
            accentColor = NexusBlue,
            accentBg = NexusBlueContainer,
            modifier = Modifier.weight(1f)
        )
        MetricCard(
            label = "SAVED",
            value = "$favoriteCount",
            subtext = "Architectures",
            accentColor = NexusOrange,
            accentBg = NexusOrangeBg,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    subtext: String,
    accentColor: Color,
    accentBg: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = NexusSurface
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = accentBg,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = NexusDarkNavy,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun AccountSectionCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(1.dp, NexusBorder, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NexusSurface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextSecondary,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NexusSurfaceVariant, RoundedCornerShape(12.dp))
            .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NexusBlue,
                modifier = Modifier.size(18.dp)
            )
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    fontSize = 10.sp
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (actionLabel != null && onAction != null) {
            TextButton(
                onClick = onAction,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SettingToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(NexusSurfaceVariant, RoundedCornerShape(14.dp))
            .border(1.dp, NexusBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                modifier = Modifier.size(34.dp),
                shape = RoundedCornerShape(8.dp),
                color = NexusBlueContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = NexusBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = NexusDarkNavy,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextMuted,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NexusBlue,
                uncheckedThumbColor = NexusTextMuted,
                uncheckedTrackColor = NexusBorder
            )
        )
    }
}

@Composable
fun ActionNavRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String,
    iconTint: Color = NexusBlue,
    titleColor: Color = NexusDarkNavy,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = NexusSurfaceVariant,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(14.dp))
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    modifier = Modifier.size(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = if (iconTint == NexusRuby) NexusRuby.copy(alpha = 0.1f) else NexusBlueContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = titleColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = NexusTextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileModalSheet(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (name: String, email: String, org: String, role: String, phone: String, country: String, bio: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var fullName by remember { mutableStateOf(profile.fullName) }
    var email by remember { mutableStateOf(profile.email) }
    var organization by remember { mutableStateOf(profile.organization) }
    var roleTitle by remember { mutableStateOf(profile.roleTitle) }
    var phone by remember { mutableStateOf(profile.phone) }
    var country by remember { mutableStateOf(profile.country) }
    var bio by remember { mutableStateOf(profile.bio) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NexusSurface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Edit Profile & Identity",
                        style = MaterialTheme.typography.titleLarge,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Update public credentials across Nexus Studio",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NexusTextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_name"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexusBlue,
                        unfocusedBorderColor = NexusBorder
                    )
                )

                OutlinedTextField(
                    value = roleTitle,
                    onValueChange = { roleTitle = it },
                    label = { Text("Role / Professional Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_role"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexusBlue,
                        unfocusedBorderColor = NexusBorder
                    )
                )

                OutlinedTextField(
                    value = organization,
                    onValueChange = { organization = it },
                    label = { Text("Organization / Company") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_org"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexusBlue,
                        unfocusedBorderColor = NexusBorder
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("input_edit_phone"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexusBlue,
                            unfocusedBorderColor = NexusBorder
                        )
                    )

                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("Country") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("input_edit_country"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexusBlue,
                            unfocusedBorderColor = NexusBorder
                        )
                    )
                }

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Professional Bio") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_bio"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexusBlue,
                        unfocusedBorderColor = NexusBorder
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onSave(fullName, email, organization, roleTitle, phone, country, bio)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("save_profile_button")
            ) {
                Text("Save Profile Changes", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

data class AvatarPreset(val title: String, val role: String, val url: String)

val PRESET_AVATARS = listOf(
    AvatarPreset(
        title = "Executive Lead",
        role = "Principal Architect",
        url = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80"
    ),
    AvatarPreset(
        title = "Engineering Director",
        role = "Distributed Systems",
        url = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80"
    ),
    AvatarPreset(
        title = "Web3 Security Lead",
        role = "Zero-Knowledge & EVM",
        url = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=400&auto=format&fit=crop&q=80"
    ),
    AvatarPreset(
        title = "Cloud Infrastructure",
        role = "Latency & DevOps",
        url = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&auto=format&fit=crop&q=80"
    ),
    AvatarPreset(
        title = "Product Designer",
        role = "Minimalist UI/UX",
        url = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePhotoPickerSheet(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onPickGallery: () -> Unit,
    onSelectPreset: (String) -> Unit,
    onSaveCustomUrl: (String) -> Unit,
    onRemovePhoto: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var customUrlInput by remember { mutableStateOf(profile.profileImageUrl ?: "") }
    var selectedPresetUrl by remember { mutableStateOf(profile.profileImageUrl) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NexusSurface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("profile_photo_picker_sheet")
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
                    Text(
                        text = "Profile Picture",
                        style = MaterialTheme.typography.titleLarge,
                        color = NexusDarkNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Customize your verified architectural persona",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NexusTextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Current Avatar Preview Card
            Surface(
                color = NexusSurfaceVariant,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(76.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = NexusBlueContainer,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, NexusBlue)
                    ) {
                        if (!selectedPresetUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(selectedPresetUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Avatar Preview",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp))
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = profile.avatarInitials,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = NexusBlue,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 26.sp
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile.fullName,
                            style = MaterialTheme.typography.titleMedium,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (profile.hasCustomAvatar) "Custom avatar loaded" else "Monogram initials active",
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Persisted securely in local DataStore",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Pick from Device Gallery
            Text(
                text = "CHOOSE PHOTO SOURCE",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onPickGallery,
                colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("picker_gallery_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Upload from Photo Gallery / Files",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Preset Executive Avatars
            Text(
                text = "OR CHOOSE EXECUTIVE PRESET",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PRESET_AVATARS.forEach { preset ->
                    val isSelected = selectedPresetUrl == preset.url
                    Surface(
                        modifier = Modifier
                            .width(100.dp)
                            .clickable {
                                selectedPresetUrl = preset.url
                                onSelectPreset(preset.url)
                            }
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) NexusBlue else NexusBorder,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .testTag("preset_avatar_${preset.title.lowercase().replace(" ", "_")}"),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) NexusBlueContainer else NexusSurfaceVariant
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                modifier = Modifier.size(54.dp),
                                shape = CircleShape,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) NexusBlue else NexusBorder
                                )
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(preset.url)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = preset.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = preset.title,
                                style = MaterialTheme.typography.labelSmall,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                            Text(
                                text = preset.role,
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextMuted,
                                fontSize = 8.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Custom Image URL
            Text(
                text = "OR ENTER IMAGE WEB URL",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customUrlInput,
                    onValueChange = { customUrlInput = it },
                    placeholder = { Text("https://example.com/photo.jpg", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_custom_avatar_url"),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Link, contentDescription = null, tint = NexusBlue, modifier = Modifier.size(16.dp))
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexusBlue,
                        unfocusedBorderColor = NexusBorder
                    )
                )

                Button(
                    onClick = {
                        if (customUrlInput.isNotBlank()) {
                            onSaveCustomUrl(customUrlInput.trim())
                        }
                    },
                    enabled = customUrlInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(52.dp)
                        .testTag("apply_custom_avatar_url")
                ) {
                    Text("Apply", fontWeight = FontWeight.Bold)
                }
            }

            // 4. Remove Avatar Option
            if (profile.hasCustomAvatar) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onRemovePhoto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("remove_avatar_button"),
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusRuby.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = NexusRuby,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Remove Custom Photo (Reset to Initials)",
                        style = MaterialTheme.typography.labelMedium,
                        color = NexusRuby,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Change Master Password",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NexusDarkNavy
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Ensure your password is at least 12 characters with alphanumeric and symbolic entropy.",
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextSecondary
                )
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Secure Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm New Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White)
            ) {
                Text("Update Password", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = NexusTextSecondary)
            }
        },
        containerColor = NexusSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ExportDataDialog(
    jsonData: String,
    onDismiss: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Personal Data Export (GDPR)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NexusDarkNavy
            )
        },
        text = {
            Column {
                Text(
                    text = "A machine-readable snapshot of all personal records and telemetry under your profile:",
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = NexusSurfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .border(1.dp, NexusBorder, RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = jsonData,
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextPrimary,
                        fontSize = 11.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onCopy,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy JSON", fontSize = 12.sp)
                }
                Button(
                    onClick = onShare,
                    colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Share", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = NexusTextSecondary)
            }
        },
        containerColor = NexusSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ActiveSessionsDialog(
    onDismiss: () -> Unit,
    onRevokeOther: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Authorized Devices & Sessions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NexusDarkNavy
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SessionDeviceCard(
                    deviceName = "Android App • Pixel 9 Pro",
                    location = "Amsterdam, Netherlands",
                    status = "Current Device • Active Now",
                    isCurrent = true
                )
                SessionDeviceCard(
                    deviceName = "Web Portal • MacBook Pro M3",
                    location = "Utrecht, Netherlands",
                    status = "Last active 3 hours ago",
                    isCurrent = false
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onRevokeOther,
                colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White)
            ) {
                Text("Terminate Remote Sessions", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = NexusTextSecondary)
            }
        },
        containerColor = NexusSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun SessionDeviceCard(
    deviceName: String,
    location: String,
    status: String,
    isCurrent: Boolean
) {
    Surface(
        color = NexusSurfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = deviceName,
                    style = MaterialTheme.typography.titleSmall,
                    color = NexusDarkNavy,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCurrent) NexusEmerald else NexusTextMuted,
                    fontSize = 10.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                )
            }
            if (isCurrent) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = NexusEmerald,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
