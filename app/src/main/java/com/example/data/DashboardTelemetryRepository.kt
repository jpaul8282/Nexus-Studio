package com.example.data

import com.example.model.BuildFrequencyPoint
import com.example.model.ComplianceBadge
import com.example.model.DashboardKpiSummary
import com.example.model.DeploymentSuccessPoint
import com.example.model.PipelineEnvironment
import com.example.model.ProjectTelemetrySnapshot
import com.example.model.TeamActivityPoint
import com.example.model.TelemetryTimeRange
import kotlin.math.abs
import kotlin.math.roundToInt

object DashboardTelemetryRepository {

    val COMPLIANCE_BADGES: List<ComplianceBadge> = listOf(
        ComplianceBadge(
            id = "openssf-best-practices",
            category = "OPENSSF",
            title = "OpenSSF Best Practices",
            statusText = "PASSING",
            scoreOrLevel = "100% Core",
            docTitle = "OpenSSF Best Practices Certification",
            summary = "Aligned with Open Source Security Foundation (OpenSSF) Best Practices criteria covering public version control, automated test suites, vulnerability disclosure, and cryptographic integrity.",
            highlights = listOf(
                "Unique semantic versioning (v1.0.0) & immutable release notes in CHANGELOG.md",
                "Automated unit & Robolectric verification suite on every pull request",
                "Documented bug-reporting & private vulnerability disclosure workflow (SECURITY.md)",
                "Strict TLS 1.3 transport enforcement and zero hardcoded credentials"
            ),
            referencePath = "README.md • SECURITY.md"
        ),
        ComplianceBadge(
            id = "openssf-scorecard",
            category = "SSF SCORECARD",
            title = "OpenSSF Scorecard",
            statusText = "VERIFIED",
            scoreOrLevel = "8.8 / 10",
            docTitle = "OpenSSF Scorecard Supply-Chain Audit",
            summary = "Automated supply-chain security posture tracking dependency pinning, branch protection, static analysis, and signed artifact provenance.",
            highlights = listOf(
                "Gradle Version Catalog (libs.versions.toml) pinned to audited releases",
                "Android Lint & R8 static optimization checks enforced on build",
                "Minimal least-privilege AndroidManifest permissions (INTERNET only)",
                "No dynamic code loading (DCL) or unverified binary blobs"
            ),
            referencePath = "gradle/libs.versions.toml"
        ),
        ComplianceBadge(
            id = "proguard-r8",
            category = "R8 / PROGUARD",
            title = "ProGuard & R8 Hardened",
            statusText = "ENABLED",
            scoreOrLevel = "Release Minified",
            docTitle = "ProGuard & R8 Code Shrinking & Obfuscation",
            summary = "Release builds apply R8 full-mode shrinking, resource stripping, class repackaging, debug log removal, and targeted keep rules for Compose, Moshi, Room, Retrofit, and Firebase.",
            highlights = listOf(
                "isMinifyEnabled = true & isShrinkResources = true on release builds",
                "Aggressive class repackaging & access modification enabled",
                "Verbose android.util.Log.v/d/i calls stripped in production APKs",
                "Explicit keep rules for Moshi Codegen, Retrofit interfaces, Room DAOs & Coroutines"
            ),
            referencePath = "app/proguard-rules.pro"
        ),
        ComplianceBadge(
            id = "security-mitm",
            category = "SECURITY",
            title = "TLS 1.3 & MITM Shield",
            statusText = "ACTIVE",
            scoreOrLevel = "Grade A+",
            docTitle = "Transport & Cryptographic Security Policy",
            summary = "All outbound telemetry and API endpoints enforce HTTPS/TLS with cleartext traffic globally disabled via Android Network Security Config.",
            highlights = listOf(
                "cleartextTrafficPermitted=\"false\" in network_security_config.xml",
                "Secrets isolated via Secrets Gradle Plugin & BuildConfig (never hardcoded)",
                "AES-256 local preference protection & DataStore isolation",
                "Responsible disclosure channel: security@nexus-studio.io"
            ),
            referencePath = "res/xml/network_security_config.xml"
        ),
        ComplianceBadge(
            id = "floss-docs",
            category = "FLOSS & DOCS",
            title = "Apache-2.0 & Docs",
            statusText = "PUBLISHED",
            scoreOrLevel = "v1.0.0",
            docTitle = "Software Documentation & FLOSS Governance",
            summary = "Complete engineering documentation suite including architecture diagrams, onboarding guide, release changelog, contributing policy, and Apache-2.0 open source licensing.",
            highlights = listOf(
                "Apache License 2.0 permissive open-source license (LICENSE)",
                "Architecture & MVVM Data Flow specification (docs/ARCHITECTURE.md)",
                "Contribution & Bug-Reporting Standards (CONTRIBUTING.md)",
                "Release Notes & Semantic Version History (CHANGELOG.md)"
            ),
            referencePath = "docs/ARCHITECTURE.md"
        )
    )

    fun getTelemetrySnapshot(
        timeRange: TelemetryTimeRange,
        environment: PipelineEnvironment,
        projectId: String? = null
    ): ProjectTelemetrySnapshot {
        val seedOffset = projectId?.let { abs(it.hashCode() % 7) } ?: 0
        val envBuildMultiplier = when (environment) {
            PipelineEnvironment.ALL -> 1.0f
            PipelineEnvironment.PRODUCTION -> 0.42f
            PipelineEnvironment.STAGING -> 0.58f
        }
        val envSuccessBias = when (environment) {
            PipelineEnvironment.ALL -> 0.0f
            PipelineEnvironment.PRODUCTION -> 0.35f
            PipelineEnvironment.STAGING -> -0.65f
        }
        val projectScale = if (projectId != null) 0.32f else 1.0f

        val count = when (timeRange) {
            TelemetryTimeRange.SEVEN_DAYS -> 7
            TelemetryTimeRange.FOURTEEN_DAYS -> 10
            TelemetryTimeRange.THIRTY_DAYS -> 12
        }

        val dayLabels7 = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val fullDates7 = listOf("Nov 14", "Nov 15", "Nov 16", "Nov 17", "Nov 18", "Nov 19", "Nov 20")

        val buildPoints = mutableListOf<BuildFrequencyPoint>()
        val deployPoints = mutableListOf<DeploymentSuccessPoint>()
        val teamPoints = mutableListOf<TeamActivityPoint>()

        val baseAutoBuilds = listOf(28, 34, 41, 37, 46, 22, 31, 39, 44, 36, 48, 42)
        val baseManualBuilds = listOf(6, 9, 8, 11, 10, 4, 5, 8, 12, 7, 9, 8)
        val baseDurations = listOf(96, 88, 92, 84, 79, 75, 81, 86, 83, 78, 76, 74)
        val baseSuccess = listOf(98.4f, 99.1f, 98.8f, 99.5f, 99.7f, 99.2f, 99.6f, 98.9f, 99.4f, 99.8f, 99.6f, 99.9f)
        val baseCommits = listOf(42, 58, 64, 51, 72, 24, 33, 61, 69, 55, 76, 68)
        val baseReviews = listOf(18, 24, 29, 22, 34, 11, 15, 27, 31, 26, 35, 32)
        val baseAudits = listOf(6, 8, 11, 9, 14, 5, 7, 10, 13, 11, 15, 14)

        for (i in 0 until count) {
            val idx = (i + seedOffset) % baseAutoBuilds.size
            val label = when (timeRange) {
                TelemetryTimeRange.SEVEN_DAYS -> dayLabels7[i % dayLabels7.size]
                TelemetryTimeRange.FOURTEEN_DAYS -> "D${i + 1}"
                TelemetryTimeRange.THIRTY_DAYS -> "W${(i / 3) + 1}.${(i % 3) + 1}"
            }
            val fullDate = when (timeRange) {
                TelemetryTimeRange.SEVEN_DAYS -> fullDates7[i % fullDates7.size]
                TelemetryTimeRange.FOURTEEN_DAYS -> "Nov ${7 + i}"
                TelemetryTimeRange.THIRTY_DAYS -> "Oct/Nov #${i + 1}"
            }

            val auto = (baseAutoBuilds[idx] * envBuildMultiplier * projectScale).roundToInt().coerceAtLeast(3)
            val manual = (baseManualBuilds[idx] * envBuildMultiplier * projectScale).roundToInt().coerceAtLeast(1)
            val duration = (baseDurations[idx] + (seedOffset * 2) - (i / 2)).coerceIn(48, 135)

            buildPoints.add(
                BuildFrequencyPoint(
                    label = label,
                    fullDate = fullDate,
                    automatedBuilds = auto,
                    manualBuilds = manual,
                    avgDurationSec = duration
                )
            )

            val rate = (baseSuccess[idx] + envSuccessBias - (seedOffset * 0.08f)).coerceIn(95.5f, 100.0f)
            val totalDep = ((auto + manual) * 0.65f).roundToInt().coerceAtLeast(2)
            val rollbacks = if (rate < 98.7f) 2 else if (rate < 99.4f) 1 else 0
            val latency = (44 + (100f - rate) * 14f).roundToInt().coerceIn(24, 118)

            deployPoints.add(
                DeploymentSuccessPoint(
                    label = label,
                    fullDate = fullDate,
                    successRatePct = (rate * 10f).roundToInt() / 10f,
                    totalDeployments = totalDep,
                    rollbacks = rollbacks,
                    p95LatencyMs = latency
                )
            )

            val commits = (baseCommits[idx] * projectScale * (if (environment == PipelineEnvironment.PRODUCTION) 0.7f else 1.0f)).roundToInt().coerceAtLeast(4)
            val reviews = (baseReviews[idx] * projectScale).roundToInt().coerceAtLeast(2)
            val audits = (baseAudits[idx] * projectScale).roundToInt().coerceAtLeast(1)

            teamPoints.add(
                TeamActivityPoint(
                    label = label,
                    fullDate = fullDate,
                    commits = commits,
                    prReviews = reviews,
                    securityAudits = audits
                )
            )
        }

        val totalBuilds = buildPoints.sumOf { it.totalBuilds }
        val avgSuccess = deployPoints.map { it.successRatePct }.average().toFloat()
        val totalTeam = teamPoints.sumOf { it.totalEvents }
        val meanDuration = buildPoints.map { it.avgDurationSec }.average().roundToInt()

        val kpi = DashboardKpiSummary(
            totalBuilds = totalBuilds,
            buildsDelta = if (environment == PipelineEnvironment.PRODUCTION) "+11.4%" else "+18.2%",
            avgSuccessRatePct = (avgSuccess * 10f).roundToInt() / 10f,
            successDelta = if (avgSuccess >= 99.0f) "SLA Met" else "-0.3% vs SLA",
            totalTeamEvents = totalTeam,
            teamDelta = "+24.6%",
            meanBuildDurationSec = meanDuration,
            durationDelta = "-14s faster"
        )

        return ProjectTelemetrySnapshot(
            timeRange = timeRange,
            environment = environment,
            kpiSummary = kpi,
            buildFrequency = buildPoints,
            deploymentSuccess = deployPoints,
            teamActivity = teamPoints
        )
    }
}
