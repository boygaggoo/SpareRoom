package com.spareroom.android.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SpareRoomCacheEntity::class], version = 1)
abstract class SpareRoomDatabase: RoomDatabase() {

    abstract fun spareRoomDao(): SpareRoomDao

    companion object {
        val DATABASE_NAME:String = "spare_room_db"
    }
}