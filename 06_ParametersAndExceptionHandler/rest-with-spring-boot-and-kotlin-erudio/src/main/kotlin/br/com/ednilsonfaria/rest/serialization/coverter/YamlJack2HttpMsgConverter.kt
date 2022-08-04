package br.com.ednilsonfaria.rest.serialization.coverter

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.springframework.http.MediaType

class YamlJack2HttpMsgConverter : AbstractJackson2HttpMessageConverter(

    YAMLMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL),
    MediaType.parseMediaType("application/x-yaml")

)