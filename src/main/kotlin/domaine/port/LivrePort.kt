package domaine.port

import domaine.livre.Livre

interface LivrePort {
    fun creer_livre(livre: Livre)

    fun liste_livre(): List<Livre>
}