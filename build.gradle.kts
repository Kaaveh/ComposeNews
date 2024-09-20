import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    libs.plugins.apply {
        alias(android.application) apply false
        alias(kotlin.parcelize) apply false
        alias(android.library) apply false
        alias(kotlin.android) apply false
        alias(hilt.android) apply false
        alias(kotliner) apply false
        alias(detekt) apply false
//        alias(ksp) apply false
        alias(compose) apply false
    }
}


gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

// Run it with: gradle assembleRelease -PcomposeCompilerReports=true
subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.layout.buildDirectory.asFile.get()}/compose_compiler"
                )
            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.layout.buildDirectory.asFile.get()}/compose_compiler"
                )
            }
        }
    }
}

configureGitHooks()
fun Project.configureGitHooks() {
    registerCopyPreCommitHookTask()
    registerCopyPrePushHookTask()
    registerCopyGitHooksTask()
    registerInstallGitHooksTask()
}

fun Project.registerCopyPreCommitHookTask() {
    tasks.register("copyPreCommitHook", Copy::class.java) {
        group = "git hooks"
        val suffix = osSuffix()
        from(file("$rootDir/git-hooks/pre-commit-$suffix.sh"))
        into("$rootDir/.git/hooks")
        rename("pre-commit-$suffix.sh", "pre-commit")
        fileMode = "775".toInt(8)
    }
}

fun Project.registerCopyPrePushHookTask() {
    tasks.register("copyPrePushHook", Copy::class.java) {
        group = "git hooks"
        val suffix = osSuffix()
        from(file("$rootDir/git-hooks/pre-push-$suffix.sh"))
        into("$rootDir/.git/hooks")
        rename("pre-push-$suffix.sh", "pre-push")
        fileMode = "775".toInt(8)
    }
}

fun Project.registerCopyGitHooksTask() {
    tasks.register("copyGitHooks", Copy::class.java) {
        group = "git hooks"
        description = "Copies the git hooks from /git-hooks to the .git folder."
        dependsOn("copyPreCommitHook", "copyPrePushHook")
    }
}

fun Project.registerInstallGitHooksTask() {
    tasks.register("installGitHooks", Exec::class.java) {
        group = "git hooks"
        description = "Installs the pre-commit git hooks from /git-hooks."
        workingDir = rootDir
        commandLine("chmod", "-R", "+x", ".git/hooks/")
        if (!Os.isFamily(Os.FAMILY_WINDOWS)) {
            commandLine("chmod", "-R", "+x", ".git/hooks/")
        }
        dependsOn("copyGitHooks")
        doLast {
            logger.info("Git hook installed successfully.")
        }
    }
}

fun osSuffix(): String {
    return if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        "windows"
    } else {
        "macos"
    }
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}

afterEvaluate {
    // We install the hook at the first occasion
    tasks.named("clean") {
        dependsOn(":installGitHooks")
    }
}