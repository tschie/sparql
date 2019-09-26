package io.github.tschie.sparql.controller

import io.github.tschie.sparql.entities.Location
import io.github.tschie.sparql.query.InvalidExpressionException
import io.github.tschie.sparql.query.evaluate
import io.github.tschie.sparql.query.parse
import io.github.tschie.sparql.repository.LocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@Validated
@RequestMapping("locations")
class LocationRestController {

    @Autowired
    lateinit var locationRepository: LocationRepository

    @GetMapping
    fun get(@RequestParam("q") q: String? = null): ResponseEntity<List<Location>> {
        val predicate = q?.let {
            val expression = parse(q)
            try {
                evaluate<Location>(Location::class.java, expression)
            } catch (e: InvalidExpressionException) {
                throw InvalidExpressionException(expression)
            }
        }
        val locations = locationRepository.findAll(predicate)
        return ResponseEntity(locations, HttpStatus.OK)
    }

    @PostMapping
    fun post(@Valid @RequestBody locations: List<Location>) : List<Location> = locationRepository.saveAll(locations)

}