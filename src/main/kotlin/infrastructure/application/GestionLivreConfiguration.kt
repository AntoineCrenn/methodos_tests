package kotlin.infrastructure.application


import domaine.livremanage.GestionLivre
import infrastructure.adaptateur.LivreDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GestionLivreConfiguration {
    @Bean
    fun GestionLivre(livreDAO: LivreDAO): GestionLivre {
        return GestionLivre(livreDAO)
    }
}