package livremanagement.domaine.livremanage

import assertk.assertThat
import assertk.assertFailure
import assertk.assertions.containsExactly
import assertk.assertions.isInstanceOf
import assertk.assertions.hasMessage
import kotlin.livremanagement.domaine.livre.Livre
import kotlin.livremanagement.domaine.port.LivrePort
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.livremanagement.domaine.livremanage.GestionLivre

@ExtendWith(MockKExtension::class)
class GestionLivreTests {
    @InjectMockKs
    private lateinit var gestionlivre: GestionLivre

    @MockK
    private lateinit var port: LivrePort

    @Test
    fun `test recuperation livres trier par titre`() {
        every{port.liste_livre()} returns listOf(
            Livre("Gasly, le magnifique", "Pierre Gasly"),
            Livre("Charles Leclerc, le prodige", "Charles Leclerc"),
            Livre("La mort en face", "Romain Grosjean"),
            Livre("Ma vie sans gravité", "Thomas Pesquet")
        )
        val resultat = gestionlivre.liste_livre()
        assertThat(resultat).containsExactly(
            Livre("Charles Leclerc, le prodige", "Charles Leclerc", false),
            Livre("Gasly, le magnifique", "Pierre Gasly", false),
            Livre("La mort en face", "Romain Grosjean", false),
            Livre("Ma vie sans gravité", "Thomas Pesquet", false)
        )
    }

    @Test
    fun `test creation livre`() {
        justRun {port.creer_livre(any()) }
        val livre = Livre("Charles Leclerc, le prodige", "Charles Leclerc")
        gestionlivre.creer_livre(livre)
        verify {port.creer_livre(livre)}
    }

    @Test
    fun `reserver livre`() {
        every { port.LivreparTitre("La mort en face") } answers { Livre("La mort en face", "Romain Grosjean", false) }
        every { port.reserverLivre("La mort en face") } answers { nothing }
        val livre = Livre("La mort en face", "Romain Grosjean")
        gestionlivre.reserverLivre(livre.titre)
        verify(exactly = 1) { port.reserverLivre(livre.titre) }
    }

    @Test
    fun `livre deja reserver`() {
        every { port.LivreparTitre("La mort en face") } answers { Livre("La mort en face", "Romain Grosjean", true) }
        val livre = Livre("La mort en face", "Romain Grosjean")
        println(livre)
        assertFailure{ gestionlivre.reserverLivre(livre.titre) }
            .isInstanceOf(Exception::class.java)
            .hasMessage("Le livre avec le titre ${livre.titre} est déjà reservé.")
    }

    @Test
    fun `livre existe pas`() {
        every { port.LivreparTitre("Ma vie sans gravité") } answers  { throw NoSuchElementException("Le livre avec le titre Ma vie sans gravité n'a pas était trouvé.") }
        val livre = Livre("Ma vie sans gravité", "Thomas Pesquet")
        assertFailure{ gestionlivre.reserverLivre(livre.titre) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Le livre avec le titre ${livre.titre} n'a pas était trouvé.")
    }

    @Test
    fun `Livre par Titre`() {
        every { port.LivreparTitre(any()) } answers {Livre("Charles Leclerc, le prodige", "Charles Leclerc")}
        val livre = Livre("Charles Leclerc, le prodige", "Charles Leclerc")
        gestionlivre.LivreparTitre(livre.titre)
        verify(exactly = 1) { port.LivreparTitre(livre.titre) }
    }
}