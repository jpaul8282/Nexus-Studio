package com.example.model

data class UserProfile(
    val id: String = "USR-88421",
    val fullName: String = "J.P. Westerveld",
    val email: String = "westerveldjp@gmail.com",
    val profileImageUrl: String? = null,
    val organization: String = "Nexus Studio & First Aid Sponder",
    val roleTitle: String = "Lead Systems Architect & Founder",
    val phone: String = "+31 6 1234 5678",
    val country: String = "Netherlands",
    val tier: String = "TIER 1 ARCHITECTURE PARTNER",
    val avatarInitials: String = computeInitials(fullName),
    val bio: String = "Architecting high-concurrency cloud systems, progressive Web3 interfaces, and distributed edge applications across Europe.",
    val memberSince: String = "March 2024",
    val securityScore: Int = 100,
    val pushNotificationsEnabled: Boolean = true,
    val smsAlertsEnabled: Boolean = true,
    val emailDigestEnabled: Boolean = true,
    val twoFactorAuthEnabled: Boolean = true,
    val biometricAuthEnabled: Boolean = true,
    val highFrequencyTelemetry: Boolean = true,
    val defaultViewIsGrid: Boolean = true
) {
    val hasCustomAvatar: Boolean
        get() = !profileImageUrl.isNullOrBlank()

    companion object {
        fun computeInitials(name: String): String {
            val parts = name.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
            return when {
                parts.isEmpty() -> "JP"
                parts.size == 1 -> parts.first().take(1).uppercase()
                else -> "${parts.first().take(1)}${parts.last().take(1)}".uppercase()
            }
        }
    }
}
