package livremanagement.infrastructure.application

import livremanagement.domaine.livremanage.GestionLivre
import livremanagement.infrastructure.adaptateur.LivreDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GestionLivreConfiguration {
    @Bean
    fun gestionLivre(livreDAO: LivreDAO): GestionLivre {
        return GestionLivre(livreDAO)
    }
}