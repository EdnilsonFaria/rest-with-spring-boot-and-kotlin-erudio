package br.com.ednilsonfaria.rest.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class RequiredObjectIsNullExceptions : RuntimeException {
    constructor(): super("IS not allowed to persist a null object")
    constructor(exception: String?): super(exception)
}