package kotlin.livremanagement.infrastructure.application

import kotlin.livremanagement.domaine.livremanage.GestionLivre
import kotlin.livremanagement.infrastructure.adaptateur.LivreDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GestionLivreConfiguration {
    @Bean
    fun gestionLivre(livreDAO: LivreDAO): GestionLivre {
        return GestionLivre(livreDAO)
    }
}