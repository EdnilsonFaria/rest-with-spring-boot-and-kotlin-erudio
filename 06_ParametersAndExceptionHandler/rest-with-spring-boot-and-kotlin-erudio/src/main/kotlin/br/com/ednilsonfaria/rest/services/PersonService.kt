package br.com.ednilsonfaria.rest.services

import br.com.ednilsonfaria.rest.controller.PersonController
import br.com.ednilsonfaria.rest.data.vo.v1.PersonVO
import br.com.ednilsonfaria.rest.exceptions.RequiredObjectIsNullExceptions
import br.com.ednilsonfaria.rest.exceptions.ResourceNotFoundExceptions
import br.com.ednilsonfaria.rest.mapper.DozerMapper
import br.com.ednilsonfaria.rest.model.Person
import br.com.ednilsonfaria.rest.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository : PersonRepository
    @Autowired
    private lateinit var assembler : PagedResourcesAssembler<PersonVO>
    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(pageable: Pageable): PagedModel<EntityModel<PersonVO>> {
        logger.info("Finding all people")
        val persons =repository.findAll(pageable)
        val vos = persons.map { p ->  DozerMapper.parseObject(p, PersonVO::class.java) }
        vos.map { p -> p.add(linkTo(PersonController::class.java).slash(p.key).withSelfRel()) }
        return assembler.toModel(vos)

//        val vos = DozerMapper.parseListObjects(persons, PersonVO::class.java)
        /* Linha para inserir o Hateoas */
//        for(p in vos){
//            val withSelfRel = linkTo(PersonController::class.java).slash(p.key).withSelfRel()
//            p.add(withSelfRel)
//        }
//        return vos
    }

    fun findPersonByName(firstName: String, pageable: Pageable): PagedModel<EntityModel<PersonVO>> {
        logger.info("Finding all people")
        val persons =repository.findPersonByName(firstName, pageable)
        val vos = persons.map { p ->  DozerMapper.parseObject(p, PersonVO::class.java) }
        vos.map { p -> p.add(linkTo(PersonController::class.java).slash(p.key).withSelfRel()) }
        return assembler.toModel(vos)
    }

    fun findById(id: Long) : PersonVO {
        logger.info("Finding one person")
        val person = repository.findById(id)
            .orElseThrow { ResourceNotFoundExceptions("No records found for this ID") }
        val personVO : PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun create(personVO: PersonVO) : PersonVO {
        //if(personVO == null) throw RequiredObjectIsNullExceptions()
        logger.info("Creating one person with name ${personVO.firstName}")
        val person = DozerMapper.parseObject(personVO, Person::class.java)
        val savedPerson = repository.save(person)
        val personVO : PersonVO = DozerMapper.parseObject(savedPerson, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun update(person: PersonVO): PersonVO {
        //if(person == null) throw RequiredObjectIsNullExceptions()
        logger.info("Updating one person with ID ${person.key}")

        val entity = repository.findById(person.key)
            .orElseThrow { ResourceNotFoundExceptions("No records found for this ID") }

        ReturnEntity(entity, person)

        val savedPerson = repository.save(entity)
        val personVO : PersonVO = DozerMapper.parseObject(savedPerson, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    @Transactional
    fun disablePerson(id: Long) : PersonVO {
        logger.info("Disabling one person")
        repository.disablePerson(id)
        val person = repository.findById(id)
            .orElseThrow { ResourceNotFoundExceptions("No records found for this ID") }
        val personVO : PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    private fun ReturnEntity(entity: Person, person: PersonVO) {
        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender
    }

    fun delete(id: Long) {
        logger.info("Creating one person with ID $id")
        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundExceptions("No records found for this ID") }
        repository.delete(entity)
    }

//    private fun mockPerson(i: Int): Person {
//        val person = Person(
//            id = counter.incrementAndGet(),
//            firstName = "Person name $i",
//            lastName = "Last name $i",
//            address = "Some address in Brasil",
//            gender = "Male"
//        )
//        return person
//    }
}