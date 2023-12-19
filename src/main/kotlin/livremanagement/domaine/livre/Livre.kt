package livremanagement.domaine.livre

data class Livre(val titre: String, val auteur: String, val reserver: Boolean = false)