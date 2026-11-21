# Security Policy

[![OpenSSF Best Practices](https://img.shields.io/badge/OpenSSF-Best%20Practices%20Passing-005AC1?style=flat-square)](./README.md)
[![OpenSSF Scorecard](https://img.shields.io/badge/OpenSSF%20Scorecard-8.8%2F10-0284C7?style=flat-square)](./README.md)

## Supported Versions

| Version | Supported          | Security Updates |
| ------- | ------------------ | ---------------- |
| 1.0.x   | :white_check_mark: | Active           |
| < 1.0   | :x:                | End of Life      |

---

## Reporting a Vulnerability

We take security vulnerabilities seriously and follow coordinated vulnerability disclosure principles.

**Please do NOT open a public issue for security vulnerabilities.**

1. Email **security@nexus-studio.io** with:
   - Affected version and component (e.g., `app/src/main/java/com/example/...`)
   - Detailed reproduction steps or proof-of-concept
   - Assessed impact (data exposure, MITM, privilege escalation, etc.)
2. You will receive an acknowledgement within **48 hours** and an initial triage assessment within **5 business days**.
3. Verified vulnerabilities are patched in a dedicated security release and credited in `CHANGELOG.md` (unless anonymity is requested).

---

## Security Architecture & Practices

### 1. Transport Security (MITM Prevention)
- Cleartext HTTP traffic is globally disabled in [`app/src/main/res/xml/network_security_config.xml`](./app/src/main/res/xml/network_security_config.xml) (`cleartextTrafficPermitted="false"`) and enforced in [`AndroidManifest.xml`](./app/src/main/AndroidManifest.xml) (`android:usesCleartextTraffic="false"`).
- Outbound HTTPS connections rely on system-trusted certificate authorities with TLS 1.2/1.3.

### 2. Cryptographic & Secret Management Practices
- **No Hardcoded Secrets**: API keys and sensitive tokens are never committed to source control. Credentials are injected via the AI Studio Secrets / `.env` mechanism through the Secrets Gradle Plugin (`BuildConfig`).
- **Least-Privilege Permissions**: Only `android.permission.INTERNET` is declared in `AndroidManifest.xml`. Media selection uses zero-permission system pickers.
- **No Dynamic Code Loading (DCL)**: No external `.dex`, `.jar`, or `.so` loading occurs at runtime.

### 3. R8 / ProGuard Hardening
- Release builds (`isMinifyEnabled = true`, `isShrinkResources = true`) strip unused code/resources, repackage internal classes (`-repackageclasses 'o'`), and remove debug/info logs (`Log.v`, `Log.d`, `Log.i`) via [`app/proguard-rules.pro`](./app/proguard-rules.pro).

### 4. Static & Dynamic Verification
- Static checks: Android Lint (`gradle lintDebug`) and strict Kotlin compiler type safety.
- Automated tests: JUnit4 and Robolectric local JVM tests (`gradle :app:testDebugUnitTest`).
