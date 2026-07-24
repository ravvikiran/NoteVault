# Add project specific ProGuard rules here.
-keepattributes *Annotation*

# Room entities
-keep class com.notevault.app.data.local.entity.** { *; }
-keep class com.notevault.app.data.model.** { *; }

# Export models (kotlinx.serialization)
-keep class com.notevault.app.data.export.** { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
}
-keepclasseswithmembers class ** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.notevault.app.**$$serializer { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
