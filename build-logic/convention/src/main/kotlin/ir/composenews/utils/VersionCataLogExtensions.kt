package ir.composenews.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency

val Project.vLibs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.version(name: String): String {
    return findVersion(name).get().toString()
}

fun VersionCatalog.plugin(name: String): PluginDependency {
    return findPlugin(name).orElseThrow {
        IllegalArgumentException("Plugin '$name' not found in libs.versions.toml")
    }.get()
}

fun VersionCatalog.library(name: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(name).orElseThrow {
        IllegalArgumentException("Library '$name' not found in libs.versions.toml")
    }
}

fun VersionCatalog.bundle(name: String): Provider<ExternalModuleDependencyBundle> {
    return findBundle(name).orElseThrow {
        IllegalArgumentException("Bundle '$name' not found in libs.versions.toml")
    }
}
