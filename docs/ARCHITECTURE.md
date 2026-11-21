# Nexus Studio — Software Architecture & Engineering Guide

## 1. Overview

Nexus Studio is structured around **MVVM (Model-View-ViewModel)** with unidirectional data flow (`StateFlow` / `collectAsState`) and **Jetpack Compose** Material Design 3 UI components.

```
┌──────────────────────────────────────────────────────────────────────┐
│                         UI Layer (Compose)                           │
│  HomeScreen • ProjectDetailScreen • TemplatesScreen • AccountProfile │
│  DashboardCharts (BuildFreq, DeploySuccess, TeamActivity, Badges)    │
└───────────────────────────────┬──────────────────────────────────────┘
                                │ observes StateFlow / dispatches intents
┌───────────────────────────────▼──────────────────────────────────────┐
│                         ViewModel Layer                              │
│                       NexusStudioViewModel                           │
│  • filteredProjects / filteredTemplates                              │
│  • telemetrySnapshot (TimeRange x PipelineEnvironment)               │
│  • userProfile / chatMessages / complianceBadges                     │
└───────────────────────────────┬──────────────────────────────────────┘
                                │ reads / persists
┌───────────────────────────────▼──────────────────────────────────────┐
│                       Repository & Data Layer                        │
│  ProjectRepository • DashboardTelemetryRepository • TemplateRepo     │
│  UserPreferencesRepository (DataStore) • GeminiChatService (HTTPS)   │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 2. Dashboard Visualization Engine (`DashboardCharts.kt`)

All charts are implemented natively in Jetpack Compose `Canvas` without web views:

- **BuildFrequencyBarChartCard**: Stacked vertical bars (`automatedBuilds` + `manualBuilds`) with dashed Cartesian gridlines, spring/tween entry animation, and `detectTapGestures` bar selection tooltip.
- **DeploymentSuccessAreaChartCard**: Smooth cubic-Bezier curve with translucent gradient area fill, dashed `99.0%` SLA threshold line, and tap scrubbing for rollbacks & p95 latency.
- **TeamActivityLineChartCard**: Multi-series line chart (`Commits`, `PR Reviews`, `Security Audits`) with interactive series toggle chips and day inspection callout.

---

## 3. Security, OpenSSF & ProGuard Configuration

- **Network Security**: `res/xml/network_security_config.xml` sets `cleartextTrafficPermitted="false"`.
- **ProGuard / R8**: `app/proguard-rules.pro` defines keep rules for Coroutines, Compose, Retrofit, OkHttp, Moshi, Room, DataStore, Firebase, and Coil, while stripping release debug logs and obfuscating internal packages.
- **Compliance Verification**: OpenSSF Best Practices and Scorecard controls are surfaced both in repository governance files (`SECURITY.md`, `LICENSE`, `CHANGELOG.md`) and in-app via `SecuritySsfBadgesSection` and `ComplianceDocModalSheet`.
