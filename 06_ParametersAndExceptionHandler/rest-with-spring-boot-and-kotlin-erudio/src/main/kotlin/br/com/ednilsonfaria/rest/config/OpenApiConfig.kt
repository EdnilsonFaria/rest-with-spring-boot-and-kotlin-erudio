package br.com.ednilsonfaria.rest.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customApi() : OpenAPI{
        return OpenAPI()
            .info(
                Info()
                    .title("RESTFull API with Kotlin 1.6.10 and Spring Boot 3.0.0")
                    .version("v1")
                    .description("Some description")
                    .termsOfService("Terms of service")
                    .license(
                        License().name("Apache 2.0")
                            .url("https://some url")
                    )
            )
    }


}