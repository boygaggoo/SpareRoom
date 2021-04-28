package com.spareroom.android.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SpareRoomDao  {

    //Return rows number OR -1 if failed.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mangaEntity: SpareRoomCacheEntity): Long
    
    @Query("SELECT * FROM SpareRoom")
    suspend fun getAll(): List<SpareRoomCacheEntity>

}