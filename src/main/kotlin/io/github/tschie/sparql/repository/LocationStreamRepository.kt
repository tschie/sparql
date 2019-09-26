package io.github.tschie.sparql.repository

import io.github.tschie.sparql.entities.LocationStream
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LocationStreamRepository : JpaRepository<LocationStream, UUID>, JpaSpecificationExecutor<LocationStream>