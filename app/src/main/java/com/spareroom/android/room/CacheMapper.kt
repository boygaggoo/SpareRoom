package com.spareroom.android.room

import com.nyan.ktmvvmhilt.utils.EntityMapper
import com.spareroom.android.model.SpareRoomModel
import javax.inject.Inject

class CacheMapper

@Inject
constructor(): EntityMapper<SpareRoomCacheEntity, SpareRoomModel> {
    override fun mapFromEntity(entity: SpareRoomCacheEntity): SpareRoomModel {
        return SpareRoomModel(
            id = entity.id,
            url = entity.url,
            image_url = entity.imageUrl,
            title = entity.title,
            airing = entity.airing,
            synopsis = entity.synopsis,
            type = entity.type,
            episode = entity.episode,
            rated = entity.rated
        )
    }

    override fun mapToEntity(domain: SpareRoomModel): SpareRoomCacheEntity {
        return SpareRoomCacheEntity(
            id = domain.id,
            url = domain.url,
            imageUrl = domain.image_url,
            title = domain.title,
            airing = domain.airing,
            synopsis = domain.synopsis,
            type = domain.type,
            episode = domain.episode,
            rated = domain.rated
        )
    }

    fun mapFromEntityList(entities: List<SpareRoomCacheEntity>): List<SpareRoomModel> {
        return entities.map { mapFromEntity(it) }
    }

}