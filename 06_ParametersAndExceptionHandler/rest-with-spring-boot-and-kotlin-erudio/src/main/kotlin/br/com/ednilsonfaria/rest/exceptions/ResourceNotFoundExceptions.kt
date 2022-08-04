package br.com.ednilsonfaria.rest.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.*

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundExceptions (exception: String?) : RuntimeException(exception)