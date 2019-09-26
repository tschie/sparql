package io.github.tschie.sparql.entities

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.github.tschie.sparql.component.GeometryDeserializer
import io.github.tschie.sparql.component.GeometrySerializer
import org.geolatte.geom.C3D
import org.geolatte.geom.Geometry
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


/**
 * A geometry for a specific time range.
 */
@Entity
@Table(name = "location")
class Location {

        /**
         * Unique identifier.
         */
        @Id
        @GenericGenerator(name = "UseIdOrGenerate", strategy = "io.github.tschie.sparql.entities.UseIdOrGenerate")
        @GeneratedValue(generator = "UseIdOrGenerate")
        @Column(columnDefinition = "uuid", updatable = false)
        val id: UUID? = null

        /**
         * The ID of the location stream this location belongs to.
         */
        @Column(name = "location_stream_id", columnDefinition = "uuid", updatable = false)
        val locationStreamId: UUID? = null

        /**
         * Beginning of the time range.
         */
        @field:NotNull(message = "Required parameter is missing: beg")
        val beg: Date? = null

        /**
         * End of the time range.
         */
        @field:NotNull(message = "Required parameter is missing: end")
        val end: Date? = null

        /**
         * Geometry representing the object.
         */
        @field:NotNull(message = "Required parameter is missing: geometry")
        @field:JsonSerialize(using = GeometrySerializer::class)
        @field:JsonDeserialize(using = GeometryDeserializer::class)
        val geometry: Geometry<C3D>? = null

}