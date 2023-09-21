plugins {
    libs.plugins.apply {
        alias(android.application) apply false
        alias(kotlin.parcelize) apply false
        alias(android.library) apply false
        alias(kotlin.android) apply false
        alias(hilt.android) apply false
        alias(kapt) apply false
        alias(detekt) apply false
        alias(kotliner) apply false
    }
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}

// Run it with: gradle assembleRelease -PcomposeCompilerReports=true
 subprojects {
     tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
         kotlinOptions {
             if (project.findProperty("composeCompilerReports") == "true") {
                 freeCompilerArgs += listOf(
                     "-P",
                     "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_compiler"
                 )
             }
             if (project.findProperty("composeCompilerMetrics") == "true") {
                 freeCompilerArgs += listOf(
                     "-P",
                     "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_compiler"
                 )
             }
         }
     }
 }
