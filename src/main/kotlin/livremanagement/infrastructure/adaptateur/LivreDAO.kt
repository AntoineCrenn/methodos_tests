package livremanagement.infrastructure.adaptateur

import livremanagement.domaine.livre.Livre
import livremanagement.domaine.port.LivrePort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class LivreDAO(private val parametres: NamedParameterJdbcTemplate): LivrePort {
    override fun liste_livre(): List<Livre> {
        return parametres
            .query("SELECT * FROM LIVRE", MapSqlParameterSource()) { rs, _ ->
                Livre(
                    titre = rs.getString("titre"),
                    auteur = rs.getString("auteur"),
                    reserver = rs.getBoolean("reserver")
                )
            }
    }

    override fun creer_livre(livre: Livre) {
        parametres
            .update("INSERT INTO LIVRE (titre, auteur) values (:titre, :auteur)", mapOf(
                "titre" to livre.titre,
                "auteur" to livre.auteur,
                "reserver" to livre.reserver
            ))
    }

    override fun LivreparTitre(titre: String): Livre? {
        val parametreMap = mapOf("titre" to titre)

        return parametres.queryForObject(
            "SELECT * FROM LIVRE WHERE titre=:titre",
            parametreMap
        ) { rs, _ ->
            Livre(
                titre = rs.getString("titre"),
                auteur = rs.getString("auteur"),
                reserver = rs.getBoolean("reserver")
            )
        }
    }

    override fun reserverLivre(titre: String) {
        parametres
            .update("UPDATE livre SET reserver=true WHERE titre=:titre", mapOf(
                "titre" to titre
            ))
    }
}
