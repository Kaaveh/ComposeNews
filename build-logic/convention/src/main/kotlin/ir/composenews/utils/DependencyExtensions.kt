package ir.composenews.utils

import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.api(dep: Any) {
    safeAdd("api", dep)
}

fun DependencyHandlerScope.implementation(dep: Any) {
    safeAdd("implementation", dep)
}

fun DependencyHandlerScope.testImplementation(dep: Any) {
    safeAdd("testImplementation", dep)
}

fun DependencyHandlerScope.androidTestImplementation(dep: Any) {
    safeAdd("androidTestImplementation", dep)
}

fun DependencyHandlerScope.debugImplementation(dep: Any) {
    safeAdd("debugImplementation", dep)
}

fun DependencyHandlerScope.custom(configuration: String, dep: Any) {
    safeAdd(configuration, dep)
}

private fun DependencyHandlerScope.safeAdd(
    configuration: String,
    dependency: Any,
) {
    when (dependency) {
        is Provider<*> -> {
            val value = dependency.orNull
            if (value != null) {
                safeAdd(configuration, value)
            }
        }

        is Collection<*> -> {
            dependency.forEach {
                if (it != null) {
                    safeAdd(configuration, it)
                }
            }
        }

        else -> {
            try {
                add(configuration, dependency)
            } catch (_: Exception) {
                throw IllegalArgumentException("Unsupported dependency type: ${dependency.javaClass.name}")
            }
        }
    }
}
