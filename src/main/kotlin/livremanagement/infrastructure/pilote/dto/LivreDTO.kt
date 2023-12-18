package kotlin.livremanagement.infrastructure.pilote.dto

import kotlin.livremanagement.domaine.livre.Livre

data class LivreDTO(val titre: String, val auteur: String, val reserver: Boolean) {
    fun domaine(): Livre {
        return Livre(
            titre = this.titre,
            auteur = this.auteur,
            reserver = this.reserver
        )
    }
}

fun Livre.toDto() = LivreDTO(
    titre = this.titre,
    auteur = this.auteur,
    reserver = this.reserver
)