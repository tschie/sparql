package io.github.tschie.sparql.handlers

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.github.tschie.sparql.query.InvalidExpressionException
import io.github.tschie.sparql.query.UnsupportedExpressionException
import org.apache.catalina.connector.ClientAbortException
import org.postgresql.util.PSQLException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.io.IOException
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<String> {
        return ResponseEntity(e.constraintViolations.map { it.message }.toString(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidFormatException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidFormatException(e: InvalidFormatException) : ResponseEntity<String> {
        return ResponseEntity(e.message?.split(":")?.get(0) ?: "Invalid field format", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidExpressionException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidExpressionException(e: InvalidExpressionException) : ResponseEntity<String> {
        return ResponseEntity("Invalid expression(s) in braces: ${e.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UnsupportedExpressionException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnsupportedExpressionException(e: UnsupportedExpressionException) : ResponseEntity<String> {
        return ResponseEntity("Unsupported expression: ${e.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PropertyReferenceException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlePropertyReferenceException(e: PropertyReferenceException): ResponseEntity<String> {
        return ResponseEntity("No property with name: ${e.propertyName}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoSuchFieldException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNoSuchFieldException(e: NoSuchFieldException) : ResponseEntity<String> {
        return ResponseEntity("No property with name: ${e.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PSQLException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlePSQLException(e: PSQLException) : ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleDataIntegrityViolationException(e: DataIntegrityViolationException) : ResponseEntity<String> {
        val cause = e.cause
        if (cause is PSQLException && cause.message?.contains("duplicate key") == true) {
            return ResponseEntity("Not unique", HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ClientAbortException::class)
    fun handleClientAbortException(e: IOException) {}

}