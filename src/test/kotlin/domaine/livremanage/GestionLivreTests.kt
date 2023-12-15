package domaine.livremanage

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsExactly
import assertk.assertions.containsExactlyInAnyOrder
import domaine.livre.Livre
import domaine.port.LivrePort
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import net.jqwik.api.*
import net.jqwik.api.Combinators.combine
import net.jqwik.kotlin.api.ofSize
import org.junit.jupiter.api.Test

class GestionLivreTests {
    private val gestionlivre = mockk<GestionLivre>()
    private val port = mockk<LivrePort>()
    private fun strings(): Arbitrary<String> = Arbitraries.strings().withCharRange('A', 'Z').ofMinLength(3).ofMaxLength(10)
    val liste = mutableListOf<Livre>()

    @Test
    fun `test creation livre`() {
        justRun {port.creer_livre(any()) }
        val livre = Livre("Charles Leclerc, le prodige", "Charles Leclerc")
        gestionlivre.creer_livre(livre)
        verify {port.creer_livre(livre)}
    }

    @Property
    fun `test recuperation livres trier par titre`() {
        every{port.liste_livre()} returns listOf(
            Livre("Gasly, le magnifique", "Pierre Gasly"),
            Livre("Charles Leclerc, le prodige", "Charles Leclerc"),
            Livre("La mort en face", "Romain Grosjean"),
            Livre("Ma vie sans gravité", "Thomas Pesquet")
        )

        val resultat = gestionlivre.liste_livre()

        assertThat(resultat).containsExactly(
            Livre("Gasly, le magnifique", "Pierre Gasly"),
            Livre("Charles Leclerc, le prodige", "Charles Leclerc"),
            Livre("La mort en face", "Romain Grosjean"),
            Livre("Ma vie sans gravité", "Thomas Pesquet")
        )
    }

    @Property
    fun `test recuperation livre base donnees`(
        @ForAll("generateurlivre") livres: List<Livre>) {
        every {port.liste_livre()} returns livres
        val livres = gestionlivre.liste_livre()
        assertThat(livres).containsExactlyInAnyOrder(*livres.toTypedArray())

    }

    @Provide
    fun generateurlivre(): Arbitrary<List<Livre>> {
        return combine(strings(), strings()).`as` {titre: String, auteur: String -> Livre(titre, auteur)
        }.list().uniqueElements().ofSize(20)
    }
}