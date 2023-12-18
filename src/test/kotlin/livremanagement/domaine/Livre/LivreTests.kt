package livremanagement.domaine.Livre

import assertk.assertFailure
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import kotlin.livremanagement.domaine.livre.Livre
import net.jqwik.api.Arbitraries
import net.jqwik.api.Arbitrary
import net.jqwik.api.Property
import org.junit.jupiter.api.Test

class LivreTests {
    val myBookList: MutableList<Livre> = mutableListOf()
    fun strings(): Arbitrary<String> = Arbitraries.strings().withCharRange('A', 'Z').ofMinLength(3).ofMaxLength(10)

    @Test
    fun `test titre vide`() {
        assertFailure { Livre("", "Pierre Gasly") }
            .isInstanceOf(IllegalArgumentException::class)
            .hasMessage("Le titre ne peut pas être vide.")
    }

    @Test
    fun `test auteur vide`() {
        assertFailure { Livre("Gasly, le magnifique", "") }
            .isInstanceOf(IllegalArgumentException::class)
            .hasMessage("L'auteur ne peut pas être vide.")
    }

    @Property
    fun test() {
        Livre(strings().sample(), strings().sample())
    }

}