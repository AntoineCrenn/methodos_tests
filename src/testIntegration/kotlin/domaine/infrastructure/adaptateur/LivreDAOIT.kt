package domaine.infrastructure.adaptateur

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import domaine.livre.Livre
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

@SpringBootTest
@Testcontainers
@ActiveProfiles("testIntegration")
class LivreDAOIT {

    @Autowired
    private lateinit var livreDAO: LivreDA0

    @BeforeEach
    fun beforeEach() {
        performQuery(
            // language=sql
            "DELETE FROM livre"
        )
    }

    @Test
    fun `get livre from db`() {
        performQuery(
            // language=sql
            """
               insert into livre (titre, auteur)
               values 
                    ("Gasly, le magnifique", "Pierre Gasly"),
                    ("Charles Leclerc, le prodige", "Charles Leclerc"),
                    ("La mort en face", "Romain Grosjean"),
                    ("Ma vie sans gravité", "Thomas Pesquet");
            """.trimIndent())

        val resultat = LivreDAO.liste_livre()

        assertThat(resultat).containsExactlyInAnyOrder(
            Livre("Gasly, le magnifique", "Pierre Gasly"),
            Livre("Charles Leclerc, le prodige", "Charles Leclerc"),
            Livre("La mort en face", "Romain Grosjean"),
            Livre("Ma vie sans gravité", "Thomas Pesquet")
        )
    }

    @Test
    fun `create livre in db`() {
        LivreDAO.creer_livre(Livre("La Terre entre nos mains", "Thomas Pesquet"))
        val resultat = performQuery(
            // language=sql
            "SELECT * from livre"
        )

        assertThat(resultat.size).isEqualTo(1)
        assertThat(resultat[0]["id"]).isNotNull()
        assertThat(resultat[0]["id"] is Int).isTrue()
        assertThat(resultat[0]["titre"]).isEqualTo("Gasly, le magnifique")
        assertThat(resultat[0]["auteur"]).isEqualTo("Pierre Gasly")
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