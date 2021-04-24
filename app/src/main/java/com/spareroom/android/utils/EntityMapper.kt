package com.spareroom.android.utils

/**
 * Helps from Retrofit Entity converts to Domain model.
 */

interface EntityMapper<Entity, DomainModel>  {

    fun mapFromEntity(entity: Entity): DomainModel

    fun mapToEntity(domain: DomainModel): Entity

}