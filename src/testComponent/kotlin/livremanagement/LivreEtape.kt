package livremanagement

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import io.restassured.response.ValidatableResponse
import org.springframework.boot.test.web.server.LocalServerPort

class LivreEtape {
    @LocalServerPort
    private var port: Int? = 0

    @Before
    fun etat(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @When("L'utilisateur crée le livre {string} écrit par {string} et {string}.")
    fun creer_livre(titre: String, auteur: String, reserver: String) {
        val reserverBoolean = reserver.toBoolean()
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                    {
                      "titre": "$titre",
                      "auteur": "$auteur",
                      "reserver": $reserverBoolean
                    }
                """.trimIndent()
            )
            .`when`()
            .post("/livres")
            .then()
            .statusCode(201)
    }

    @When("L'utilisateur récupère tous les livres.")
    fun liste_livre() {
        ResultatDernierLivre = given()
            .`when`()
            .get("/livres")
            .then()
            .statusCode(200)
    }

    @When("L'utilisateur réserve le livre intitulé {string}.")
    fun reserverLivre(titre: String) {
        given()
            .contentType(ContentType.JSON)
            .and()
            .`when`()
            .put("/livres/${titre}")
            .then()
            .statusCode(200)
    }

    @When("L'utilisateur récupère le livre intitulé {string}.")
    fun LivreparTitre(titre: String) {
        ResultatLivre = given()
            .`when`()
            .get("/livres/${titre}")
            .then()
            .statusCode(200)
    }

    @Then("La liste doit contenir les livres suivants dans le même ordre.")
    fun Liste_Livre(liste: List<Map<String, Any>>) {
        val reponse = liste.joinToString(separator = ",", prefix = "[", postfix = "]") { ligne ->
            """
                ${
                ligne.entries.joinToString(separator = ",", prefix = "{", postfix = "}") {
                    if (it.key == "reserver") {
                        """"${it.key}": ${it.value}"""
                    } else {
                        """"${it.key}": "${it.value}""""
                    }
                }
            }
            """.trimIndent()
        }
        assertThat(ResultatDernierLivre.extract().body().jsonPath().prettify())
            .isEqualTo(JsonPath(reponse).prettify())
    }

    @Then("La réservation du livre doit être {string}.")
    fun `Status Reservation Livre`(status: String) {
        val reponse = status.toBoolean()
        assertThat(ResultatLivre.extract().body().jsonPath().getBoolean("reserver")).isEqualTo(reponse)
    }

    companion object {
        lateinit var ResultatDernierLivre: ValidatableResponse
        lateinit var ResultatLivre: ValidatableResponse
    }
}