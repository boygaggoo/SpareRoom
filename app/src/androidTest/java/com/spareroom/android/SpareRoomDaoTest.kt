package com.spareroom.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.spareroom.android.model.SpareRoomModel
import com.spareroom.android.room.CacheMapper
import com.spareroom.android.room.SpareRoomCacheEntity
import com.spareroom.android.room.SpareRoomDao
import com.spareroom.android.room.SpareRoomDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named
import com.google.common.truth.Truth.assertThat

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class SpareRoomDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: SpareRoomDatabase
    private lateinit var spareRoomDao: SpareRoomDao
    private lateinit var spareRoomCacheEntity: SpareRoomCacheEntity
    private lateinit var cacheMapper: CacheMapper

    @Before
    fun setup() {
        hiltRule.inject()
        spareRoomDao = database.spareRoomDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val spareRoomModel1 = SpareRoomModel("https://images.unsplash.com/photo-1582642017153-e36e8796b3f8",
            "Manhattan 1","+44 20 8759 9036","The Penny Farthing","2021-06-18T17:00:00Z","2021-06-18T22:00:00Z","free")
        cacheMapper = CacheMapper()
         var myLong = spareRoomDao.insert(cacheMapper.mapToEntity(spareRoomModel1))
        assertThat(myLong).isNotEqualTo(-1)
        assertThat(cacheMapper.mapFromEntityList(listOf(spareRoomDao.getAll()[0]))).contains(spareRoomModel1)
        print("Test Pass for SpareRoomModel Db Operation")
    }
}