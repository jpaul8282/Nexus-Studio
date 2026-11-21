package com.example

import com.example.data.DashboardTelemetryRepository
import com.example.model.PipelineEnvironment
import com.example.model.TelemetryTimeRange
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DashboardTelemetryTest {

    @Test
    fun `telemetry snapshot generates expected series counts across ranges`() {
        val seven = DashboardTelemetryRepository.getTelemetrySnapshot(
            TelemetryTimeRange.SEVEN_DAYS,
            PipelineEnvironment.ALL
        )
        assertEquals(7, seven.buildFrequency.size)
        assertEquals(7, seven.deploymentSuccess.size)
        assertEquals(7, seven.teamActivity.size)

        val fourteen = DashboardTelemetryRepository.getTelemetrySnapshot(
            TelemetryTimeRange.FOURTEEN_DAYS,
            PipelineEnvironment.PRODUCTION
        )
        assertEquals(10, fourteen.buildFrequency.size)
        assertEquals(10, fourteen.deploymentSuccess.size)
        assertEquals(10, fourteen.teamActivity.size)

        val thirty = DashboardTelemetryRepository.getTelemetrySnapshot(
            TelemetryTimeRange.THIRTY_DAYS,
            PipelineEnvironment.STAGING
        )
        assertEquals(12, thirty.buildFrequency.size)
        assertEquals(12, thirty.deploymentSuccess.size)
        assertEquals(12, thirty.teamActivity.size)
    }

    @Test
    fun `kpi summary matches aggregated series totals and bounds`() {
        val snapshot = DashboardTelemetryRepository.getTelemetrySnapshot(
            TelemetryTimeRange.SEVEN_DAYS,
            PipelineEnvironment.ALL
        )
        val expectedBuilds = snapshot.buildFrequency.sumOf { it.automatedBuilds + it.manualBuilds }
        val expectedTeamEvents = snapshot.teamActivity.sumOf { it.commits + it.prReviews + it.securityAudits }

        assertEquals(expectedBuilds, snapshot.kpiSummary.totalBuilds)
        assertEquals(expectedTeamEvents, snapshot.kpiSummary.totalTeamEvents)
        assertTrue(snapshot.kpiSummary.avgSuccessRatePct in 90.0f..100.0f)
        assertTrue(snapshot.kpiSummary.meanBuildDurationSec > 0)
    }

    @Test
    fun `per project telemetry scales builds relative to global dashboard`() {
        val global = DashboardTelemetryRepository.getTelemetrySnapshot(
            TelemetryTimeRange.SEVEN_DAYS,
            PipelineEnvironment.ALL
        )
        val project = DashboardTelemetryRepository.getTelemetrySnapshot(
            TelemetryTimeRange.SEVEN_DAYS,
            PipelineEnvironment.ALL,
            projectId = "1"
        )
        assertTrue(project.kpiSummary.totalBuilds < global.kpiSummary.totalBuilds)
        assertTrue(project.kpiSummary.totalBuilds > 0)
    }

    @Test
    fun `compliance badges include openssf proguard security and docs`() {
        val ids = DashboardTelemetryRepository.COMPLIANCE_BADGES.map { it.id }.toSet()
        assertTrue(ids.contains("openssf-best-practices"))
        assertTrue(ids.contains("openssf-scorecard"))
        assertTrue(ids.contains("proguard-r8"))
        assertTrue(ids.contains("security-mitm"))
        assertTrue(ids.contains("floss-docs"))
    }
}
