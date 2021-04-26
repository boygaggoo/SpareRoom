package com.spareroom.android.repository

import android.util.Log
import com.spareroom.android.model.SpareRoomModel
import com.spareroom.android.retrofit.NetworkMapper
import com.spareroom.android.retrofit.NetworkService
import com.spareroom.android.retrofit.SpareRoomNetworkEntity
import com.spareroom.android.room.CacheMapper
import com.spareroom.android.room.SpareRoomDao
import com.spareroom.android.utils.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainRepository

constructor(
    private val spareRoomDao: SpareRoomDao,
    private val networkService: NetworkService,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
) {

    private val TAG = "MainRepository"

    suspend fun getSpareRoomEvents(): Flow<DataState<List<SpareRoomModel>>> = flow {
        Log.d(TAG, "getManga")

        //Show loading.
        emit(DataState.Loading)

        //Show dummy delay.
        delay(1000)

        try {
            //Get from network.
            var networkUpcomingEvents = networkService.getUpcomingEvents()
            Log.d(TAG, "getUpcomingEvents : " + networkUpcomingEvents)
            val iter: MutableIterator<SpareRoomNetworkEntity> = networkUpcomingEvents.iterator() as MutableIterator<SpareRoomNetworkEntity>
            while (iter.hasNext()) {
                val spareRoomNetworkEntity: SpareRoomNetworkEntity = iter.next()
                if (spareRoomNetworkEntity.phone_number == null || spareRoomNetworkEntity.image_url == null || spareRoomNetworkEntity.cost == null) iter.remove()
            }
            //Convert network model > domain model.
            val eventsList = networkMapper.mapFromEntityList(networkUpcomingEvents)
            //Insert to db one by one.
            for (event in eventsList) {
                 spareRoomDao.insert(cacheMapper.mapToEntity(event))
            }
            //Get all the saved data in db.
            val cacheMangas = spareRoomDao.getAll()
            //Show success with saved data.
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheMangas)))
        } catch (e: Exception) {
            e.printStackTrace()
            //Show error.
            emit(DataState.Error(e))
        }
    }
    suspend fun checkLocalDb(): Flow<DataState<List<SpareRoomModel>>> = flow {
        //Show loading.
        emit(DataState.Loading)
        try {
            //Get all the saved data in db.
            val cacheMangas = spareRoomDao.getAll()
            //Show success with saved data.
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheMangas)))
        } catch (e: Exception) {
            e.printStackTrace()
            //Show error.
            emit(DataState.Error(e))
        }
    }
}