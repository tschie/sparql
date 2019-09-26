package io.github.tschie.sparql.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Size

/**
 * A named stream of locations.
 */
@Entity
@Table(name = "location_stream")
class LocationStream {

    /**
     * Unique identifier.
     */
    @Id
    @GenericGenerator(name = "UseIdOrGenerate", strategy = "io.github.tschie.sparql.entities.UseIdOrGenerate")
    @GeneratedValue(generator = "UseIdOrGenerate")
    @Column(columnDefinition = "uuid", updatable = false)
    val id: UUID? = null

    /**
     * Name of the stream.
     */
    @Column(name = "name", nullable = false)
    @field:Size(min = 2, max = 50)
    val name: String? = null

    /**
     * Locations of the stream.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "location_stream_id")
    @JsonIgnore
    val locations: MutableList<Location>? = mutableListOf()
}