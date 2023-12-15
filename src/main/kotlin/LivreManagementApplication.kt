package kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LivreManagementApplication

fun main(args: Array<String>) {
    runApplication<LivreManagementApplication>(*args)
}