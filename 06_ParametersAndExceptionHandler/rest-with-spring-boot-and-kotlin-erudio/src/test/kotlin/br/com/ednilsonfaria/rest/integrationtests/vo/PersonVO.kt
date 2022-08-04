package br.com.ednilsonfaria.rest.integrationtests.vo

data class PersonVO(
    var id: Long = 0,
    var firstName : String = "",
    var lastName : String = "",
    var address : String = "",
    var gender : String = ""
)
