# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class * extends android.app.Activity { *; }
-keep class * extends androidx.fragment.app.Fragment { *; }
-keep class * extends android.app.Service { *; }
-keep class * extends android.content.BroadcastReceiver { *; }
-keep class * extends android.content.ContentProvider { *; }
-keep class androidx.navigation.NavArgs { *; }

-keepclasseswithmembers class * {
    @androidx.lifecycle.ViewModelInject <init>(...);
}
-keepclasseswithmembers class * {
    @javax.inject.Inject <init>(...);
}

-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclassmembers class * {
    @androidx.compose.ui.tooling.preview.Preview *;
}
-keepattributes *Annotation*

-keep class * extends androidx.compose.runtime.Composable { *; }

-keepclassmembers class * {
    @dagger.hilt.android.lifecycle.HiltViewModel *;
}
-keep,@dagger.hilt.InstallIn class * { *; }
-keep class dagger.hilt.** { *; }
-dontwarn dagger.internal.codegen.ComponentProcessor

-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-keep class * implements retrofit2.CallAdapter.Factory { *; }
-keep class * implements retrofit2.Converter.Factory { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keep class ir.composenews.db.** { *; }
-keep interface ir.composenews.db.** { *; }

-keep class io.kotest.** { *; }
-dontwarn io.kotest.**
-keep class kotlinx.coroutines.test.** { *; }
-dontwarn kotlinx.coroutines.test.**

-dontwarn java.lang.management.**
-dontwarn reactor.blockhound.**

-dontwarn reactor.blockhound.**