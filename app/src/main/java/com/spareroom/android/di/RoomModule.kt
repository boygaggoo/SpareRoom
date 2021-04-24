package com.spareroom.android.di

import android.content.Context
import androidx.room.Room
import com.spareroom.android.room.SpareRoomDatabase
import com.spareroom.android.room.SpareRoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RoomModule{

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): SpareRoomDatabase {
        return Room.databaseBuilder(
            context,
            SpareRoomDatabase::class.java,
            SpareRoomDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(database: SpareRoomDatabase): SpareRoomDao {
        return database.spareRoomDao()
    }

}