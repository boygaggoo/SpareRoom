package com.spareroom.android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mangas")
data class SpareRoomCacheEntity(
   @PrimaryKey(autoGenerate = false)
   @ColumnInfo(name = "id")
   var id: Int,
   @ColumnInfo(name = "url")
   var url: String,
   @ColumnInfo(name = "image_url")
   var imageUrl: String,
   @ColumnInfo(name = "title")
   var title: String,
   @ColumnInfo(name = "airing")
   var airing: String,
   @ColumnInfo(name = "synopsis")
   var synopsis: String,
   @ColumnInfo(name = "type")
   var type: String,
   @ColumnInfo(name = "episode")
   var episode: Int,
   @ColumnInfo(name = "rated")
   var rated: String
)