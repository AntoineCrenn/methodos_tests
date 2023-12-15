package domaine.livremanage

import domaine.livre.Livre
import domaine.port.LivrePort

class GestionLivre (
    private val livreport: LivrePort ) {

    fun creer_livre(livre: Livre) {
        livreport.creer_livre(livre)
    }

    fun liste_livre(): List<Livre> {
        return livreport.liste_livre().sortedBy {it.titre.lowercase()}
    }
}