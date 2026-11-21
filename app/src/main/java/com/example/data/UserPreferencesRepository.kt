package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "nexus_user_profile_preferences")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: UserPreferencesRepository? = null

        fun getInstance(context: Context): UserPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferencesRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
        val FULL_NAME = stringPreferencesKey("user_full_name")
        val EMAIL = stringPreferencesKey("user_email")
        val PROFILE_IMAGE_URL = stringPreferencesKey("profile_image_url")
        val ORGANIZATION = stringPreferencesKey("organization")
        val ROLE_TITLE = stringPreferencesKey("role_title")
        val PHONE = stringPreferencesKey("phone")
        val COUNTRY = stringPreferencesKey("country")
        val TIER = stringPreferencesKey("tier")
        val BIO = stringPreferencesKey("bio")
        val MEMBER_SINCE = stringPreferencesKey("member_since")
        val SECURITY_SCORE = intPreferencesKey("security_score")
        val PUSH_NOTIFICATIONS = booleanPreferencesKey("push_notifications")
        val SMS_ALERTS = booleanPreferencesKey("sms_alerts")
        val EMAIL_DIGEST = booleanPreferencesKey("email_digest")
        val TWO_FACTOR_AUTH = booleanPreferencesKey("two_factor_auth")
        val BIOMETRIC_AUTH = booleanPreferencesKey("biometric_auth")
        val HIGH_FREQ_TELEMETRY = booleanPreferencesKey("high_freq_telemetry")
        val DEFAULT_VIEW_GRID = booleanPreferencesKey("default_view_grid")
    }

    val userProfileFlow: Flow<UserProfile> = context.userDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val default = UserProfile()
            val fullName = preferences[PreferencesKeys.FULL_NAME] ?: default.fullName
            val initials = if (fullName.isNotBlank()) {
                fullName.trim().split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .take(2)
                    .joinToString("")
                    .uppercase()
                    .ifBlank { "JP" }
            } else "JP"

            UserProfile(
                id = preferences[PreferencesKeys.USER_ID] ?: default.id,
                fullName = fullName,
                email = preferences[PreferencesKeys.EMAIL] ?: default.email,
                profileImageUrl = preferences[PreferencesKeys.PROFILE_IMAGE_URL],
                organization = preferences[PreferencesKeys.ORGANIZATION] ?: default.organization,
                roleTitle = preferences[PreferencesKeys.ROLE_TITLE] ?: default.roleTitle,
                phone = preferences[PreferencesKeys.PHONE] ?: default.phone,
                country = preferences[PreferencesKeys.COUNTRY] ?: default.country,
                tier = preferences[PreferencesKeys.TIER] ?: default.tier,
                avatarInitials = initials,
                bio = preferences[PreferencesKeys.BIO] ?: default.bio,
                memberSince = preferences[PreferencesKeys.MEMBER_SINCE] ?: default.memberSince,
                securityScore = preferences[PreferencesKeys.SECURITY_SCORE] ?: default.securityScore,
                pushNotificationsEnabled = preferences[PreferencesKeys.PUSH_NOTIFICATIONS] ?: default.pushNotificationsEnabled,
                smsAlertsEnabled = preferences[PreferencesKeys.SMS_ALERTS] ?: default.smsAlertsEnabled,
                emailDigestEnabled = preferences[PreferencesKeys.EMAIL_DIGEST] ?: default.emailDigestEnabled,
                twoFactorAuthEnabled = preferences[PreferencesKeys.TWO_FACTOR_AUTH] ?: default.twoFactorAuthEnabled,
                biometricAuthEnabled = preferences[PreferencesKeys.BIOMETRIC_AUTH] ?: default.biometricAuthEnabled,
                highFrequencyTelemetry = preferences[PreferencesKeys.HIGH_FREQ_TELEMETRY] ?: default.highFrequencyTelemetry,
                defaultViewIsGrid = preferences[PreferencesKeys.DEFAULT_VIEW_GRID] ?: default.defaultViewIsGrid
            )
        }

    suspend fun updateFullName(name: String) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.FULL_NAME] = name
        }
    }

    suspend fun updateProfileImageUrl(imageUrl: String?) {
        context.userDataStore.edit { preferences ->
            if (imageUrl.isNullOrBlank()) {
                preferences.remove(PreferencesKeys.PROFILE_IMAGE_URL)
            } else {
                preferences[PreferencesKeys.PROFILE_IMAGE_URL] = imageUrl
            }
        }
    }

    suspend fun updateFullProfile(
        name: String,
        email: String,
        org: String,
        role: String,
        phone: String,
        country: String,
        bio: String,
        profileImageUrl: String? = null
    ) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.FULL_NAME] = name
            preferences[PreferencesKeys.EMAIL] = email
            preferences[PreferencesKeys.ORGANIZATION] = org
            preferences[PreferencesKeys.ROLE_TITLE] = role
            preferences[PreferencesKeys.PHONE] = phone
            preferences[PreferencesKeys.COUNTRY] = country
            preferences[PreferencesKeys.BIO] = bio
            if (profileImageUrl != null) {
                if (profileImageUrl.isBlank()) {
                    preferences.remove(PreferencesKeys.PROFILE_IMAGE_URL)
                } else {
                    preferences[PreferencesKeys.PROFILE_IMAGE_URL] = profileImageUrl
                }
            }
        }
    }

    suspend fun updateNotificationPreferences(push: Boolean, sms: Boolean, digest: Boolean) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.PUSH_NOTIFICATIONS] = push
            preferences[PreferencesKeys.SMS_ALERTS] = sms
            preferences[PreferencesKeys.EMAIL_DIGEST] = digest
        }
    }

    suspend fun updateSecurityPreferences(twoFactor: Boolean, biometric: Boolean) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.TWO_FACTOR_AUTH] = twoFactor
            preferences[PreferencesKeys.BIOMETRIC_AUTH] = biometric
            preferences[PreferencesKeys.SECURITY_SCORE] = if (twoFactor) 100 else 75
        }
    }

    suspend fun updateTelemetry(enabled: Boolean) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.HIGH_FREQ_TELEMETRY] = enabled
        }
    }

    suspend fun clearProfileData() {
        context.userDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
