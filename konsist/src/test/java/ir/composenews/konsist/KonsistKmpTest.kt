@file:Suppress(
    "ktlint:standard:multiline-expression-wrapping",
    "ktlint:standard:chain-method-continuation",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertFalse
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldNotBeEmpty

class KonsistKmpTest : AnnotationSpec() {
    // Every KMP module, `feature:*:presentation` included, keeps its shared code in commonMain.
    private val commonMainFiles = Konsist
        .scopeFromSourceSet("commonMain")
        .files

    @Test
    fun `commonMain scope is not empty`() {
        commonMainFiles.shouldNotBeEmpty()
    }

    @Test
    fun `commonMain does not import Compose or the Android SDK`() {
        commonMainFiles
            .assertFalse {
                it.imports.any { import ->
                    import.name.startsWith("androidx.compose.") || import.name.startsWith("android.")
                }
            }
    }
}
