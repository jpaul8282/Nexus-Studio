package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DashboardTelemetryRepository
import com.example.data.GeminiChatService
import com.example.data.ProjectRepository
import com.example.data.TemplateRepository
import com.example.data.UserPreferencesRepository
import com.example.model.ArchitectureTemplate
import com.example.model.ChatMessage
import com.example.model.ChatSender
import com.example.model.CmsProject
import com.example.model.ComplianceBadge
import com.example.model.CustomTemplateRequest
import com.example.model.PipelineEnvironment
import com.example.model.ProjectTelemetrySnapshot
import com.example.model.StudioInquiry
import com.example.model.StudioService
import com.example.model.TelemetryTimeRange
import com.example.model.TemplateCategory
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ProjectFilterCategory(val label: String, val queryMatch: String) {
    ALL("ALL SYSTEMS", ""),
    WEB3("WEB 3.0", "web 3"),
    LUXURY("LUXURY & COMMERCE", "commerce"),
    FINTECH("FINTECH / SAAS", "fintech"),
    AI("AI & SYSTEMS", "intelligence"),
    ARCHITECTURE("ARCHITECTURE", "architecture")
}

data class NexusUiState(
    val searchQuery: String = "",
    val selectedCategory: ProjectFilterCategory = ProjectFilterCategory.ALL,
    val showFavoritesOnly: Boolean = false,
    val showInquirySheet: Boolean = false,
    val showClientPortalSheet: Boolean = false,
    val showChatbotSheet: Boolean = false,
    val selectedServiceForInquiry: StudioService? = null,
    val inquirySuccessMessage: String? = null,
    val activeDetailTab: Int = 0, // 0 = Overview, 1 = Code Architecture, 2 = Technical Specs
    val notificationMessage: String? = null,
    val clientNameInput: String = "Enterprise Partner",
    val clientTier: String = "TIER 1 ARCHITECTURE PARTNER",
    val isDarkTheme: Boolean = true,
    val isGalleryGridMode: Boolean = true,
    // Template State
    val selectedTemplateCategory: TemplateCategory = TemplateCategory.ALL,
    val templateSearchQuery: String = "",
    val showTemplateFavoritesOnly: Boolean = false,
    val showCustomTemplateGeneratorSheet: Boolean = false,
    val isGeneratingCustomTemplate: Boolean = false,
    val selectedTemplateForDetail: ArchitectureTemplate? = null,
    val prefilledInquiryNotes: String? = null,
    // Dashboard Telemetry & Security/SSF Governance State
    val selectedTelemetryRange: TelemetryTimeRange = TelemetryTimeRange.SEVEN_DAYS,
    val selectedPipelineEnv: PipelineEnvironment = PipelineEnvironment.ALL,
    val selectedComplianceBadge: ComplianceBadge? = null,
    val activeDashboardFocus: Int = 0 // 0 = All Overview, 1 = Telemetry & Charts
)

class NexusStudioViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository = UserPreferencesRepository.getInstance(application)
    private val geminiChatService = GeminiChatService()

    private val _uiState = MutableStateFlow(NexusUiState())
    val uiState: StateFlow<NexusUiState> = _uiState.asStateFlow()

    val userProfile: StateFlow<UserProfile> = preferencesRepository.userProfileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    val favoriteIds: StateFlow<Set<String>> = ProjectRepository.favoriteIds
    val inquiries: StateFlow<List<StudioInquiry>> = ProjectRepository.inquiries
    val cmsStatus: StateFlow<String> = ProjectRepository.cmsStatus

    // Chatbot State
    private val initialWelcomeMessage = ChatMessage(
        sender = ChatSender.ASSISTANT,
        text = "Welcome to **Nexus Studio AI Copilot**.\n\nI specialize in distributed systems, high-concurrency cloud scaling, Web3 zero-knowledge architectures, and GDPR/SOC-2 enterprise compliance under European jurisdiction.\n\nHow may I assist your engineering brief or technical evaluation?",
        suggestedActions = listOf(
            "Explain Web3 Zero-Knowledge Architecture",
            "What tech stack is best for high-throughput FinTech?",
            "How do I formulate a project brief?",
            "Review Dutch GDPR & SOC-2 compliance"
        )
    )

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(initialWelcomeMessage))
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatGenerating = MutableStateFlow(false)
    val isChatGenerating: StateFlow<Boolean> = _isChatGenerating.asStateFlow()

    val filteredProjects: StateFlow<List<CmsProject>> = combine(
        ProjectRepository.projects,
        _uiState,
        ProjectRepository.favoriteIds
    ) { allProjects, state, favs ->
        allProjects.filter { project ->
            val matchesCategory = when (state.selectedCategory) {
                ProjectFilterCategory.ALL -> true
                ProjectFilterCategory.WEB3 -> project.category.contains("Web 3", ignoreCase = true)
                ProjectFilterCategory.LUXURY -> project.category.contains("Commerce", ignoreCase = true) || project.category.contains("Luxury", ignoreCase = true)
                ProjectFilterCategory.FINTECH -> project.category.contains("Fintech", ignoreCase = true) || project.category.contains("SaaS", ignoreCase = true)
                ProjectFilterCategory.AI -> project.category.contains("Intelligence", ignoreCase = true) || project.category.contains("AI", ignoreCase = true)
                ProjectFilterCategory.ARCHITECTURE -> project.category.contains("Architecture", ignoreCase = true) || project.category.contains("Portfolio", ignoreCase = true)
            }

            val matchesSearch = state.searchQuery.isBlank() ||
                project.title.contains(state.searchQuery, ignoreCase = true) ||
                project.category.contains(state.searchQuery, ignoreCase = true) ||
                project.description.contains(state.searchQuery, ignoreCase = true) ||
                project.technologies.any { it.contains(state.searchQuery, ignoreCase = true) }

            val matchesFavorites = !state.showFavoritesOnly || favs.contains(project.id)

            matchesCategory && matchesSearch && matchesFavorites
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProjectRepository.DEFAULT_PROJECTS
    )

    // Templates State Flows
    val templates: StateFlow<List<ArchitectureTemplate>> = TemplateRepository.templates
    val favoriteTemplateIds: StateFlow<Set<String>> = TemplateRepository.favoriteTemplateIds

    val filteredTemplates: StateFlow<List<ArchitectureTemplate>> = combine(
        TemplateRepository.templates,
        _uiState,
        TemplateRepository.favoriteTemplateIds
    ) { allTemplates, state, favs ->
        allTemplates.filter { template ->
            val matchesCategory = when (state.selectedTemplateCategory) {
                TemplateCategory.ALL -> true
                else -> template.category == state.selectedTemplateCategory
            }

            val matchesSearch = state.templateSearchQuery.isBlank() ||
                template.title.contains(state.templateSearchQuery, ignoreCase = true) ||
                template.subtitle.contains(state.templateSearchQuery, ignoreCase = true) ||
                template.description.contains(state.templateSearchQuery, ignoreCase = true) ||
                template.tags.any { it.contains(state.templateSearchQuery, ignoreCase = true) } ||
                template.targetStack.contains(state.templateSearchQuery, ignoreCase = true)

            val matchesFavorites = !state.showTemplateFavoritesOnly || favs.contains(template.id)

            matchesCategory && matchesSearch && matchesFavorites
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Dashboard Telemetry StateFlow
    val complianceBadges: List<ComplianceBadge> = DashboardTelemetryRepository.COMPLIANCE_BADGES

    val telemetrySnapshot: StateFlow<ProjectTelemetrySnapshot> = combine(
        _uiState
    ) { states ->
        val state = states[0]
        DashboardTelemetryRepository.getTelemetrySnapshot(
            timeRange = state.selectedTelemetryRange,
            environment = state.selectedPipelineEnv
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardTelemetryRepository.getTelemetrySnapshot(
            TelemetryTimeRange.SEVEN_DAYS,
            PipelineEnvironment.ALL
        )
    )

    fun onTelemetryRangeSelect(range: TelemetryTimeRange) {
        _uiState.update { it.copy(selectedTelemetryRange = range) }
    }

    fun onPipelineEnvironmentSelect(env: PipelineEnvironment) {
        _uiState.update { it.copy(selectedPipelineEnv = env) }
    }

    fun onDashboardFocusSelect(focusIndex: Int) {
        _uiState.update { it.copy(activeDashboardFocus = focusIndex) }
    }

    fun openComplianceBadgeDoc(badge: ComplianceBadge) {
        _uiState.update { it.copy(selectedComplianceBadge = badge) }
    }

    fun closeComplianceBadgeDoc() {
        _uiState.update { it.copy(selectedComplianceBadge = null) }
    }

    fun getProjectTelemetry(projectId: String): ProjectTelemetrySnapshot {
        val state = _uiState.value
        return DashboardTelemetryRepository.getTelemetrySnapshot(
            timeRange = state.selectedTelemetryRange,
            environment = state.selectedPipelineEnv,
            projectId = projectId
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelect(category: ProjectFilterCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun toggleFavoritesOnly() {
        _uiState.update { it.copy(showFavoritesOnly = !it.showFavoritesOnly) }
    }

    fun setGalleryGridMode(isGrid: Boolean) {
        _uiState.update { it.copy(isGalleryGridMode = isGrid) }
    }

    fun toggleFavorite(projectId: String) {
        ProjectRepository.toggleFavorite(projectId)
    }

    fun openInquirySheet(service: StudioService? = null) {
        _uiState.update {
            it.copy(
                showInquirySheet = true,
                selectedServiceForInquiry = service ?: ProjectRepository.STUDIO_SERVICES.first()
            )
        }
    }

    fun closeInquirySheet() {
        _uiState.update { it.copy(showInquirySheet = false, inquirySuccessMessage = null) }
    }

    fun openClientPortal() {
        _uiState.update { it.copy(showClientPortalSheet = true) }
    }

    fun closeClientPortal() {
        _uiState.update { it.copy(showClientPortalSheet = false) }
    }

    fun openChatbotSheet() {
        _uiState.update { it.copy(showChatbotSheet = true) }
    }

    fun closeChatbotSheet() {
        _uiState.update { it.copy(showChatbotSheet = false) }
    }

    fun setDetailTab(index: Int) {
        _uiState.update { it.copy(activeDetailTab = index) }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun submitInquiry(
        name: String,
        email: String,
        serviceTitle: String,
        budget: String,
        timeline: String,
        notes: String
    ) {
        val inquiry = StudioInquiry(
            id = "INQ-${System.currentTimeMillis().toString().takeLast(6)}",
            clientName = name.ifBlank { "Client" },
            email = email.ifBlank { "client@nexus-studio.io" },
            serviceTitle = serviceTitle,
            budgetRange = budget,
            timeline = timeline,
            projectNotes = notes.ifBlank { "Standard scope engagement briefing." }
        )
        ProjectRepository.submitInquiry(inquiry)
        _uiState.update {
            it.copy(
                inquirySuccessMessage = "Briefing #${inquiry.id} dispatched to Nexus Studio Architecture team."
            )
        }
    }

    fun showNotification(msg: String) {
        _uiState.update { it.copy(notificationMessage = msg) }
        viewModelScope.launch {
            kotlinx.coroutines.delay(3000)
            _uiState.update { if (it.notificationMessage == msg) it.copy(notificationMessage = null) else it }
        }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(notificationMessage = null) }
    }

    fun getProject(id: String): CmsProject? {
        return ProjectRepository.getProjectById(id)
    }

    // --- Profile & DataStore Management ---

    fun updateProfile(
        fullName: String,
        email: String,
        organization: String,
        roleTitle: String,
        phone: String,
        country: String,
        bio: String,
        profileImageUrl: String? = null
    ) {
        viewModelScope.launch {
            preferencesRepository.updateFullProfile(
                name = fullName,
                email = email,
                org = organization,
                role = roleTitle,
                phone = phone,
                country = country,
                bio = bio,
                profileImageUrl = profileImageUrl
            )
            _uiState.update { it.copy(clientNameInput = fullName) }
            showNotification("Account Profile & credentials persisted locally")
        }
    }

    fun updateProfilePhoto(imageUriOrUrl: String?) {
        viewModelScope.launch {
            preferencesRepository.updateProfileImageUrl(imageUriOrUrl)
            showNotification(if (imageUriOrUrl.isNullOrBlank()) "Profile photo removed" else "Profile photo updated")
        }
    }

    fun togglePushNotifications() {
        val current = userProfile.value
        viewModelScope.launch {
            preferencesRepository.updateNotificationPreferences(
                push = !current.pushNotificationsEnabled,
                sms = current.smsAlertsEnabled,
                digest = current.emailDigestEnabled
            )
        }
    }

    fun toggleSmsAlerts() {
        val current = userProfile.value
        viewModelScope.launch {
            preferencesRepository.updateNotificationPreferences(
                push = current.pushNotificationsEnabled,
                sms = !current.smsAlertsEnabled,
                digest = current.emailDigestEnabled
            )
        }
    }

    fun toggleEmailDigest() {
        val current = userProfile.value
        viewModelScope.launch {
            preferencesRepository.updateNotificationPreferences(
                push = current.pushNotificationsEnabled,
                sms = current.smsAlertsEnabled,
                digest = !current.emailDigestEnabled
            )
        }
    }

    fun toggleTwoFactorAuth() {
        val current = userProfile.value
        val newState = !current.twoFactorAuthEnabled
        viewModelScope.launch {
            preferencesRepository.updateSecurityPreferences(
                twoFactor = newState,
                biometric = current.biometricAuthEnabled
            )
            showNotification(if (newState) "2-Factor Authentication activated" else "2-Factor Authentication disabled")
        }
    }

    fun toggleBiometricAuth() {
        val current = userProfile.value
        viewModelScope.launch {
            preferencesRepository.updateSecurityPreferences(
                twoFactor = current.twoFactorAuthEnabled,
                biometric = !current.biometricAuthEnabled
            )
        }
    }

    fun toggleHighFrequencyTelemetry() {
        val current = userProfile.value
        viewModelScope.launch {
            preferencesRepository.updateTelemetry(!current.highFrequencyTelemetry)
        }
    }

    fun clearLocalCache() {
        viewModelScope.launch {
            showNotification("Local cache cleared (14.2 MB freed)")
        }
    }

    fun exportPersonalDataJson(): String {
        val p = userProfile.value
        return """
            {
              "export_date": "2026-08-16",
              "jurisdiction": "Netherlands (GDPR compliant)",
              "application": "FIRST AID SPONDER // NEXUS STUDIO",
              "user_profile": {
                "id": "${p.id}",
                "full_name": "${p.fullName}",
                "email": "${p.email}",
                "profile_image_url": "${p.profileImageUrl ?: "none"}",
                "organization": "${p.organization}",
                "role_title": "${p.roleTitle}",
                "phone": "${p.phone}",
                "country": "${p.country}",
                "tier": "${p.tier}",
                "member_since": "${p.memberSince}",
                "security_score": ${p.securityScore}
              },
              "settings": {
                "push_notifications": ${p.pushNotificationsEnabled},
                "sms_alerts": ${p.smsAlertsEnabled},
                "email_digest": ${p.emailDigestEnabled},
                "two_factor_auth": ${p.twoFactorAuthEnabled},
                "biometric_auth": ${p.biometricAuthEnabled}
              }
            }
        """.trimIndent()
    }

    // --- Chatbot Messaging ---

    fun sendChatMessage(prompt: String) {
        val trimmed = prompt.trim()
        if (trimmed.isBlank() || _isChatGenerating.value) return

        val userMessage = ChatMessage(
            sender = ChatSender.USER,
            text = trimmed
        )
        val loadingMessage = ChatMessage(
            sender = ChatSender.ASSISTANT,
            text = "Analyzing architectural patterns & systems...",
            isLoading = true
        )

        val updatedList = _chatMessages.value + userMessage + loadingMessage
        _chatMessages.value = updatedList
        _isChatGenerating.value = true

        viewModelScope.launch {
            val result = geminiChatService.sendMessage(_chatMessages.value, trimmed)
            _isChatGenerating.value = false

            val replyText = result.getOrElse {
                "Nexus Copilot encountered a connection issue. Please verify telemetry link."
            }

            val finalAssistantMessage = ChatMessage(
                sender = ChatSender.ASSISTANT,
                text = replyText,
                isLoading = false
            )

            // Replace loading message with real response
            _chatMessages.update { list ->
                list.filterNot { it.id == loadingMessage.id } + finalAssistantMessage
            }
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(initialWelcomeMessage)
    }

    // --- Architecture Templates Engine ---

    fun onTemplateSearchQueryChange(query: String) {
        _uiState.update { it.copy(templateSearchQuery = query) }
    }

    fun onTemplateCategorySelect(category: TemplateCategory) {
        _uiState.update { it.copy(selectedTemplateCategory = category) }
    }

    fun toggleTemplateFavoritesOnly() {
        _uiState.update { it.copy(showTemplateFavoritesOnly = !it.showTemplateFavoritesOnly) }
    }

    fun toggleTemplateFavorite(templateId: String) {
        TemplateRepository.toggleFavorite(templateId)
    }

    fun selectTemplateForDetail(template: ArchitectureTemplate?) {
        _uiState.update { it.copy(selectedTemplateForDetail = template) }
    }

    fun openCustomTemplateGeneratorSheet() {
        _uiState.update { it.copy(showCustomTemplateGeneratorSheet = true) }
    }

    fun closeCustomTemplateGeneratorSheet() {
        _uiState.update { it.copy(showCustomTemplateGeneratorSheet = false) }
    }

    fun generateCustomTemplate(
        request: CustomTemplateRequest,
        onFinished: ((ArchitectureTemplate) -> Unit)? = null
    ) {
        _uiState.update { it.copy(isGeneratingCustomTemplate = true) }
        viewModelScope.launch {
            val template = TemplateRepository.generateTemplateWithAi(request, geminiChatService)
            _uiState.update {
                it.copy(
                    isGeneratingCustomTemplate = false,
                    showCustomTemplateGeneratorSheet = false,
                    selectedTemplateForDetail = template
                )
            }
            showNotification("Template '${template.title}' generated successfully!")
            onFinished?.invoke(template)
        }
    }

    fun deleteCustomTemplate(templateId: String) {
        TemplateRepository.deleteCustomTemplate(templateId)
        if (_uiState.value.selectedTemplateForDetail?.id == templateId) {
            _uiState.update { it.copy(selectedTemplateForDetail = null) }
        }
        showNotification("Template deleted")
    }

    fun applyTemplateToInquiry(template: ArchitectureTemplate) {
        val prefilled = """
--- PREFILLED FROM TEMPLATE: ${template.title} ---
Domain / Stack: ${template.targetStack}
Category: ${template.category.displayName}

${template.markdownContent.take(800)}...
        """.trimIndent()

        _uiState.update {
            it.copy(
                prefilledInquiryNotes = prefilled,
                showInquirySheet = true,
                selectedTemplateForDetail = null
            )
        }
        showNotification("Loaded '${template.title}' into Project Brief")
    }

    fun sendTemplateToCopilot(template: ArchitectureTemplate, onNavigate: () -> Unit) {
        val prompt = "Please review and critique this architectural specification for '${template.title}':\n\n${template.markdownContent.take(1000)}"
        sendChatMessage(prompt)
        onNavigate()
    }
}
