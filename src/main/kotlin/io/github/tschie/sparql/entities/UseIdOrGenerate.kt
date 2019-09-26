package io.github.tschie.sparql.entities

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.UUIDGenerator
import java.io.Serializable

/**
 * A generator which only generates a UUID if one is not already set.
 */
class UseIdOrGenerate : UUIDGenerator() {

    @Throws(HibernateException::class)
    override fun generate(session: SharedSessionContractImplementor, `object`: Any): Serializable {
        val id = session.getEntityPersister(null, `object`).classMetadata.getIdentifier(`object`, session)
        return id ?: super.generate(session, `object`)
    }
}