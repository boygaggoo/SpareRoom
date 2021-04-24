package com.spareroom.android.repository

import android.util.Log
import com.spareroom.android.model.SpareRoomModel
import com.spareroom.android.retrofit.NetworkMapper
import com.spareroom.android.retrofit.NetworkService
import com.spareroom.android.room.CacheMapper
import com.spareroom.android.room.SpareRoomDao
import com.spareroom.android.utils.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

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
            val networkManga = networkService.getUpcomingEvents()

            Log.d(TAG, "getUpcomingEvents : " + networkManga)

            //Convert network model > domain model.
            val mangas = networkMapper.mapFromEntityList(networkManga)
            //Insert to db one by one.
            for (manga in mangas) {
                spareRoomDao.insert(cacheMapper.mapToEntity(manga))
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

}