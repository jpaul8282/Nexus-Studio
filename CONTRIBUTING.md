# Contributing to Nexus Studio

Thank you for contributing to **Nexus Studio**! This guide covers our bug-reporting workflow, code quality standards, static/dynamic analysis checks, and testing requirements.

---

## Bug-Reporting Process

Before filing a bug report, search existing issues to avoid duplicates. When opening a bug report, please include:

1. **App Version & Environment**: `versionName` / `versionCode`, Android API level, and build type (`debug` or `release`).
2. **Steps to Reproduce**: Minimal, deterministic steps to trigger the issue.
3. **Expected vs. Actual Behavior**: What should happen vs. what happened.
4. **Logs / Screenshots**: Redacted logcat output (never include API keys or PII).

For security bugs, **do not file a public issue** — follow [`SECURITY.md`](./SECURITY.md).

---

## Development & Code Quality Standards

- **Language & UI**: Kotlin + Jetpack Compose + Material Design 3 (`MaterialTheme.colorScheme`).
- **Architecture**: Strict MVVM (`ViewModel` + `StateFlow` + `Repository`) with unidirectional data flow.
- **Accessibility & Testing**:
  - Minimum `48.dp` interactive touch targets.
  - Meaningful `contentDescription` on icons/images.
  - Unique `Modifier.testTag("snake_case")` on primary interactive components.

---

## Automated Verification & Analysis

Run all checks locally before submitting a pull request:

```bash
# 1. Build verification
gradle assembleDebug

# 2. Unit & Robolectric test suite (including new functionality tests)
gradle :app:testDebugUnitTest

# 3. Android Lint static analysis
gradle lintDebug
```

---

## Licensing

By submitting a contribution, you agree that your work will be licensed under the **Apache License 2.0** ([`LICENSE`](./LICENSE)).
