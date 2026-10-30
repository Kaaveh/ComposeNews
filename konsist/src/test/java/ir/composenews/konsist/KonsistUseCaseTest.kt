@file:Suppress(
    "ktlint:standard:multiline-expression-wrapping",
    "ktlint:standard:chain-method-continuation",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldNotBeEmpty

class KonsistUseCaseTest : AnnotationSpec() {
    private val domainModuleUseCasesScope = Konsist
        .scopeFromProject(
            moduleName = "domain/market",
            sourceSetName = "commonMain",
        )
        .classes()
        .filter { it.resideInPackage("..use_case..") }

    // A wrong module or source set name gives an empty scope, and every rule below passes vacuously.
    @Test
    fun `use case scope is not empty`() {
        domainModuleUseCasesScope.shouldNotBeEmpty()
    }

    @Test
    fun `every use case has name ending with 'UseCase'`() {
        domainModuleUseCasesScope
            .assertTrue {
                it.hasNameEndingWith("UseCase")
            }
    }

    @Test
    fun `every use case has operator method name 'invoke'`() {
        domainModuleUseCasesScope
            .assertTrue {
                it.hasFunction { function ->
                    function.name == "invoke" && function.hasOperatorModifier
                }
            }
    }

    @Test
    fun `every use case has only one public method 'invoke'`() {
        domainModuleUseCasesScope
            .assertTrue {
                it.countFunctions { item ->
                    item.hasPublicOrDefaultModifier
                } == 1
            }
    }

    @Test
    fun `every use case is public`() {
        domainModuleUseCasesScope
            .assertTrue {
                it.hasPublicOrDefaultModifier
            }
    }
}
