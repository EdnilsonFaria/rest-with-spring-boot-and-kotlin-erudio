package br.com.ednilsonfaria.rest.converters

object NumberConverter {

    fun convertToDouble(strNumber: String?): Double {
        return if(NumberConverter.isNumeric(strNumber))
            NumberConverter.convertAndReturn(strNumber).toDouble()
        else 0.0
    }

    fun isNumeric(strNumber: String?): Boolean {
        val number = convertAndReturn(strNumber)
        return number.matches("""[-+]?[0-9]*\.?[0-9]+""".toRegex())
    }

    fun convertAndReturn(strNumber: String?) : String{
        if(strNumber.isNullOrBlank()) return ""
        val number = strNumber.replace(",".toRegex(), ".")
        return number
    }

}