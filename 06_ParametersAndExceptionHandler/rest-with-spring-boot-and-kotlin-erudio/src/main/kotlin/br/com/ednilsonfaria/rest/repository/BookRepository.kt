package br.com.ednilsonfaria.rest.repository

import br.com.ednilsonfaria.rest.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long?>