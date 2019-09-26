package io.github.tschie.sparql.component

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.geolatte.geom.Geometry
import org.geolatte.geom.codec.Wkt
import org.springframework.boot.jackson.JsonComponent
import java.io.IOException

@JsonComponent
class GeometrySerializer : JsonSerializer<Geometry<*>?>() {

    @Throws(IOException::class)
    override fun serialize(value: Geometry<*>?, gen: JsonGenerator, provider: SerializerProvider) {
        val wkt = Wkt.toWkt(value)
        gen.writeString(wkt)
    }
}