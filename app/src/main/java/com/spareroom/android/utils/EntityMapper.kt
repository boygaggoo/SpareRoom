package com.nyan.ktmvvmhilt.utils

/**
 * Helps from Retrofit Entity converts to Domain model.
 */

interface EntityMapper<Entity, DomainModel>  {

    fun mapFromEntity(entity: Entity): DomainModel

    fun mapToEntity(domain: DomainModel): Entity

}