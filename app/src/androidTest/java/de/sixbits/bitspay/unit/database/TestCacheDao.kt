package de.sixbits.bitspay.unit.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import de.sixbits.bitspay.ImageEntityFactory
import de.sixbits.bitspay.database.dao.CacheDao
import de.sixbits.bitspay.database.database.CacheDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4ClassRunner::class)
class TestCacheDao {

    lateinit var database: CacheDatabase
    lateinit var cacheDao: CacheDao

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, CacheDatabase::class.java).build()

        cacheDao = database.cacheDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGet() {
        val image = ImageEntityFactory.getImageItem1()
        cacheDao.insert(image).test()

        cacheDao.getById(image.id)
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue { it.id == image.id }
    }

    @Test
    fun testWithDuplication() {
        // Test With duplications
        val image1 = ImageEntityFactory.getImageItem1()
        cacheDao.insert(image1).test()

        val image2 = ImageEntityFactory.getImageItem1()
        cacheDao.insert(image2).test()

        cacheDao.getById(image2.id)
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue { it.createdAt == image2.createdAt }
    }

    @Test
    fun testWithMultipleObjects() {
        var image = ImageEntityFactory.getImageItem1()
        cacheDao.insert(image).test()

        image = ImageEntityFactory.getImageItem2()
        cacheDao.insert(image).test()

        cacheDao.getAll()
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue { it.size == 2 }
    }
}
