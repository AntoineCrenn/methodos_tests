package infrastructure.adaptateur

import domaine.livre.Livre
import domaine.port.LivrePort
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
                    auteur = rs.getString("auteur")
                )
            }
    }

    override fun creer_livre(livre: Livre) {
        parametres
            .update("INSERT INTO LIVRE (titre, auteur) values (:titre, :auteur)", mapOf(
                "titre" to livre.titre,
                "auteur" to livre.auteur
            ))
    }
}
