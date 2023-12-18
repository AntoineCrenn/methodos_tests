package kotlin.livremanagement.infrastructure.pilote

import kotlin.livremanagement.domaine.livremanage.GestionLivre
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import kotlin.livremanagement.infrastructure.pilote.dto.toDto
import kotlin.livremanagement.infrastructure.pilote.dto.LivreDTO

@RestController
@RequestMapping("/livres")
class LivreControlleur(
    private val gestionlivre : GestionLivre
) {
    @CrossOrigin
    @GetMapping
    fun liste_livre(): List<LivreDTO> {
        return gestionlivre.liste_livre()
            .map { it.toDto() }
    }

    @CrossOrigin
    @GetMapping("/{titre}")
    fun LivreparTitre(@PathVariable titre : String): LivreDTO? {
        return gestionlivre.LivreparTitre(titre).toDto()
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun ajout_livre(@RequestBody livreDTO: LivreDTO) {
        gestionlivre.creer_livre(livreDTO.domaine())
    }

    @CrossOrigin
    @PutMapping("/{titre}")
    fun reserverLivre(@PathVariable titre : String) {
        gestionlivre.reserverLivre(titre)
    }
}