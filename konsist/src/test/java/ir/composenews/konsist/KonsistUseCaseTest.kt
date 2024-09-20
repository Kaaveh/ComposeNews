package ir.composenews.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.AnnotationSpec

class KonsistUseCaseTest : AnnotationSpec() {
    private val domainModuleUseCasesScope = Konsist
        .scopeFromProject(
            moduleName = "domain/market",
            sourceSetName = "main",
        )
        .classes()
        .filter { it.resideInPackage("..use_case..") }

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
