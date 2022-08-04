package br.com.ednilsonfaria.rest.integrationtests.controller.cors.withjson

import br.com.ednilsonfaria.rest.integrationtests.TestConfigs
import br.com.ednilsonfaria.rest.integrationtests.testcontainer.AITest
import br.com.ednilsonfaria.rest.integrationtests.vo.AccountCredentialsVO
import br.com.ednilsonfaria.rest.integrationtests.vo.PersonVO
import br.com.ednilsonfaria.rest.integrationtests.vo.TokenVO
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.SpringBootTest
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PCCWJson(): AITest() {

	private lateinit var specification: RequestSpecification
	private lateinit var objectMapper: ObjectMapper
	private lateinit var person: PersonVO
	private lateinit var token: String

	@BeforeAll
	fun setupTests(){
		objectMapper = ObjectMapper()
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
		person = PersonVO()
		token = ""
	}

	@Test
	@Order(0)
	fun authorization() {
		val user = AccountCredentialsVO (
			username = "leandro",
			password = "admin123"
		)

		token = given()
			.basePath("/auth/signin")
			.port(TestConfigs.SERVER_PORT)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.body(user)
			.`when`()
			.post()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.`as`(TokenVO::class.java)
			.accessToken!!
	}

	@Test
	@Order(1)
	fun testCreate() {
		
		mockPerson()

		specification = RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
			.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
			.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(RequestLoggingFilter(LogDetail.ALL))
			.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.body(person)
				.`when`()
			.post()
			.then()
				.statusCode(200)
			.extract()
			.body()
				.asString()

		val createdPerson = objectMapper.readValue(
			content,
			PersonVO::class.java
		)
		assertTrue(content.contains("Swagger UI"))
	}
/*
	@Test
	@Order(2)
	fun testUpdate() {
		person.lastName = "Matthew Stallman"

		val content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.body(person)
			.`when`()
			.put()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString()

		val item = objectMapper.readValue(content, PersonVO::class.java)
		person = item

		assertNotNull(item.id)
		assertNotNull(item.firstName)
		assertNotNull(item.lastName)
		assertNotNull(item.address)
		assertNotNull(item.gender)
		assertEquals(person.id, item.id)
		assertEquals("Richard", item.firstName)
		assertEquals("Matthew Stallman", item.lastName)
		assertEquals("New York City, New York, US", item.address)
		assertEquals("Male", item.gender)
	}

	@Test
	@Order(3)
	fun testFindById() {
		val content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.pathParam("id", person.id)
			.`when`()
			.get("{id}")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString()

		val item = objectMapper.readValue(content, PersonVO::class.java)
		person = item

		assertNotNull(item.id)
		assertNotNull(item.firstName)
		assertNotNull(item.lastName)
		assertNotNull(item.address)
		assertNotNull(item.gender)
		assertEquals(person.id, item.id)
		assertEquals("Richard", item.firstName)
		assertEquals("Matthew Stallman", item.lastName)
		assertEquals("New York City, New York, US", item.address)
		assertEquals("Male", item.gender)
	}

	@Test
	@Order(4)
	fun testDelete() {
		given()
			.spec(specification)
			.pathParam("id", person.id)
			.`when`()
			.delete("{id}")
			.then()
			.statusCode(204)
	}

	@Test
	@Order(5)
	fun testFindAll() {
		val content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.`when`()
			.get()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString()

		val people = objectMapper.readValue(content, Array<PersonVO>::class.java)

		val item1 = people[0]

		assertNotNull(item1.id)
		assertNotNull(item1.firstName)
		assertNotNull(item1.lastName)
		assertNotNull(item1.address)
		assertNotNull(item1.gender)
		assertEquals("Ayrton", item1.firstName)
		assertEquals("Senna", item1.lastName)
		assertEquals("São Paulo", item1.address)
		assertEquals("Male", item1.gender)

		val item2 = people[6]

		assertNotNull(item2.id)
		assertNotNull(item2.firstName)
		assertNotNull(item2.lastName)
		assertNotNull(item2.address)
		assertNotNull(item2.gender)
		assertEquals("Nikola", item2.firstName)
		assertEquals("Tesla", item2.lastName)
		assertEquals("Smiljan - Croatia", item2.address)
		assertEquals("Male", item2.gender)
	}

	@Test
	@Order(5)
	fun testFindAllWithoutToken() {

		val specificationWithoutToken: RequestSpecification = RequestSpecBuilder()
			.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(RequestLoggingFilter(LogDetail.ALL))
			.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		given()
			.spec(specificationWithoutToken)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.`when`()
			.get()
			.then()
			.statusCode(403)
			.extract()
			.body()
			.asString()

	}
 */
	private fun mockPerson() {
		person.firstName = "Richard"
		person.lastName = "Stallman"
		person.address = "New York City, New York, US"
		person.gender = "Male"
	}


}
