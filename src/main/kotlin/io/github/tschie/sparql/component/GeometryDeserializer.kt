package io.github.tschie.sparql.component

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.geolatte.geom.Geometry
import org.geolatte.geom.codec.Wkt
import org.springframework.boot.jackson.JsonComponent
import java.io.IOException

@JsonComponent
class GeometryDeserializer : JsonDeserializer<Geometry<*>?>() {

    @Throws(IOException::class)
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Geometry<*>? {
        return p?.let {
            Wkt.fromWkt(p.valueAsString)
        }
    }
}