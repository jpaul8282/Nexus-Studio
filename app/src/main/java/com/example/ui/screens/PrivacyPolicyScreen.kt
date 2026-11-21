package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

data class PolicySection(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val content: String,
    val bulletPoints: List<String> = emptyList()
)

val PRIVACY_POLICY_SECTIONS = listOf(
    PolicySection(
        id = "overview",
        title = "1. Interpretation and Definitions",
        subtitle = "Core terms, legal definitions, and scope",
        icon = Icons.Default.Gavel,
        content = "This Privacy Policy describes Our policies and procedures on the collection, use, and disclosure of Your information when You use the Service and informs You about Your privacy rights and legal protections.\n\n" +
                "The words whose initial letters are capitalized have meanings defined under the following conditions. The following definitions shall have the same meaning regardless of whether they appear in singular or plural:\n\n" +
                "• Account: A unique account created for You to access Our Service or parts of Our Service.\n" +
                "• Application / Service: Refers to FIRST AID SPONDER, the software platform provided by the Company.\n" +
                "• Company: Refers to FIRST AID SPONDER / Oistars Entertainment (Country: Netherlands).\n" +
                "• Country / State: Netherlands.\n" +
                "• Personal Data: Any information relating to an identified or identifiable natural person.\n" +
                "• Device: Any device accessing the Service, including smartphones, tablets, or computers."
    ),
    PolicySection(
        id = "data_collected",
        title = "2. Collecting & Types of Data",
        subtitle = "Personal data, usage telemetry & device identifiers",
        icon = Icons.Default.Security,
        content = "While using Our Service, We may ask You to provide Us with certain personally identifiable information that can be used to contact or identify You.\n\n" +
                "Personal Data may include, but is not limited to:\n" +
                "• Email address\n" +
                "• First name and last name\n" +
                "• Phone number\n" +
                "• Address, State, Province, ZIP/Postal code, City\n\n" +
                "Usage Data is collected automatically when using the Service. It includes IP addresses, browser types, operating systems, session duration, unique device IDs, and diagnostic telemetry.",
        bulletPoints = listOf(
            "Contact info collected strictly for authentication and communication",
            "Hardware identifiers sanitized for diagnostic telemetry",
            "Encrypted at rest with industry standard AES-256 protocols"
        )
    ),
    PolicySection(
        id = "location_info",
        title = "3. Information Collected While Using Application",
        subtitle = "Device permissions and location services",
        icon = Icons.Default.Info,
        content = "While using Our Application, in order to provide specialized features of Our Application, We may collect, with Your prior permission:\n\n" +
                "• Information regarding Your location\n\n" +
                "We use this information to provide features of Our Service and to improve and customize Our Service. The information may be uploaded to the Company's servers and/or a Service Provider's server or it may be simply stored on Your device.\n\n" +
                "You can enable or disable access to location permissions at any time through Your Device settings.",
        bulletPoints = listOf(
            "Location permissions require explicit runtime opt-in",
            "Can be revoked at any time via Android App Settings",
            "Used solely for contextual service delivery and safety response"
        )
    ),
    PolicySection(
        id = "use_of_data",
        title = "4. Use of Your Personal Data",
        subtitle = "Purposes of processing, contract execution & notifications",
        icon = Icons.Default.VerifiedUser,
        content = "The Company may use Personal Data for the following purposes:\n\n" +
                "• To provide and maintain Our Service, including usage monitoring.\n" +
                "• To manage Your Account and registration.\n" +
                "• For the performance of a contract, procurement, or service delivery.\n" +
                "• To contact You via email, SMS, push notifications, or equivalent electronic channels regarding updates, security alerts, and service notices.\n" +
                "• To provide marketing offers (where prior consent is granted or permitted under applicable law, with one-click unsubscribe anytime).\n" +
                "• To manage and resolve inquiries, support tickets, and business requests.\n" +
                "• For business transfers, data analysis, and service improvements."
    ),
    PolicySection(
        id = "sms_notice",
        title = "5. Text Messages (SMS) Privacy Notice",
        subtitle = "Strict no-sharing commitment for mobile numbers",
        icon = Icons.Default.Shield,
        content = "You have the option to receive text (SMS) messages from Us for customer care, account notifications, verification passcodes (OTP), and security alerts.\n\n" +
                "IMPORTANT: No mobile information will be shared with or sold to third parties or affiliates for marketing or promotional purposes. The phone numbers and consent records We collect for texting are never shared with anyone for any purpose, except the Service Providers that technically have to handle them to deliver the texts.\n\n" +
                "• Reply STOP to opt-out at any time.\n" +
                "• Reply HELP for support.\n" +
                "• Message & data rates may apply. Messaging frequency may vary.",
        bulletPoints = listOf(
            "100% strict policy: Phone numbers are never sold or marketed",
            "Consent to SMS is NOT a mandatory condition of service purchase",
            "Instant opt-out available by replying STOP"
        )
    ),
    PolicySection(
        id = "retention",
        title = "6. Retention of Your Personal Data",
        subtitle = "Data retention schedules & deletion procedures",
        icon = Icons.Default.Lock,
        content = "The Company will retain Your Personal Data only for as long as is necessary for the purposes set out in this Privacy Policy:\n\n" +
                "• User Accounts: Retained for the duration of the account relationship plus up to 24 months post-closure to handle inquiries or disputes.\n" +
                "• Customer Support Data & Tickets: Retained up to 24 months from ticket closure.\n" +
                "• Usage Data & Server Logs: Retained up to 24 months for security audits and telemetry optimization.\n\n" +
                "When retention periods expire, data is securely erased or irreversibly anonymized for statistical reporting."
    ),
    PolicySection(
        id = "rights_and_deletion",
        title = "7. Delete Your Personal Data & GDPR Rights",
        subtitle = "Right of access, rectification, erasure and portability",
        icon = Icons.Default.Gavel,
        content = "You have the right to delete or request that We assist in deleting the Personal Data that We have collected about You.\n\n" +
                "You may update, amend, or delete Your information at any time by accessing Your account settings or by emailing Us directly. We will process verified erasure requests in accordance with GDPR and applicable global data protection regulations."
    ),
    PolicySection(
        id = "service_providers",
        title = "8. Third-Party Service Providers",
        subtitle = "Google Places & infrastructure sub-processors",
        icon = Icons.Default.Security,
        content = "We may use third-party Service Providers to facilitate and optimize Our Service:\n\n" +
                "• Google Places: A service operated by Google returning place and map information. Data gathered by Google Places is handled in accordance with Google's Privacy Policy (https://www.google.com/intl/en/policies/privacy/).\n\n" +
                "All Service Providers are contractually bound by confidentiality and data processing agreements."
    ),
    PolicySection(
        id = "minors_and_transfers",
        title = "9. Children's Privacy & International Transfers",
        subtitle = "Protection of minors and cross-border safeguards",
        icon = Icons.Default.Shield,
        content = "The Service is not directed to, and We do not knowingly collect Personal Information from, anyone under the age of 16. If You believe a child under 16 has provided personal data, contact Us immediately for expedited deletion.\n\n" +
                "Your information is processed at Company offices in the Netherlands and secure server infrastructure with appropriate technical and contractual safeguards."
    ),
    PolicySection(
        id = "contact_us",
        title = "10. Contact Us",
        subtitle = "Direct contact channels for privacy inquiries",
        icon = Icons.Default.Email,
        content = "If You have any questions or requests regarding this Privacy Policy, You can contact Us directly:\n\n" +
                "• Email: oistarsentertainment@gmail.com\n" +
                "• Web Profile: https://www.gravatar.com/oistars\n" +
                "• Jurisdiction: Netherlands\n" +
                "• Last Updated: August 16, 2026"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var expandedSectionIds by remember { mutableStateOf(setOf("overview", "sms_notice", "contact_us")) }

    val filteredSections = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            PRIVACY_POLICY_SECTIONS
        } else {
            val query = searchQuery.lowercase()
            PRIVACY_POLICY_SECTIONS.filter {
                it.title.lowercase().contains(query) ||
                        it.subtitle.lowercase().contains(query) ||
                        it.content.lowercase().contains(query) ||
                        it.bulletPoints.any { bp -> bp.lowercase().contains(query) }
            }
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
                            text = "Privacy Policy",
                            style = MaterialTheme.typography.titleMedium,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "FIRST AID SPONDER // NETHERLANDS",
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
                        modifier = Modifier.testTag("privacy_policy_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NexusDarkNavy
                        )
                    }
                },
                actions = {
                    Surface(
                        color = NexusEmeraldBg,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .border(1.dp, NexusEmerald.copy(alpha = 0.3f), RoundedCornerShape(50))
                    ) {
                        Text(
                            text = "GDPR COMPLIANT",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusEmerald,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
                .testTag("privacy_policy_list"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Header Banner
            item(key = "header_banner") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NexusBorder, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NexusBlueContainer)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
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
                                    text = "OFFICIAL LEGAL NOTICE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NexusOnBlueContainer,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Privacy & Data Protection Policy",
                                style = MaterialTheme.typography.headlineSmall,
                                color = NexusDarkNavy,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Last updated: August 16, 2026 • Jurisdiction: Netherlands\nApplication: FIRST AID SPONDER • Company: Oistars Entertainment",
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Policy Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Search privacy clauses, SMS notice, retention...",
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
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
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
                            .testTag("privacy_search_field"),
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

            // 2. Policy Sections
            items(filteredSections, key = { it.id }) { section ->
                val isExpanded = expandedSectionIds.contains(section.id) || searchQuery.isNotBlank()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .border(1.dp, NexusBorder, RoundedCornerShape(16.dp))
                        .testTag("policy_section_${section.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NexusSurface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedSectionIds = if (expandedSectionIds.contains(section.id)) {
                                        expandedSectionIds - section.id
                                    } else {
                                        expandedSectionIds + section.id
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    modifier = Modifier.size(36.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    color = NexusBlueContainer
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = section.icon,
                                            contentDescription = null,
                                            tint = NexusBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = section.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = NexusDarkNavy,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = section.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NexusTextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    expandedSectionIds = if (expandedSectionIds.contains(section.id)) {
                                        expandedSectionIds - section.id
                                    } else {
                                        expandedSectionIds + section.id
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                                    tint = NexusTextSecondary
                                )
                            }
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 14.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(NexusBorder)
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = section.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NexusTextSecondary,
                                    lineHeight = 22.sp
                                )

                                if (section.bulletPoints.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        section.bulletPoints.forEach { point ->
                                            Row(
                                                verticalAlignment = Alignment.Top,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(top = 6.dp)
                                                        .size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(NexusBlue)
                                                )
                                                Text(
                                                    text = point,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = NexusTextPrimary,
                                                    fontWeight = FontWeight.Medium,
                                                    lineHeight = 18.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. Contact & Direct Actions Card
            item(key = "contact_actions") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .border(1.dp, NexusBorder, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NexusSurface)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "DATA PROTECTION OFFICER & INQUIRIES",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Exercise Your Data Subject Rights",
                            style = MaterialTheme.typography.titleMedium,
                            color = NexusDarkNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "To request data deletion, export, or submit inquiries regarding FIRST AID SPONDER's data governance:",
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Email action button
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:oistarsentertainment@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "Privacy & Data Subject Request - FIRST AID SPONDER")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Privacy Email", "oistarsentertainment@gmail.com")
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Email copied to clipboard", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NexusBlue, contentColor = Color.White),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("privacy_contact_email_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Email: oistarsentertainment@gmail.com",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Gravatar / Website Link
                        Surface(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gravatar.com/oistars"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(50),
                            color = NexusSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .border(1.dp, NexusBorder, RoundedCornerShape(50))
                                .testTag("privacy_gravatar_link")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = null,
                                    tint = NexusBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Visit Gravatar Profile (gravatar.com/oistars)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = NexusDarkNavy,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // 4. Footer info spacing
            item(key = "bottom_space") {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
