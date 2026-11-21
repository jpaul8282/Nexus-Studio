package com.example.model

data class CmsProject(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val longOverview: String = "",
    val technologies: List<String>,
    val link: String,
    val image: String,
    val color: String,
    val code: String,
    val client: String = "Global Enterprise",
    val latency: String = "< 85ms",
    val uptime: String = "99.99%",
    val security: String = "AES-256 / SSL",
    val platformType: String = "Progressive Edge",
    val architectureSummary: String = "Decoupled edge-computing orchestration with atomic CI/CD deployments and multi-region state caching.",
    val keyFeatures: List<String> = emptyList()
)

data class StudioService(
    val id: String,
    val title: String,
    val tagline: String,
    val description: String,
    val deliverables: List<String>,
    val iconName: String = "code",
    val accentHex: String = "#E2FF45"
)

data class StudioInquiry(
    val id: String,
    val clientName: String,
    val email: String,
    val serviceTitle: String,
    val budgetRange: String,
    val timeline: String,
    val projectNotes: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUBMITTED"
)

data class StudioMetric(
    val label: String,
    val value: String,
    val change: String,
    val detail: String
)
