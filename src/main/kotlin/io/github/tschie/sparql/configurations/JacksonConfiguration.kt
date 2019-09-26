package io.github.tschie.sparql.configurations

import com.bedatadriven.jackson.datatype.jts.JtsModule
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat


@Configuration
class JacksonConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df.isLenient = false
        objectMapper.dateFormat = df
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        return objectMapper
    }

    @Bean
    fun jtsModule(): JtsModule {
        return JtsModule()
    }

}