package com.example.demo

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CesarTests {

	@Test
	fun `tester code cesar`() {
		val lettre = 'A'
		val valeur = 2
		val reponse = cesar(lettre, valeur)
		assertThat(reponse).isEqualTo('C')

	}

}
