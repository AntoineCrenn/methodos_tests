package infrastructure.pilote

import domaine.livremanage.GestionLivre
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import infrastructure.pilote.dto.toDto
import infrastructure.pilote.dto.LivreDTO

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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun ajout_livre(@RequestBody livreDTO: LivreDTO) {
        gestionlivre.creer_livre(livreDTO.domaine())
    }

}