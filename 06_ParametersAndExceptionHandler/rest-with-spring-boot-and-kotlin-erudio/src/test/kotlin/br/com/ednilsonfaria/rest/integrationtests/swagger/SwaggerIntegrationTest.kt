package br.com.ednilsonfaria.rest.integrationtests.swagger

import br.com.ednilsonfaria.rest.integrationtests.TestConfigs
import br.com.ednilsonfaria.rest.integrationtests.testcontainer.AITest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest(): AITest() {

	@Test
	fun shouldDisplaySwaggerUiPage() {
		val content = given()
			.basePath("/swagger-ui/index.html")
			.port(TestConfigs.SERVER_PORT)
				.`when`()
			.get()
			.then()
				.statusCode(200)
			.extract()
			.body()
				.asString()

		assertTrue(content.contains("Swagger UI"))

	}

}
