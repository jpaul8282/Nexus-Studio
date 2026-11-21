# Changelog

All notable changes to **Nexus Studio** are documented in this file.
The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and [Semantic Versioning](https://semver.org/).

## [1.0.0] - 2026-11-20

### Added
- **Engineering Telemetry Dashboard** (`DashboardCharts.kt`, `DashboardTelemetryRepository.kt`, `DashboardTelemetry.kt`):
  - **Build Frequency Bar Chart**: Stacked Automated CI vs. Manual Release executions with interactive bar tap tooltips.
  - **Deployment Success Rate Area Chart**: Cubic-Bezier reliability curve with a `99.0%` SLA threshold line, rollback metrics, and p95 latency inspection.
  - **Team Activity Over Time Multi-Line Chart**: Series for Commits, PR Reviews, and Security Audits with interactive legend toggles.
  - **Timeframe & Pipeline Filters**: Support for `7D`, `14D`, and `30D` ranges and `All Pipelines`, `Production`, and `Staging` environments across both the Home Dashboard and Project Detail Specs.
- **Security, OpenSSF ("SSF") & Governance Badges**:
  - Added interactive compliance badges strip (`OpenSSF Best Practices`, `OpenSSF Scorecard`, `ProGuard & R8`, `TLS 1.3 & MITM Shield`, `Apache-2.0 & Docs`) and `ComplianceDocModalSheet`.
- **ProGuard & R8 Hardening**:
  - Configured `app/proguard-rules.pro` with Kotlin, Coroutines, Compose, Retrofit, OkHttp, Moshi, Room, DataStore, Firebase, and Coil keep rules, release log stripping, and stack trace obfuscation.
  - Enabled `isMinifyEnabled = true` and `isShrinkResources = true` for `release` builds.
- **MITM Transport Protection**:
  - Added `app/src/main/res/xml/network_security_config.xml` disabling cleartext traffic and wired it in `AndroidManifest.xml`.
- **Documentation & FLOSS Governance**:
  - Added `README.md` with Build, OpenSSF, Security, License, and Version badges, plus `SECURITY.md`, `CONTRIBUTING.md`, `LICENSE` (Apache-2.0), and `docs/ARCHITECTURE.md`.
