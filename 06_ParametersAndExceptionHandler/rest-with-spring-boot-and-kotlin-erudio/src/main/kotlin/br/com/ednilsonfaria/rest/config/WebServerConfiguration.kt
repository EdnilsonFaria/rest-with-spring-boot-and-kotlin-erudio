package br.com.ednilsonfaria.rest.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

//@Configuration
class WebServerConfiguration {

    @Value("\${cors.originPatterns:default}")
    private val corsOriginPatterns: String = ""



}