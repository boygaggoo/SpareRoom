package com.spareroom.android.room

import com.spareroom.android.model.SpareRoomModel
import com.spareroom.android.utils.EntityMapper
import javax.inject.Inject

class CacheMapper

@Inject
constructor(): EntityMapper<SpareRoomCacheEntity, SpareRoomModel> {
    override fun mapFromEntity(entity: SpareRoomCacheEntity): SpareRoomModel {
        return SpareRoomModel(
            image_url = entity.image_url,
            location = entity.location,
            phone_number = entity.phone_number,
            venue = entity.venue,
            end_time = entity.end_time,
            start_time = entity.start_time,
            cost = entity.cost
        )
    }

    override fun mapToEntity(domain: SpareRoomModel): SpareRoomCacheEntity {
        return SpareRoomCacheEntity(
            image_url = domain.image_url,
            location = domain.location,
            phone_number = domain.phone_number,
            venue = domain.venue,
            end_time = domain.end_time,
            start_time = domain.start_time,
            cost = domain.cost
        )
    }

    fun mapFromEntityList(entities: List<SpareRoomCacheEntity>): List<SpareRoomModel> {
        return entities.map { mapFromEntity(it) }
    }

}