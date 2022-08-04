package br.com.ednilsonfaria.rest.integrationtests.testcontainer

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MySQLContainer

//import java.util.stream.Stream
//import java.util.Map

@ContextConfiguration(initializers = [AITest.Initializer::class])
open class AITest {

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext>{

        override fun initialize(applicationContext: ConfigurableApplicationContext) {
//            startContainers()
//
//            val environment = applicationContext.environment
//            val testcontainers = MapPropertySource(
//                "testcontainers", createConnectionConfiguration()
//            )
//            environment.propertySources.addFirst(testcontainers)
        }

        companion object {

            private var mysql: MySQLContainer<*> = MySQLContainer("mysql:8.0.28")

//            private fun startContainers() {
//                Startables.deepStart(Stream.of(mysql)).join()
//            }
//
//            private fun createConnectionConfiguration(): MutableMap<String, Any> {
//                return java.util.Map.of(
//                    "spring.datasource.url", mysql.jdbcUrl,
//                    "spring.datasource.username", mysql.username,
//                    "spring.datasource.password", mysql.password
//                )
//            }
        }
    }
}