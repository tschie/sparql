package io.github.tschie.sparql.controller

import io.github.tschie.sparql.entities.LocationStream
import io.github.tschie.sparql.query.InvalidExpressionException
import io.github.tschie.sparql.query.evaluate
import io.github.tschie.sparql.query.parse
import io.github.tschie.sparql.repository.LocationStreamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@Validated
@RequestMapping("location_streams")
class LocationStreamRestController {

    @Autowired
    lateinit var locationStreamRepository: LocationStreamRepository

    @GetMapping
    fun get(@RequestParam("q") q: String? = null): ResponseEntity<List<LocationStream>> {
        val predicate = q?.let {
            val expression = parse(q)
            try {
                evaluate<LocationStream>(LocationStream::class.java, expression)
            } catch (e: InvalidExpressionException) {
                throw InvalidExpressionException(expression)
            }
        }
        val locationStreams = locationStreamRepository.findAll(predicate)
        return ResponseEntity(locationStreams, HttpStatus.OK)
    }

    @PostMapping
    fun post(@Valid @RequestBody locationStreams: List<LocationStream>) : List<LocationStream> =
            locationStreamRepository.saveAll(locationStreams)

}