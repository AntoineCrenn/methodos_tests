package kotlin.livremanagement.domaine.port

import kotlin.livremanagement.domaine.livre.Livre

interface LivrePort {
    fun creer_livre(livre: Livre)

    fun liste_livre(): List<Livre>
    fun LivreparTitre(titre: String): Livre?
    fun reserverLivre(titre: String)
}