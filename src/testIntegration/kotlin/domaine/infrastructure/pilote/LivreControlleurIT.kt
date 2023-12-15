package domaine.infrastructure.pilote

import com.ninjasquad.springmockk.MockkBean
import domaine.livre.Livre
import domaine.livremanage.GestionLivre
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@ExtendWith(SpringExtension::class)
@WebMvcTest
class LivreControlleurIT {

    @MockkBean
    private lateinit var gestionLivre: GestionLivre

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `rest route get livre`() {
        every { gestionLivre.liste_livre() } returns listOf(Livre("A", "B"))
        mockMvc.get("/livres")
            .andExpect {
                status { isOk() }
                content { content { APPLICATION_JSON } }
                content { json(
                    // language=json
                    """
                        [
                          {
                            "titre": "A",
                            "auteur": "B"
                          }
                        ]
                    """.trimIndent()
                ) }
            }
    }

    @Test
    fun `rest route post livre`() {
        justRun { gestionLivre.creer_livre(any()) }

        mockMvc.post("/livres") {
            // language=json
            content = """
                {
                  "titre": "Gasly, le magnifique",
                  "auteur": "Pierre Gasly"
                }
            """.trimIndent()
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
        }

        val expected = Livre(
            titre = "Gasly, le magnifique",
            auteur = "Pierre Gasly"
        )

        verify(exactly = 1) { gestionLivre.creer_livre(expected) }
    }

    @Test
    fun `rest route post livre doit retourner 400 quand le corps n'est pas bon`() {
        justRun { gestionLivre.creer_livre(any()) }

        mockMvc.post("/livres") {
            // language=json
            content = """
                {
                  "title": "Gasly, le magnifique",
                  "author": "Pierre Gasly"
                }
            """.trimIndent()
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }

        verify(exactly = 0) { gestionLivre.creer_livre(any()) }
    }
}