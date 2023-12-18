package kotlin.livremanagement.domaine.livremanage

import kotlin.livremanagement.domaine.livre.Livre
import kotlin.livremanagement.domaine.port.LivrePort

class GestionLivre (
    private val livreport: LivrePort
) {

    fun creer_livre(livre: Livre) {
        livreport.creer_livre(livre)
    }

    fun liste_livre(): List<Livre> {
        return livreport.liste_livre().sortedBy {it.titre.lowercase()}
    }

    fun LivreparTitre(titre: String) : Livre {
        val livre = livreport.LivreparTitre(titre)
        if(livre!=null){
            return livre
        } else {
            throw NoSuchElementException("Le livre avec le titre $titre n'a pas était trouvé.")
        }
    }
    fun reserverLivre(titre: String) {
        val livre = livreport.LivreparTitre(titre)
        if (!livre!!.reserver) {
            livreport.reserverLivre(titre)
        } else {
            throw Exception("Le livre avec le titre $titre est déjà reservé.")
        }
    }
}