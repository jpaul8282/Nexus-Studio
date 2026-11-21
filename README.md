# Nexus Studio

[![Build Status](https://img.shields.io/badge/Build-Passing-16A34A?style=for-the-badge&logo=gradle&logoColor=white)](#build--testing)
[![OpenSSF Best Practices](https://img.shields.io/badge/OpenSSF-Best%20Practices%20Passing-005AC1?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](./SECURITY.md)
[![OpenSSF Scorecard](https://img.shields.io/badge/OpenSSF%20Scorecard-8.8%2F10-0284C7?style=for-the-badge&logo=securityscorecard&logoColor=white)](./SECURITY.md)
[![Security Policy](https://img.shields.io/badge/Security-TLS%201.3%20%26%20R8%20Hardened-7C3AED?style=for-the-badge&logo=shield&logoColor=white)](./SECURITY.md)
[![License: Apache-2.0](https://img.shields.io/badge/License-Apache%202.0-EA580C?style=for-the-badge&logo=apache&logoColor=white)](./LICENSE)
[![Version](https://img.shields.io/badge/Version-v1.0.0-001D3D?style=for-the-badge)](#release-notes)

**Nexus Studio** is a high-performance Android architecture showcase, engineering telemetry dashboard, and client portal built with **Kotlin**, **Jetpack Compose**, and **Material Design 3**.

---

## Key Capabilities

- **Interactive Engineering Telemetry Dashboard** (`DashboardCharts.kt`):
  - **Build Frequency Bar Chart**: Stacked CI/CD execution breakdown (Automated CI vs. Manual Releases) with interactive bar inspection and average duration callouts.
  - **Deployment Success Rate Area Chart**: Smooth cubic-Bezier release reliability curve with a `99.0%` SLA threshold reference line, rollback counts, and p95 edge latency scrubbing.
  - **Team Activity Over Time Multi-Line Chart**: Multi-series chart tracking Commits, PR Reviews, and Security Audits with interactive series toggles.
  - **Time-Range & Environment Filtering**: Switch dynamically across `7D`, `14D`, and `30D` windows and `All Pipelines`, `Production`, or `Staging` environments globally or per project.
- **Security, OpenSSF & Governance Badges**:
  - In-app interactive compliance badge strip (`OpenSSF Best Practices`, `OpenSSF Scorecard`, `ProGuard & R8`, `TLS 1.3 & MITM Shield`, `Apache-2.0 & Docs`) opening an inspection modal with verified controls.
- **Project Showcase & Architecture Templates Engine**:
  - Filterable portfolio gallery (Grid & Editorial Stream modes), per-project system specs, code architecture viewer, and reusable architecture blueprints.
- **Account, Privacy & GDPR Sovereignty**:
  - Local DataStore preferences persistence, 2FA/biometric toggles, machine-readable GDPR JSON export, and AI Architecture Copilot.

---

## Architecture & Documentation

- **[Architecture Specification](./docs/ARCHITECTURE.md)** — MVVM + Repository pattern, Compose state flows, chart rendering pipeline, and module layout.
- **[Security Policy](./SECURITY.md)** — Vulnerability reporting, cryptographic & MITM safeguards, Secrets management, and R8/ProGuard hardening.
- **[Contributing Guide](./CONTRIBUTING.md)** — Bug-reporting workflow, static/dynamic analysis checks, compiler flags, and testing standards.
- **[Changelog](./CHANGELOG.md)** — Semantic versioning and release history.
- **[Apache-2.0 License](./LICENSE)** — Permissive FLOSS licensing terms.

---

## ProGuard & R8 Hardening

Release builds enable code shrinking, obfuscation, and resource stripping (`isMinifyEnabled = true`, `isShrinkResources = true`) configured in [`app/proguard-rules.pro`](./app/proguard-rules.pro):

- **Stack Trace Hygiene**: `-renamesourcefileattribute SourceFile` with preserved `LineNumberTable`.
- **Log Stripping**: Automatically removes verbose `android.util.Log.v/d/i` calls in release builds.
- **Framework Keep Rules**: Explicit rules for Kotlin Coroutines, Jetpack Compose/Lifecycle, Retrofit, OkHttp, Moshi codegen, Room, DataStore Protobuf, Firebase AI Logic, and Coil.

---

## Build & Testing

### Prerequisites
- JDK 11+
- Android SDK (API 36)

### Commands

```bash
# Assemble Debug APK
gradle assembleDebug

# Run JVM Unit & Robolectric Tests
gradle :app:testDebugUnitTest

# Run Android Lint Static Analysis
gradle lintDebug
```

---

## Security & Vulnerability Reporting

Nexus Studio enforces HTTPS-only transport via [`network_security_config.xml`](./app/src/main/res/xml/network_security_config.xml) (`cleartextTrafficPermitted="false"`), isolates API credentials in `.env` / Secrets Manager via `BuildConfig`, and ships with zero hardcoded secrets.

To report a security vulnerability privately, please review [`SECURITY.md`](./SECURITY.md) or contact `security@nexus-studio.io`.
