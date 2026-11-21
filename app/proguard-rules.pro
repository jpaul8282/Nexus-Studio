# ==============================================================================
# Nexus Studio — R8 / ProGuard Hardening & Keep Rules
# ==============================================================================

# ------------------------------------------------------------------------------
# 1. Stack Trace & Source File Obfuscation
# ------------------------------------------------------------------------------
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-repackageclasses 'o'
-allowaccessmodification

# Preserve annotations, signatures, and Kotlin metadata for reflection/serialization
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,RuntimeVisibleAnnotations
-keep class kotlin.Metadata { *; }

# ------------------------------------------------------------------------------
# 2. Release Log Stripping (Prevent sensitive telemetry leaks in production)
# ------------------------------------------------------------------------------
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# ------------------------------------------------------------------------------
# 3. Kotlin Coroutines & Flow
# ------------------------------------------------------------------------------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# ------------------------------------------------------------------------------
# 4. Jetpack Compose, Lifecycle, ViewModel & Navigation
# ------------------------------------------------------------------------------
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keepclassmembers class * extends androidx.lifecycle.AndroidViewModel {
    <init>(...);
}
-keepnames class androidx.compose.runtime.** { *; }
-keepclassmembers class com.example.model.** { *; }

# ------------------------------------------------------------------------------
# 5. Retrofit, OkHttp & Moshi Serialization
# ------------------------------------------------------------------------------
# Retrofit service interfaces and generic signatures
-keepattributes Signature,Exceptions
-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# OkHttp & Okio
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Moshi Codegen & Kotlin Reflect adapters
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}
-keep @com.squareup.moshi.JsonClass class * { *; }
-keep class * extends com.squareup.moshi.JsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}

# ------------------------------------------------------------------------------
# 6. Room Database & DataStore Preferences
# ------------------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-dontwarn androidx.room.paging.**

-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}

# ------------------------------------------------------------------------------
# 7. Firebase AI Logic & App Check
# ------------------------------------------------------------------------------
-keepattributes EnclosingMethod,InnerClasses
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# ------------------------------------------------------------------------------
# 8. Coil Image Loader
# ------------------------------------------------------------------------------
-keep class coil.** { *; }
-dontwarn coil.**
