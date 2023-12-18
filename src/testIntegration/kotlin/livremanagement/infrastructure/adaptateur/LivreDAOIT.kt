package livremanagement.infrastructure.adaptateur

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlin.livremanagement.domaine.livre.Livre
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.ResultSet
import kotlin.livremanagement.infrastructure.adaptateur.LivreDAO

@SpringBootTest
@Testcontainers
@ActiveProfiles("testIntegration")
class LivreDAOIT {

    @Autowired
    private lateinit var livreDAO: LivreDAO

    @BeforeEach
    fun beforeEach() {
        performQuery(
            // language=sql
            "DELETE FROM livre"
        )
    }

    @Test
    fun `get les livres from db`() {
        performQuery(
            // language=sql
            """
               insert into livre (titre, auteur)
               values 
                    ('Gasly, le magnifique', 'Pierre Gasly'),
                    ('Charles Leclerc, le prodige', 'Charles Leclerc'),
                    ('La mort en face', 'Romain Grosjean'),
                    ('Ma vie sans gravité', 'Thomas Pesquet');
            """.trimIndent())
        val resultat = livreDAO.liste_livre()
        assertThat(resultat).containsExactlyInAnyOrder(
            Livre("Charles Leclerc, le prodige", "Charles Leclerc", false),
            Livre("Gasly, le magnifique", "Pierre Gasly", false),
            Livre("La mort en face", "Romain Grosjean", false),
            Livre("Ma vie sans gravité", "Thomas Pesquet", false)
        )
    }

    @Test
    fun `create livre in db`() {
        livreDAO.creer_livre(Livre("La Terre entre nos mains", "Thomas Pesquet"))
        val resultat = performQuery(
            // language=sql
            "SELECT * from livre"
        )
        assertThat(resultat.size).isEqualTo(1)
        assertThat(resultat[0]["id"]).isNotNull()
        assertThat(resultat[0]["id"] is Int).isTrue()
        assertThat(resultat[0]["titre"]).isEqualTo("La Terre entre nos mains")
        assertThat(resultat[0]["auteur"]).isEqualTo("Thomas Pesquet")
        assertThat(resultat[0]["reserver"]).isEqualTo(false)
    }

    @Test
    fun `reserver livre in db`() {
        performQuery(
            // language=sql
            """
               insert into livre (titre, auteur)
               values 
                   ('La mort en face', 'Romain Grosjean');
            """.trimIndent())
        livreDAO.reserverLivre("La mort en face")
        val resultat = performQuery(
            // language=sql
            "SELECT * from livre WHERE titre='La mort en face'"
        )
        assertThat(resultat.size).isEqualTo(1)
        assertThat(resultat[0]["reserver"]).isEqualTo(true)
        assertThat(resultat[0]["titre"]).isEqualTo("La mort en face")
        assertThat(resultat[0]["auteur"]).isEqualTo("Romain Grosjean")
    }

    @Test
    fun `get un livre par titre in db`() {
        performQuery(
            // language=sql
            """
               insert into livre (titre, auteur)
               values 
                   ('La mort en face', 'Romain Grosjean');
            """.trimIndent())
        val resultat = livreDAO.LivreparTitre("La mort en face")
        assertThat(resultat).isNotNull()
        assertThat(resultat?.reserver).isEqualTo(false)
        assertThat(resultat?.titre).isEqualTo("La mort en face")
        assertThat(resultat?.auteur).isEqualTo("Romain Grosjean")
    }

    protected fun performQuery(sql: String): List<Map<String, Any>> {
        val hikariConfig = HikariConfig()
        hikariConfig.setJdbcUrl(postgresqlContainer.jdbcUrl)
        hikariConfig.username = postgresqlContainer.username
        hikariConfig.password = postgresqlContainer.password
        hikariConfig.setDriverClassName(postgresqlContainer.driverClassName)
        val ds = HikariDataSource(hikariConfig)
        val statement = ds.getConnection().createStatement()
        statement.execute(sql)
        val resultSet = statement.resultSet
        return resultSet?.toList() ?: listOf()
    }

    private fun ResultSet.toList(): List<Map<String, Any>> {
        val md = this.metaData
        val columns = md.columnCount
        val rows: MutableList<Map<String, Any>> = ArrayList()
        while (this.next()) {
            val row: MutableMap<String, Any> = HashMap(columns)
            for (i in 1..columns) {
                row[md.getColumnName(i)] = this.getObject(i)
            }
            rows.add(row)
        }
        return rows
    }

    companion object {
        @Container
        private val postgresqlContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:13-alpine")

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            postgresqlContainer.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgresqlContainer.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
        }
    }
}