package br.com.ednilsonfaria.rest.services

import br.com.ednilsonfaria.rest.config.FileStorageConfig
import br.com.ednilsonfaria.rest.exceptions.FileStorageException
import br.com.ednilsonfaria.rest.exceptions.MyFileNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService @Autowired constructor(filestorageConfig: FileStorageConfig) {
    private val fileStorageLocation: Path

    init {
        fileStorageLocation = Paths.get(filestorageConfig.uploadDir).toAbsolutePath().normalize()
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (e: Exception) {
            throw FileStorageException("Couldn´t create the directory where the uploaded files will be storage", e)
        }
    }

    fun storeFile(file: MultipartFile) : String {
        val fileName = StringUtils.cleanPath(file.originalFilename!!)
        return try {
            if(fileName.contains(".."))
                throw FileStorageException("Sorry! The filename $fileName contains invalid path sequence")

            var targetLocation = fileStorageLocation.resolve(fileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            fileName
        } catch (e: Exception) {
            throw FileStorageException("Couldn´t store file $fileName. Please, try again", e)
        }
    }

    fun loadFileAsResource(fileName: String) : Resource {
        return try {
            val filePath = fileStorageLocation.resolve(fileName).normalize()
            val resource : Resource = UrlResource(filePath.toUri())
            if(resource.exists()) resource
            else throw MyFileNotFoundException("File $fileName not found.")
        } catch (e: Exception){
            throw MyFileNotFoundException("File $fileName not found.", e)
        }
    }
}