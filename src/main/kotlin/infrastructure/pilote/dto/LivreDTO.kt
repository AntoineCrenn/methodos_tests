package infrastructure.pilote.dto

import domaine.livre.Livre

data class LivreDTO(val titre: String, val auteur: String) {
    fun domaine(): Livre {
        return Livre(
            titre = this.titre,
            auteur = this.auteur
        )
    }
}

fun Livre.toDto() = LivreDTO(
    titre = this.titre,
    auteur = this.auteur
)