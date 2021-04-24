package com.spareroom.android.di


import com.spareroom.android.retrofit.NetworkService
import com.spareroom.android.room.SpareRoomDao
import com.spareroom.android.repository.MainRepository
import com.spareroom.android.room.CacheMapper
import com.spareroom.android.retrofit.NetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        dao: SpareRoomDao,
        networkService: NetworkService,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): MainRepository {
        return MainRepository(dao, networkService, cacheMapper, networkMapper)
    }


}