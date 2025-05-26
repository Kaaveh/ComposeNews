package ir.composenews.utils

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.implementation(dep: Provider<*>) {
    addDependency("implementation", dep)
}

fun DependencyHandlerScope.testImplementation(dep: Provider<*>) {
    addDependency("testImplementation", dep)
}

fun DependencyHandlerScope.androidTestImplementation(dep: Provider<*>) {
    addDependency("androidTestImplementation", dep)
}

fun DependencyHandlerScope.debugImplementation(dep: Provider<*>) {
    addDependency("debugImplementation", dep)
}

fun DependencyHandlerScope.custom(configuration: String, dep: Provider<*>) {
    addDependency(configuration, dep)
}

private fun DependencyHandlerScope.addDependency(
    configuration: String,
    provider: Provider<*>,
) {
    val dependency = provider.orNull
    when (dependency) {
        is MinimalExternalModuleDependency -> {
            add(configuration, dependency)
        }

        is Collection<*> -> {
            dependency.forEach {
                if (it is MinimalExternalModuleDependency) {
                    add(configuration, it)
                } else {
                    throw IllegalArgumentException("Unsupported dependency in bundle: ${it?.javaClass?.name}")
                }
            }
        }

        else -> throw IllegalArgumentException("Unsupported dependency type: ${dependency?.javaClass?.name}")
    }
}
