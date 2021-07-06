package de.sixbits.bitspay.unit.main.repository

import de.sixbits.bitspay.ImageEntityFactory
import de.sixbits.bitspay.ImageResponseFactory
import de.sixbits.bitspay.database.dao.CacheDao
import de.sixbits.bitspay.main.repository.MainRepository
import de.sixbits.bitspay.network.manager.PixabayManager
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

@RunWith(JUnit4::class)
class TestMainRepository {

    @Before
    fun setUp() {
        // Preparing RX
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testGetCached() {
        val cacheDao = Mockito.mock(CacheDao::class.java)
        val pixabayManager = Mockito.mock(PixabayManager::class.java)

        val expectedCache = listOf(ImageEntityFactory.getImageItem1())

        Mockito.`when`(cacheDao.getAll())
            .thenReturn(Observable.just(expectedCache))

        val mainRepository = MainRepository(cacheDao = cacheDao, pixabayManager = pixabayManager)

        mainRepository.getCached()
            .test()
            .assertValue {
                it.size == 1
            }.assertValue {
                it[0].id == expectedCache[0].id
            }
    }

    @Test
    fun testSearchFor() {
        val cacheDao = Mockito.mock(CacheDao::class.java)
        val pixabayManager = Mockito.mock(PixabayManager::class.java)

        val expectedCache = listOf(ImageResponseFactory.getImageListItem())

        Mockito.`when`(pixabayManager.getSearchResult(anyString()))
            .thenReturn(Observable.just(expectedCache))

        val mainRepository = MainRepository(cacheDao = cacheDao, pixabayManager = pixabayManager)

        mainRepository.searchFor("Query")
            .test()
            .assertValue {
                it.size == 1
            }.assertValue {
                it[0].id == expectedCache[0].id
            }
    }
}