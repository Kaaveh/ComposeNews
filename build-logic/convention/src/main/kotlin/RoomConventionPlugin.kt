import ir.composenews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RoomConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyDependencies()
        }
    }

    private fun Project.applyDependencies() {
        dependencies {
            "implementation"(libs.findBundle("room").get())
            "ksp"(libs.findLibrary("room.compiler").get())
        }
    }
}