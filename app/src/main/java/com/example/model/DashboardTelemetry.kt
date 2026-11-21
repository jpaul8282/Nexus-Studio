package com.example.model

enum class TelemetryTimeRange(val label: String, val daysCount: Int) {
    SEVEN_DAYS("7D", 7),
    FOURTEEN_DAYS("14D", 14),
    THIRTY_DAYS("30D", 30)
}

enum class PipelineEnvironment(val label: String) {
    ALL("All Pipelines"),
    PRODUCTION("Production"),
    STAGING("Staging")
}

data class BuildFrequencyPoint(
    val label: String,
    val fullDate: String,
    val automatedBuilds: Int,
    val manualBuilds: Int,
    val avgDurationSec: Int
) {
    val totalBuilds: Int get() = automatedBuilds + manualBuilds
}

data class DeploymentSuccessPoint(
    val label: String,
    val fullDate: String,
    val successRatePct: Float,
    val totalDeployments: Int,
    val rollbacks: Int,
    val p95LatencyMs: Int
)

data class TeamActivityPoint(
    val label: String,
    val fullDate: String,
    val commits: Int,
    val prReviews: Int,
    val securityAudits: Int
) {
    val totalEvents: Int get() = commits + prReviews + securityAudits
}

data class DashboardKpiSummary(
    val totalBuilds: Int,
    val buildsDelta: String,
    val avgSuccessRatePct: Float,
    val successDelta: String,
    val totalTeamEvents: Int,
    val teamDelta: String,
    val meanBuildDurationSec: Int,
    val durationDelta: String
)

data class ProjectTelemetrySnapshot(
    val timeRange: TelemetryTimeRange,
    val environment: PipelineEnvironment,
    val kpiSummary: DashboardKpiSummary,
    val buildFrequency: List<BuildFrequencyPoint>,
    val deploymentSuccess: List<DeploymentSuccessPoint>,
    val teamActivity: List<TeamActivityPoint>
)

data class ComplianceBadge(
    val id: String,
    val category: String,
    val title: String,
    val statusText: String,
    val scoreOrLevel: String,
    val docTitle: String,
    val summary: String,
    val highlights: List<String>,
    val referencePath: String
)
