package domaine.livre

data class Livre(val titre: String, val auteur: String) {
    init {
        verifiervide()
    }
    private fun verifiervide() {
        if(titre.isNotBlank()) {
            throw IllegalArgumentException("Titre vide")
        }
        if(auteur.isNotBlank()) {
            throw IllegalArgumentException("Auteur vide")
        }
    }
}
