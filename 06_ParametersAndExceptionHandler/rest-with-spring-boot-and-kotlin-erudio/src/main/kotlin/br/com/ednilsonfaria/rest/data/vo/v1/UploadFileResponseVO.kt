package br.com.ednilsonfaria.rest.data.vo.v1

class UploadFileResponseVO (
    var fileName: String = "",
    var fileDownloadURI: String = "",
    var fileType: String = "",
    var fileSize: Long = 0,
)