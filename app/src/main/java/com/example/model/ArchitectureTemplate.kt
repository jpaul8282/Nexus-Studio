package com.example.model

enum class TemplateCategory(val displayName: String, val tagColorHex: String) {
    ALL("All Templates", "#0066FF"),
    ARCHITECTURE_RFC("Architecture RFC", "#7C3AED"),
    SYSTEM_SPEC("System Specification", "#059669"),
    WEB3_ZK("Web3 & Zero-Knowledge", "#F59E0B"),
    FINTECH_LEDGER("FinTech & Ledgers", "#0284C7"),
    GDPR_COMPLIANCE("GDPR & Security Audit", "#DC2626"),
    ANDROID_BLUEPRINT("Android & Compose", "#10B981"),
    AI_PIPELINE("AI Agent & RAG", "#8B5CF6"),
    ENTERPRISE_RFP("Enterprise RFP / SOW", "#EA580C")
}

data class ArchitectureTemplate(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: TemplateCategory,
    val description: String,
    val tags: List<String>,
    val markdownContent: String,
    val keySections: List<String>,
    val recommendedFor: String,
    val targetStack: String,
    val estimatedTime: String = "15-30 min fill",
    val isCustomGenerated: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class CustomTemplateRequest(
    val projectTitle: String,
    val domain: String,
    val category: TemplateCategory,
    val targetPlatform: String,
    val throughputLatencyTarget: String,
    val complianceStandard: String,
    val coreFeatures: String
)
