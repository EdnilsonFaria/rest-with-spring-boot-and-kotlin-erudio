package br.com.ednilsonfaria.rest.services

import br.com.ednilsonfaria.rest.controller.BookController
import br.com.ednilsonfaria.rest.controller.PersonController
import br.com.ednilsonfaria.rest.data.vo.v1.BookVO
import br.com.ednilsonfaria.rest.data.vo.v1.PersonVO
import br.com.ednilsonfaria.rest.exceptions.ResourceNotFoundExceptions
import br.com.ednilsonfaria.rest.mapper.DozerMapper
import br.com.ednilsonfaria.rest.model.Book
import br.com.ednilsonfaria.rest.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class BookService {

    @Autowired
    private lateinit var repository : BookRepository
    @Autowired
    private lateinit var assembler : PagedResourcesAssembler<BookVO>
    private val logger = Logger.getLogger(BookService::class.java.name)

    fun findAll(pageable: Pageable): PagedModel<EntityModel<BookVO>> {
        logger.info("Finding all books")
        val books = repository.findAll(pageable)
        val vos = books.map { b ->  DozerMapper.parseObject(b, BookVO::class.java) }
        vos.map { p -> p.add(linkTo(PersonController::class.java).slash(p.key).withSelfRel()) }
        return assembler.toModel(vos)
    }

    fun findById(id: Long) : BookVO {
        logger.info("Finding one book")
        val book = repository.findById(id)
            .orElseThrow { ResourceNotFoundExceptions("No records found for this ID") }
        val bookVO : BookVO = DozerMapper.parseObject(book, BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun create(bookVO: BookVO) : BookVO {
        //if(bookVO == null) throw RequiredObjectIsNullExceptions()
        logger.info("Creating one book with name ${bookVO.title}")
        val book = DozerMapper.parseObject(bookVO, Book::class.java)
        val savedBook = repository.save(book)
        val bookVO : BookVO = DozerMapper.parseObject(savedBook, BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun update(book: BookVO): BookVO {
        //if(book == null) throw RequiredObjectIsNullExceptions()
        logger.info("Updating one book with ID ${book.key}")

        val entity = repository.findById(book.key)
            .orElseThrow { ResourceNotFoundExceptions("No records found for this ID") }

        ReturnEntity(entity, book)

        val savedBook = repository.save(entity)
        val bookVO : BookVO = DozerMapper.parseObject(savedBook, BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    private fun ReturnEntity(entity: Book, book: BookVO) {
        entity.author = book.author
        entity.launchDate = book.launchDate
        entity.price = book.price
        entity.title = book.title
    }

    fun delete(id: Long) {
        logger.info("Creating one book with ID $id")
        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundExceptions("No records found for this ID") }
        repository.delete(entity)
    }

//    private fun mockBook(i: Int): Book {
//        val book = Book(
//            id = counter.incrementAndGet(),
//            firstName = "Book name $i",
//            lastName = "Last name $i",
//            address = "Some address in Brasil",
//            gender = "Male"
//        )
//        return book
//    }
}