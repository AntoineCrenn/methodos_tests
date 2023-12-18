package livremanagement.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import org.junit.jupiter.api.Test


class ArchitectureTest {
    private val base = "livremanagement"

    @Test
    fun `concept architecture`() {
        val Classes: JavaClasses = ClassFileImporter()
            .withImportOption(ImportOption.DoNotIncludeTests())
            .importPackages(base)
        val regles = layeredArchitecture().consideringAllDependencies()
            .layer("Infrastructure").definedBy("$base.infrastructure..")
            .layer("Pilote").definedBy("$base.infrastructure.pilote..")
            .layer("Adaptateur").definedBy("$base.infrastructure.adaptateur..")
            .layer("Application").definedBy("$base.infrastructure.application..")
            .layer("Domaine").definedBy("$base.domaine..")
            .layer("Standard API")
            .definedBy("java..", "kotlin..", "kotlinx..", "org.jetbrains.annotations..")
            .withOptionalLayers(true)
            .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
            .whereLayer("Pilote").mayOnlyBeAccessedByLayers("Application")
            .whereLayer("Adaptateur").mayOnlyBeAccessedByLayers("Application")
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Pilote", "Adaptateur")
            .whereLayer("Domaine").mayOnlyAccessLayers("Standard API")
        regles.check(Classes)
    }
}