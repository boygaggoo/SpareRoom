package com.spareroom.android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SpareRoom")
data class SpareRoomCacheEntity(
   @PrimaryKey(autoGenerate = true)
   var id : Int = 0,
   @ColumnInfo(name = "image_url")
   var image_url: String,
   @ColumnInfo(name = "location")
   var location: String,
   @ColumnInfo(name = "phone_number")
   var phone_number: String,
   @ColumnInfo(name = "venue")
   var venue: String,
   @ColumnInfo(name = "start_time")
   var start_time: String,
   @ColumnInfo(name = "end_time")
   var end_time: String,
   @ColumnInfo(name = "cost")
   var cost: String
)